package parser;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;

public class Parser {
	Lexer lexer;
	Token current;
	String fname;
	Boolean errorHappen = false;

	public Parser(String fname, java.io.InputStream fstream) {
		this.lexer = new Lexer(fname, fstream);
		this.current = lexer.nextToken();
		this.fname = fname;
	}

	// /////////////////////////////////////////////
	// utility methods to connect the lexer
	// and the parser.

	private void advance() {
		current = lexer.nextToken();
	}

	private void eatToken(Kind kind) throws Exception {
		if (kind == current.kind)
			advance();
		else {
			StringBuffer errorInfo = new StringBuffer();
			errorInfo.append("Expects: " + kind.toString());
			errorInfo.append("   But got: " + current.kind.toString() + ":  " + ((current.lexeme == null) ? "" : current.lexeme)
					+ "   at line: " + current.lineNum + " column: " + current.columnNum);
			errorHappen = true;
			throw new Exception(errorInfo.toString());
		}
	}

	private void error() throws Exception {
		StringBuffer errorInfo = new StringBuffer();
		errorInfo.append("Syntax error:");
		errorInfo.append("at file: " + fname + "   got: " + current.kind.toString() + ":  "
				+ ((current.lexeme == null) ? "" : current.lexeme) + "   line: " + current.lineNum + "   column: " + current.columnNum);
		errorHappen = true;
		throw new Exception(errorInfo.toString());
	}

	private void errorHandler(Exception e) {
		System.err.println(e);
		int line = current.lineNum;
		while (current != null && current.lineNum == line && current.kind != Kind.TOKEN_LBRACE && current.kind != Kind.TOKEN_RBRACE
				&& current.kind != Kind.TOKEN_RETURN) {
			advance();
		}
	}

	private boolean isType(Token temp) {
		return temp.kind == Kind.TOKEN_INT || temp.kind == Kind.TOKEN_BOOLEAN || temp.kind == Kind.TOKEN_ID;
	}

	// ////////////////////////////////////////////////////////////
	// below are method for parsing.

	// A bunch of parsing methods to parse expressions. The messy
	// parts are to deal with precedence and associativity.

	// ExpList -> Exp ExpRest*
	// ->
	// ExpRest -> , Exp
	private java.util.LinkedList<ast.exp.T> parseExpList() throws Exception {
		java.util.LinkedList<ast.exp.T> exps = new java.util.LinkedList<ast.exp.T>();
		if (current.kind == Kind.TOKEN_RPAREN)
			return exps;
		exps.add(parseExp());
		while (current.kind == Kind.TOKEN_COMMER) {
			advance();
			exps.add(parseExp());
		}
		return exps;
	}

	// AtomExp -> (exp)
	// -> INTEGER_LITERAL
	// -> true
	// -> false
	// -> this
	// -> id
	// -> new int [exp]
	// -> new id ()
	private ast.exp.T parseAtomExp() throws Exception {
		ast.exp.T exp = null;
		switch (current.kind) {
		case TOKEN_LPAREN:
			advance();
			exp = new ast.exp.Block(parseExp());
			eatToken(Kind.TOKEN_RPAREN);
			return exp;
		case TOKEN_NUM:
			exp = new ast.exp.Num(Integer.parseInt(current.lexeme), current.lineNum);
			advance();
			return exp;
		case TOKEN_TRUE:
			exp = new ast.exp.True();
			advance();
			return exp;
		case TOKEN_FALSE:
			exp = new ast.exp.False();
			advance();
			return exp;
		case TOKEN_THIS:
			exp = new ast.exp.This();
			advance();
			return exp;
		case TOKEN_ID:
			exp = new ast.exp.Id(current.lexeme, current.lineNum);
			advance();
			return exp;
		case TOKEN_NEW: {
			advance();
			switch (current.kind) {
			case TOKEN_INT:
				advance();
				eatToken(Kind.TOKEN_LBRACK);
				exp = new ast.exp.NewIntArray(parseExp());
				eatToken(Kind.TOKEN_RBRACK);
				return exp;
			case TOKEN_ID:
				String id = current.lexeme;
				advance();
				eatToken(Kind.TOKEN_LPAREN);
				eatToken(Kind.TOKEN_RPAREN);
				return new ast.exp.NewObject(id, null, current.lineNum);
			default:
				error();
				return exp;
			}
		}
		default:
			error();
			return exp;
		}
	}

	// NotExp -> AtomExp
	// -> AtomExp .id (expList)
	// -> AtomExp [exp]
	// -> AtomExp .length
	private ast.exp.T parseNotExp() throws Exception {
		ast.exp.T exp = parseAtomExp();
		while (current.kind == Kind.TOKEN_DOT || current.kind == Kind.TOKEN_LBRACK) {
			if (current.kind == Kind.TOKEN_DOT) {
				advance();
				if (current.kind == Kind.TOKEN_LENGTH) {
					advance();
					return new ast.exp.Length(exp);
				}
				String id = current.lexeme;
				eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_LPAREN);

				exp = new ast.exp.Call(exp, id, parseExpList(), current.lineNum);
				eatToken(Kind.TOKEN_RPAREN);
			} else {
				advance();
				exp = new ast.exp.ArraySelect(exp, parseExp(), current.lineNum);
				eatToken(Kind.TOKEN_RBRACK);
			}
		}
		return exp;
	}

	// TimesExp -> ! TimesExp
	// -> NotExp
	private ast.exp.T parseTimesExp() throws Exception {
		int j = 0;
		while (current.kind == Kind.TOKEN_NOT) {
			advance();
			j++;
		}

		ast.exp.T exp = parseNotExp();
		if (!((j & 1) == 0)) {
			exp = new ast.exp.Not(exp, current.lineNum);
		}

		return exp;
	}

	// AddSubExp -> TimesExp * TimesExp
	// -> TimesExp
	private ast.exp.T parseAddSubExp() throws Exception {
		ast.exp.T exp = parseTimesExp();
		while (current.kind == Kind.TOKEN_TIMES) {
			advance();
			exp = new ast.exp.Times(exp, parseTimesExp());
		}
		return exp;
	}

	// LtExp -> AddSubExp + AddSubExp
	// -> AddSubExp - AddSubExp
	// -> AddSubExp
	private ast.exp.T parseLtExp() throws Exception {
		ast.exp.T exp = parseAddSubExp();
		while (current.kind == Kind.TOKEN_ADD || current.kind == Kind.TOKEN_SUB || current.kind == Kind.TOKEN_TIMES) {
			if (current.kind == Kind.TOKEN_ADD) {
				advance();
				exp = new ast.exp.Add(exp, parseAddSubExp());
			}
			if (current.kind == Kind.TOKEN_SUB) {
				advance();
				exp = new ast.exp.Sub(exp, parseAddSubExp());
			}
			if (current.kind == Kind.TOKEN_TIMES) {
				advance();
				exp = new ast.exp.Times(exp, parseAddSubExp());
			}
		}
		return exp;
	}

	// AndExp -> LtExp < LtExp
	// -> LtExp
	private ast.exp.T parseAndExp() throws Exception {
		ast.exp.T exp = parseLtExp();
		while (current.kind == Kind.TOKEN_LT || current.kind == Kind.TOKEN_GT) {
			Kind k = current.kind;
			advance();
			if (k == Kind.TOKEN_LT)
				exp = new ast.exp.Lt(exp, parseLtExp());
			else
				exp = new ast.exp.Gt(exp, parseLtExp());
		}

		return exp;
	}

	// Exp -> AndExp && AndExp
	// -> AndExp
	private ast.exp.T parseExp() throws Exception {
		ast.exp.T exp = parseAndExp();

		while (current.kind == Kind.TOKEN_AND) {
			advance();
			exp = new ast.exp.And(exp, parseAndExp());
		}
		return exp;
	}

	// Statement -> { Statement* }
	// -> if ( Exp ) Statement else Statement
	// -> while ( Exp ) Statement
	// -> System.out.println ( Exp ) ;
	// -> id = Exp ;
	// -> id [ Exp ]= Exp ;
	private ast.stm.T parseStatement(java.util.LinkedList<ast.dec.T> locals) throws Exception {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a statement.
		int lineNum = 0;
		switch (current.kind) {
		case TOKEN_LBRACE:
			advance();

			java.util.LinkedList<ast.stm.T> stms = null;
			stms = parseStatements(locals);
			eatToken(Kind.TOKEN_RBRACE);
			return new ast.stm.Block(stms);

		case TOKEN_IF:
			ast.exp.T ifCondition = null;
			ast.stm.T ifThen = null;
			ast.stm.T ifElse = null;
			try {
				advance();
				eatToken(Kind.TOKEN_LPAREN);
				lineNum = current.lineNum;
				ifCondition = parseExp();
				eatToken(Kind.TOKEN_RPAREN);
			} catch (Exception e) {
				errorHandler(e);
			}
			ifThen = parseStatement(locals);

			eatToken(Kind.TOKEN_ELSE);
			ifElse = parseStatement(locals);

			return new ast.stm.If(ifCondition, ifThen, ifElse, lineNum);

		case TOKEN_WHILE:
			ast.exp.T whileCondition = null;
			ast.stm.T whileBody = null;
			try {
				advance();
				eatToken(Kind.TOKEN_LPAREN);
				lineNum = current.lineNum;
				whileCondition = parseExp();
				eatToken(Kind.TOKEN_RPAREN);
			} catch (Exception e) {
				errorHandler(e);
			}

			whileBody = parseStatement(locals);
			ast.stm.T while_stm = new ast.stm.While(whileCondition, whileBody, lineNum);
			return while_stm;

		case TOKEN_SYSTEM:
			ast.stm.T systemStm;
			advance();
			eatToken(Kind.TOKEN_DOT);
			eatToken(Kind.TOKEN_OUT);
			eatToken(Kind.TOKEN_DOT);
			eatToken(Kind.TOKEN_PRINTLN);
			eatToken(Kind.TOKEN_LPAREN);
			systemStm = new ast.stm.Print(parseExp());
			eatToken(Kind.TOKEN_RPAREN);
			eatToken(Kind.TOKEN_SEMI);
			return systemStm;
		case TOKEN_ID:
			String idId = current.lexeme;
			advance();
			if (current.kind == Kind.TOKEN_LBRACK) {
				advance();
				ast.exp.T index = parseExp();
				eatToken(Kind.TOKEN_RBRACK);
				eatToken(Kind.TOKEN_ASSIGN);
				ast.exp.T exp = parseExp();
				eatToken(Kind.TOKEN_SEMI);
				return new ast.stm.AssignArray(new ast.exp.Id(idId), index, exp);
			} else if (current.kind == Kind.TOKEN_ASSIGN) {
				advance();
				ast.exp.T exp = parseExp();
				eatToken(Kind.TOKEN_SEMI);
				return new ast.stm.Assign(new ast.exp.Id(idId), exp);
			} else {
				String classId = current.lexeme;
				lineNum = current.lineNum;
				eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_SEMI);
				locals.add(new ast.dec.Dec(new ast.type.Class(idId, lineNum), classId, lineNum));
				return null;
			}
		case TOKEN_BOOLEAN:
			advance();
			lineNum = current.lineNum;
			String booleanId = current.lexeme;
			eatToken(Kind.TOKEN_ID);
			eatToken(Kind.TOKEN_SEMI);

			locals.add(new ast.dec.Dec(new ast.type.Boolean(lineNum), booleanId, lineNum));
			return null;
		case TOKEN_INT:
			advance();
			if (current.kind == Kind.TOKEN_LBRACK) {
				advance();
				lineNum = current.lineNum;
				eatToken(Kind.TOKEN_RBRACK);
				String arrayId = current.lexeme;
				eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_SEMI);
				locals.add(new ast.dec.Dec(new ast.type.IntArray(lineNum), arrayId, lineNum));
				return null;
			} else {
				String intId = current.lexeme;
				lineNum = current.lineNum;
				eatToken(Kind.TOKEN_ID);
				eatToken(Kind.TOKEN_SEMI);
				locals.add(new ast.dec.Dec(new ast.type.Int(lineNum), intId, lineNum));
				return null;
			}
		default:
			error();
		}
		return null;
	}

	// Statements -> Statement Statements
	// ->
	private java.util.LinkedList<ast.stm.T> parseStatements(java.util.LinkedList<ast.dec.T> locals) {

		java.util.LinkedList<ast.stm.T> stms = new java.util.LinkedList<ast.stm.T>();

		while (current.kind == Kind.TOKEN_LBRACE || current.kind == Kind.TOKEN_IF || current.kind == Kind.TOKEN_WHILE
				|| current.kind == Kind.TOKEN_SYSTEM || isType(current)) {

			try {
				ast.stm.T temp = parseStatement(locals);
				if (temp != null)
					stms.add(temp);
			} catch (Exception e) {
				errorHandler(e);
			}
		}
		return stms;
	}

	// Type -> int []
	// -> boolean
	// -> int
	// -> id
	private ast.type.T parseType() throws Exception {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a type.
		ast.type.T type = null;
		if (current.kind == Kind.TOKEN_INT) {
			int lineNum = current.lineNum;
			advance();
			if (current.kind == Kind.TOKEN_LBRACK) {
				advance();
				eatToken(Kind.TOKEN_RBRACK);
				type = new ast.type.IntArray(lineNum);
			} else
				type = new ast.type.Int(lineNum);
		} else if (current.kind == Kind.TOKEN_BOOLEAN) {
			int lineNum = current.lineNum;
			advance();
			type = new ast.type.Boolean(lineNum);
		} else if (current.kind == Kind.TOKEN_ID) {
			int lineNum = current.lineNum;
			type = new ast.type.Class(current.lexeme, lineNum);
			advance();
		} else
			error();
		return type;
	}

	// VarDecl -> Type id ;
	private ast.dec.T parseVarDecl() throws Exception {
		// to parse the "Type" nonterminal in this method, instead of writing
		// a fresh one.
		ast.type.T type = parseType();
		String id = current.lexeme;
		int lineNum = current.lineNum;
		eatToken(Kind.TOKEN_ID);
		eatToken(Kind.TOKEN_SEMI);

		ast.dec.Dec dec = new ast.dec.Dec(type, id, lineNum);
		return dec;
	}

	// VarDecls -> VarDecl VarDecls
	// ->
	private java.util.LinkedList<ast.dec.T> parseVarDecls() {
		java.util.LinkedList<ast.dec.T> decls = new java.util.LinkedList<ast.dec.T>();
		while (isType(current)) {
			try {
				decls.add(parseVarDecl());
			} catch (Exception e) {
				errorHandler(e);
			}
		}
		return decls;
	}

	// FormalList -> Type id FormalRest*
	// ->
	// FormalRest -> , Type id
	private java.util.LinkedList<ast.dec.T> parseFormalList() throws Exception {
		java.util.LinkedList<ast.dec.T> formals = new java.util.LinkedList<ast.dec.T>();
		if (isType(current)) {
			ast.type.T type = parseType();
			String id = current.lexeme;
			int lineNum = current.lineNum;
			eatToken(Kind.TOKEN_ID);
			formals.add(new ast.dec.Dec(type, id, lineNum));
			while (current.kind == Kind.TOKEN_COMMER) {
				advance();
				type = parseType();
				id = current.lexeme;
				eatToken(Kind.TOKEN_ID);
				formals.add(new ast.dec.Dec(type, id, lineNum));
			}
		}
		return formals;
	}

	// Method -> public Type id ( FormalList )
	// { VarDecl* Statement* return Exp ;}

	private ast.method.T parseMethod() throws Exception {
		// Lab1. Exercise 4: Fill in the missing code
		// to parse a method.
		ast.type.T retType = null;
		ast.exp.T retExp = null;
		String id = null;
		java.util.LinkedList<ast.dec.T> formals = null;
		java.util.LinkedList<ast.dec.T> locals = new java.util.LinkedList<ast.dec.T>();
		java.util.LinkedList<ast.stm.T> stms = null;
		int lineNum = current.lineNum;
		try {
			advance();
			retType = parseType();
			id = current.lexeme;
			eatToken(Kind.TOKEN_ID);
			eatToken(Kind.TOKEN_LPAREN);
			formals = parseFormalList();
			eatToken(Kind.TOKEN_RPAREN);
		} catch (Exception e) {
			errorHandler(e);
		}
		try {
			eatToken(Kind.TOKEN_LBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}

		stms = parseStatements(locals);

		try {
			eatToken(Kind.TOKEN_RETURN);
			retExp = parseExp();
			eatToken(Kind.TOKEN_SEMI);
		} catch (Exception e) {
			errorHandler(e);
		}

		eatToken(Kind.TOKEN_RBRACE);
		return new ast.method.Method(retType, id, formals, locals, stms, retExp, lineNum);
	}

	// MethodDecls -> MethodDecl MethodDecls
	// ->
	private java.util.LinkedList<ast.method.T> parseMethodDecls() {
		java.util.LinkedList<ast.method.T> methods = new java.util.LinkedList<ast.method.T>();
		while (current.kind == Kind.TOKEN_PUBLIC) {
			try {
				methods.add(parseMethod());
			} catch (Exception e) {
				errorHandler(e);
			}
		}
		return methods;
	}

	// ClassDecl -> class id { VarDecl* MethodDecl* }
	// -> class id extends id { VarDecl* MethodDecl* }
	private ast.classs.T parseClassDecl() {
		String id = null;
		String extendss = null;
		java.util.LinkedList<ast.dec.T> decs = null;
		java.util.LinkedList<ast.method.T> methods = null;
		int lineNum = -1;
		try {
			eatToken(Kind.TOKEN_CLASS);
			id = current.lexeme;
			eatToken(Kind.TOKEN_ID);
			lineNum = current.lineNum;
			if (current.kind == Kind.TOKEN_EXTENDS) {
				advance();
				extendss = current.lexeme;
				eatToken(Kind.TOKEN_ID);
			}
		} catch (Exception e) {
			errorHandler(e);
		}

		try {
			eatToken(Kind.TOKEN_LBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}

		decs = parseVarDecls();
		methods = parseMethodDecls();
		try {
			eatToken(Kind.TOKEN_RBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}
		return new ast.classs.Class(id, extendss, decs, methods, lineNum);
	}

	// ClassDecls -> ClassDecl ClassDecls
	// ->
	private java.util.LinkedList<ast.classs.T> parseClassDecls() {
		java.util.LinkedList<ast.classs.T> classes = new java.util.LinkedList<ast.classs.T>();
		while (current.kind == Kind.TOKEN_CLASS)
			classes.add(parseClassDecl());
		return classes;
	}

	// MainClass -> class id
	// {
	// public static void main ( String [] id )
	// {
	// Statement
	// }
	// }
	private ast.mainClass.T parseMainClass() {
		String id = null;
		String arg = null;
		java.util.LinkedList<ast.stm.T> stms = null;
		java.util.LinkedList<ast.dec.T> decs = new java.util.LinkedList<ast.dec.T>();
		try {
			eatToken(Kind.TOKEN_CLASS);
			id = current.lexeme;
			eatToken(Kind.TOKEN_ID);
		} catch (Exception e) {
			errorHandler(e);
		}

		try {
			eatToken(Kind.TOKEN_LBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}

		try {
			eatToken(Kind.TOKEN_PUBLIC);
			eatToken(Kind.TOKEN_STATIC);
			eatToken(Kind.TOKEN_VOID);
			eatToken(Kind.TOKEN_MAIN);
			eatToken(Kind.TOKEN_LPAREN);
			eatToken(Kind.TOKEN_STRING);
			eatToken(Kind.TOKEN_LBRACK);
			eatToken(Kind.TOKEN_RBRACK);
			arg = current.lexeme;
			eatToken(Kind.TOKEN_ID);
			eatToken(Kind.TOKEN_RPAREN);
		} catch (Exception e) {
			errorHandler(e);
		}

		try {
			eatToken(Kind.TOKEN_LBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}

		stms = parseStatements(decs);

		try {
			eatToken(Kind.TOKEN_RBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}

		try {
			eatToken(Kind.TOKEN_RBRACE);
		} catch (Exception e) {
			errorHandler(e);
		}
		ast.mainClass.MainClass mainClass = new ast.mainClass.MainClass(id, arg, stms);

		return mainClass;
	}

	// Program -> MainClass ClassDecl*
	private ast.program.T parseProgram() {

		ast.mainClass.T mainClass = parseMainClass();
		java.util.LinkedList<ast.classs.T> classes = parseClassDecls();
		try {
			eatToken(Kind.TOKEN_EOF);
		} catch (Exception e) {
			errorHandler(e);
		}
		ast.program.Program prog = new ast.program.Program(mainClass, classes);
		return prog;
	}

	public ast.program.T parse() {
		ast.program.T prog = null;
		prog = parseProgram();
		if (errorHappen)
			System.exit(1);
		return prog;
	}

}
