package lexer;

import java.io.InputStream;
import java.io.PushbackInputStream;

import lexer.Token.Kind;
import util.Keyword;

public class Lexer {
	String fname; // the input file name to be compiled
	PushbackInputStream fstream; // input stream for the above file
	Integer lineNum;
	Integer columnNum;
	Integer currentState;

	public Lexer(String fname, InputStream fstream) {
		this.fname = fname;
		this.fstream = new PushbackInputStream(fstream);
		this.lineNum = 1;
		this.columnNum = 1;
	}

	private void error() throws Exception {
		throw new Exception("token type error! at line:" + lineNum + " at column:" + columnNum);
	}

	private int read() throws Exception {
		int c = this.fstream.read();
		if (c == '\r')
			c = this.fstream.read();
		return c;
	}

	// When called, return the next token (refer to the code "Token.java")
	// from the input stream.
	// Return TOKEN_EOF when reaching the end of the input stream.
	private Token nextTokenInternal() throws Exception {
		int c = read();
		if (-1 == c)
			// The value for "lineNum" is now "null",
			// you should modify this to an appropriate
			// line number for the "EOF" token.
			return new Token(Kind.TOKEN_EOF, lineNum, columnNum++);
		// skip all kinds of "blanks"

		while (' ' == c || '\t' == c || '\n' == c) {
			if ('\n' == c) {
				lineNum++;
				columnNum = 1;
			}
			c = read();
		}

		this.currentState = 0;
		StringBuffer buf = new StringBuffer();
		while (c != -1) {
			switch (currentState) {
			case 0:
				switch (c) {
				case '+':
					return new Token(Kind.TOKEN_ADD, lineNum, columnNum++);
				case '-':
					return new Token(Kind.TOKEN_SUB, lineNum, columnNum++);
				case '*':
					return new Token(Kind.TOKEN_TIMES, lineNum, columnNum++);
				case ',':
					return new Token(Kind.TOKEN_COMMER, lineNum, columnNum++);
				case '.':
					return new Token(Kind.TOKEN_DOT, lineNum, columnNum++);
				case ';':
					return new Token(Kind.TOKEN_SEMI, lineNum, columnNum++);
				case '(':
					return new Token(Kind.TOKEN_LPAREN, lineNum, columnNum++);
				case ')':
					return new Token(Kind.TOKEN_RPAREN, lineNum, columnNum++);
				case '[':
					return new Token(Kind.TOKEN_LBRACK, lineNum, columnNum++);
				case ']':
					return new Token(Kind.TOKEN_RBRACK, lineNum, columnNum++);
				case '{':
					return new Token(Kind.TOKEN_LBRACE, lineNum, columnNum++);
				case '}':
					return new Token(Kind.TOKEN_RBRACE, lineNum, columnNum++);
				case '>':
					currentState = 3;
					break;
				case '<':
					currentState = 4;
					break;
				case '=':
					currentState = 5;
					break;
				case '!':
					currentState = 6;
					break;
				case '&':
					currentState = 7;
					break;
				case '|':
					currentState = 8;
					break;
				case '/':
					currentState = 9;
					break;
				case '"':
					currentState = 10;
					break;
				case '\'':
					currentState = 11;
					break;
				default:
					buf = buf.append((char) c);
					if (Character.isDigit((char) c)) {
						currentState = 1;
						break;
					} else if ((char) c >= 'a' && (char) c <= 'z' || (char) c >= 'A' && (char) c <= 'Z' || (char) c == '_') {
						currentState = 2;
						break;
					} else
						error();
				}
				break;
			case 1:
				if (Character.isDigit((char) c)) {
					buf = buf.append((char) c);
					break;
				} else if (' ' == c || '<' == c || '>' == c || '\t' == c || '\n' == c || ',' == c || ';' == c || ']' == c || ')' == c
						|| '+' == c || '-' == c || '*' == c || '/' == c || '&' == c || '|' == c || '%' == c) {
					fstream.unread(c);
					return new Token(Kind.TOKEN_NUM, lineNum, columnNum++, buf.toString());
				} else
					error();
			case 2:
				if ((char) c >= 'a' && (char) c <= 'z' || (char) c >= 'A' && (char) c <= 'Z' || (char) c == '_'
						|| Character.isDigit((char) c)) {
					buf = buf.append((char) c);
					break;
				} else {// if (' ' == c || '\t' == c || '\n' == c || '.' == c ||
						// ';' == c || '[' == c || ']' == c || '(' == c || ')'
						// == c || '{' == c || '+' == c || '-' == c || '*' == c
						// || '/' == c || '&' == c || '|' == c || '%' == c)
					fstream.unread(c);
					Kind kind = Keyword.keywords.get(buf.toString());
					if (kind != null)
						return new Token(kind, lineNum, columnNum++);
					return new Token(Kind.TOKEN_ID, lineNum, columnNum++, buf.toString());
				}
			case 3:
				if (c == '=')
					return new Token(Kind.TOKEN_GE, lineNum, columnNum++);
				else {
					fstream.unread(c);
					return new Token(Kind.TOKEN_GT, lineNum, columnNum++);
				}
			case 4:
				if (c == '=')
					return new Token(Kind.TOKEN_LE, lineNum, columnNum++);
				else {
					fstream.unread(c);
					return new Token(Kind.TOKEN_LT, lineNum, columnNum++);
				}
			case 5:
				if (c == '=')
					return new Token(Kind.TOKEN_EQUAL, lineNum, columnNum++);
				else {
					fstream.unread(c);
					return new Token(Kind.TOKEN_ASSIGN, lineNum, columnNum++);
				}
			case 6:
				if (c == '=')
					return new Token(Kind.TOKEN_NOTEQUAL, lineNum, columnNum++);
				else {
					fstream.unread(c);
					return new Token(Kind.TOKEN_NOT, lineNum, columnNum++);
				}
			case 7:
				if (c == '&')
					return new Token(Kind.TOKEN_AND, lineNum, columnNum++);
				else {
					fstream.unread(c);
					return new Token(Kind.TOKEN_BITWISEAND, lineNum, columnNum++);
				}
			case 8:
				if (c == '|')
					return new Token(Kind.TOKEN_OR, lineNum, columnNum++);
				else {
					fstream.unread(c);
					return new Token(Kind.TOKEN_BITWISEOR, lineNum, columnNum++);
				}
			case 9:
				if (c == '*') {
					while ((c = fstream.read()) != -1) {
						if (c == '*') {
							c = fstream.read();
							if (c == '/')
								return nextTokenInternal();
							else
								fstream.unread(c);
						} else if (c == '\n') {
							lineNum++;
							columnNum = 1;
						}
					}
					error();
				} else if (c == '/') {
					while (c != '\n' && c != -1)
						c = fstream.read();
					lineNum++;
					columnNum = 1;
					return nextTokenInternal();
				} else
					return new Token(Kind.TOKEN_DIV, lineNum, columnNum++);
			case 10:
				while (c != '"' && c != -1 && c != '\n') {
					buf.append((char) c);
					c = fstream.read();
				}
				if (c == '"')
					return new Token(Kind.TOKEN_STRINGVALUE, lineNum, columnNum++, buf.toString());
				else
					error();
			case 11:
				buf.append((char) c);
				if (c == '\\') {
					c = fstream.read();
					if (c == 'b' || c == 't' || c == 'n' || c == 'f' || c == 'r' || c == '"' || c == '\'' || c == '\\')
						buf.append((char) c);
					else
						error();
				}
				c = fstream.read();
				if (c == '\'')
					return new Token(Kind.TOKEN_CHARVALUE, lineNum, columnNum++, buf.toString());
				else
					error();
			default:
				error();
			}
			c = fstream.read();
		}
		return new Token(Kind.TOKEN_EOF, lineNum, columnNum++);
	}

	public Token nextToken() {
		Token t = null;

		try {
			t = this.nextTokenInternal();
		} catch (Exception e) {
			System.err.println(e);
			t = this.nextToken();
		}
		if (control.Control.lex)
			System.out.println(t.toString());
		return t;
	}
}