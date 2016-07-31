package codegen.JavaScript;

import ast.exp.Block;

// Given a Java ast, translate it into a JavaScript ast and outputs it.

public class TranslateVisitor implements ast.Visitor {
	private ClassTable table;
	private String classId;
	private codegen.JavaScript.type.T type; // type after translation
	private codegen.JavaScript.dec.T dec;
	private codegen.JavaScript.stm.T stm;
	private codegen.JavaScript.exp.T exp;
	private codegen.JavaScript.method.T method;
	private java.util.LinkedList<codegen.JavaScript.classs.T> classes;
	private java.util.LinkedList<codegen.JavaScript.method.T> methods;
	private codegen.JavaScript.mainMethod.T mainMethod;
	public codegen.JavaScript.program.T program;

	public TranslateVisitor() {
		this.table = new ClassTable();
		this.classId = null;
		this.type = null;
		this.dec = null;
		this.stm = null;
		this.exp = null;
		this.method = null;
		this.classes = new java.util.LinkedList<codegen.JavaScript.classs.T>();
		this.methods = new java.util.LinkedList<codegen.JavaScript.method.T>();
		this.mainMethod = null;
		this.program = null;
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(ast.exp.Add e) {
		e.left.accept(this);
		codegen.JavaScript.exp.T t = this.exp;
		e.right.accept(this);
		this.exp = new codegen.JavaScript.exp.Add(t, this.exp);
		return;
	}

	@Override
	public void visit(ast.exp.And e) {
		e.left.accept(this);
		codegen.JavaScript.exp.T t = this.exp;
		e.right.accept(this);
		this.exp = new codegen.JavaScript.exp.And(t, this.exp);
		return;
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		e.array.accept(this);
		codegen.JavaScript.exp.T t = this.exp;
		e.index.accept(this);
		this.exp = new codegen.JavaScript.exp.ArraySelect(t, this.exp);
		return;
	}

	@Override
	public void visit(ast.exp.Call e) {
		e.exp.accept(this);
		codegen.JavaScript.exp.T exp = this.exp;
		java.util.LinkedList<codegen.JavaScript.exp.T> args = new java.util.LinkedList<codegen.JavaScript.exp.T>();
		for (ast.exp.T x : e.args) {
			x.accept(this);
			args.add(this.exp);
		}

		this.exp = new codegen.JavaScript.exp.Call(exp, e.id, args);
	}

	@Override
	public void visit(ast.exp.Id e) {
		e.type.accept(this);
		this.exp = new codegen.JavaScript.exp.Id(e.id, e.isField, e.isLocal, this.type);
	}

	@Override
	public void visit(ast.exp.Length e) {
		e.array.accept(this);
		this.exp = new codegen.JavaScript.exp.Length(this.exp);
	}

	@Override
	public void visit(ast.exp.Lt e) {
		e.left.accept(this);
		codegen.JavaScript.exp.T left = this.exp;
		e.right.accept(this);
		codegen.JavaScript.exp.T right = this.exp;
		this.exp = new codegen.JavaScript.exp.Lt(left, right);

	}

	@Override
	public void visit(ast.exp.Gt e) {
		e.left.accept(this);
		codegen.JavaScript.exp.T left = this.exp;
		e.right.accept(this);
		codegen.JavaScript.exp.T right = this.exp;
		this.exp = new codegen.JavaScript.exp.Gt(left, right);

	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		e.exp.accept(this);
		codegen.JavaScript.exp.NewIntArray a = new codegen.JavaScript.exp.NewIntArray(this.exp);
		this.exp = a;
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		codegen.JavaScript.exp.NewObject a = new codegen.JavaScript.exp.NewObject(e.id);
		this.exp = a;
	}

	@Override
	public void visit(ast.exp.Not e) {
		e.exp.accept(this);
		this.exp = new codegen.JavaScript.exp.Not(this.exp);
	}

	@Override
	public void visit(ast.exp.Num e) {
		this.exp = new codegen.JavaScript.exp.Num(e.num);
	}

	@Override
	public void visit(ast.exp.Sub e) {
		e.left.accept(this);
		codegen.JavaScript.exp.T left = this.exp;
		e.right.accept(this);
		codegen.JavaScript.exp.T right = this.exp;
		this.exp = new codegen.JavaScript.exp.Sub(left, right);
	}

	@Override
	public void visit(ast.exp.This e) {
		this.exp = new codegen.JavaScript.exp.This();
	}

	@Override
	public void visit(ast.exp.Times e) {
		e.left.accept(this);
		codegen.JavaScript.exp.T left = this.exp;
		e.right.accept(this);
		codegen.JavaScript.exp.T right = this.exp;
		this.exp = new codegen.JavaScript.exp.Times(left, right);
	}

	@Override
	public void visit(ast.exp.True e) {
		this.exp = new codegen.JavaScript.exp.True();
	}

	@Override
	public void visit(ast.exp.False e) {
		this.exp = new codegen.JavaScript.exp.False();
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		s.exp.accept(this);
		s.id.type.accept(this);
		this.stm = new codegen.JavaScript.stm.Assign(new codegen.JavaScript.exp.Id(s.id.id, s.id.isField, s.id.isLocal, this.type),
				this.exp);
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		codegen.JavaScript.exp.T index;
		codegen.JavaScript.exp.T exp;
		s.index.accept(this);
		index = this.exp;
		s.exp.accept(this);
		exp = this.exp;
		s.id.accept(this);
		this.stm = new codegen.JavaScript.stm.AssignArray(new codegen.JavaScript.exp.Id(s.id.id, s.id.isField, s.id.isLocal, this.type),
				index, exp);
	}

	@Override
	public void visit(ast.stm.Block s) {

		java.util.LinkedList<codegen.JavaScript.stm.T> list = new java.util.LinkedList<codegen.JavaScript.stm.T>();
		for (ast.stm.T stm : s.stms) {
			stm.accept(this);
			list.add(this.stm);
		}
		this.stm = new codegen.JavaScript.stm.Block(list);
	}

	@Override
	public void visit(ast.stm.If s) {
		s.condition.accept(this);
		codegen.JavaScript.exp.T condition = this.exp;
		s.thenn.accept(this);
		codegen.JavaScript.stm.T thenn = this.stm;
		s.elsee.accept(this);
		codegen.JavaScript.stm.T elsee = this.stm;
		this.stm = new codegen.JavaScript.stm.If(condition, thenn, elsee);
	}

	@Override
	public void visit(ast.stm.Print s) {
		s.exp.accept(this);
		this.stm = new codegen.JavaScript.stm.Write(this.exp);
	}

	@Override
	public void visit(ast.stm.While s) {
		s.condition.accept(this);
		codegen.JavaScript.exp.T condition = this.exp;
		s.body.accept(this);
		codegen.JavaScript.stm.T body = this.stm;
		this.stm = new codegen.JavaScript.stm.While(condition, body);
	}

	@Override
	public void visit(ast.type.Id t) {
		this.type = new codegen.JavaScript.type.Var();
	}

	@Override
	public void visit(ast.type.Boolean t) {
		this.type = new codegen.JavaScript.type.Var();
	}

	@Override
	public void visit(ast.type.Class t) {
		this.type = new codegen.JavaScript.type.Var();
	}

	@Override
	public void visit(ast.type.Int t) {
		this.type = new codegen.JavaScript.type.Var();
	}

	@Override
	public void visit(ast.type.IntArray t) {
		this.type = new codegen.JavaScript.type.Var();
	}

	// dec
	@Override
	public void visit(ast.dec.Dec d) {
		d.type.accept(this);
		this.dec = new codegen.JavaScript.dec.Dec(this.type, d.id);
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		m.retType.accept(this);
		codegen.JavaScript.type.T newRetType = this.type;

		java.util.LinkedList<codegen.JavaScript.dec.T> newFormals = new java.util.LinkedList<codegen.JavaScript.dec.T>();
		for (ast.dec.T d : m.formals) {
			d.accept(this);
			newFormals.add(this.dec);
		}

		java.util.LinkedList<codegen.JavaScript.dec.T> locals = new java.util.LinkedList<codegen.JavaScript.dec.T>();
		for (ast.dec.T d : m.locals) {
			d.accept(this);
			locals.add(this.dec);
		}

		java.util.LinkedList<codegen.JavaScript.stm.T> newStm = new java.util.LinkedList<codegen.JavaScript.stm.T>();
		for (ast.stm.T s : m.stms) {
			s.accept(this);
			newStm.add(this.stm);
		}

		m.retExp.accept(this);
		codegen.JavaScript.exp.T retExp = this.exp;
		this.method = new codegen.JavaScript.method.Method(newRetType, this.classId, m.id, newFormals, locals, newStm, retExp);

	}

	// class
	@Override
	public void visit(ast.classs.Class c) {
		ClassBinding cb = this.table.get(c.id);

		this.classId = c.id;

		// ast.classs.Class c2 = c;
		//
		// while (c2.extendss != null) {
		// for (ast.classs.T ct : this.p.classes) {
		// ast.classs.Class ct2 = (ast.classs.Class) ct;
		// if (ct2.id.equals(c2.extendss)) {
		// addMethod(ct2);
		// c2 = ct2;
		// }
		// }
		// }
		addMethod(c);

		this.classes.add(new codegen.JavaScript.classs.Class(c.id, c.extendss, cb.fields, this.methods));
		// this.vtables.add(new codegen.JavaScript.vtable.Vtable(c.id,
		// cb.methods));
	}

	private void addMethod(ast.classs.Class c) {

		for (ast.method.T m : c.methods) {
			m.accept(this);
			codegen.JavaScript.method.Method method0 = ((codegen.JavaScript.method.Method) this.method);
			for (codegen.JavaScript.method.T t : this.methods) {
				codegen.JavaScript.method.Method method = (codegen.JavaScript.method.Method) t;
				if (method0.id.equals(method.id) && method0.classId.equals(method.classId))
					this.methods.remove(t);
			}
			this.methods.add(this.method);
		}
	}

	// main class
	@Override
	public void visit(ast.mainClass.MainClass c) {
		// ClassBinding cb = this.table.get(c.id);
		// this.vtables.add(new codegen.JavaScript.vtable.Vtable(c.id,
		// cb.methods));

		for (ast.stm.T s : c.stms)
			s.accept(this);

		this.mainMethod = new codegen.JavaScript.mainMethod.MainMethod(this.stm);
	}

	// /////////////////////////////////////////////////////
	// the first pass
	public void scanMain(ast.mainClass.T m) {
		this.table.init(((ast.mainClass.MainClass) m).id, null);
		// this is a special hacking in that we don't want to
		// enter "main" into the table.
	}

	public void scanClasses(java.util.LinkedList<ast.classs.T> cs) {
		// put empty chuncks into the table
		for (ast.classs.T c : cs) {
			ast.classs.Class cc = (ast.classs.Class) c;
			this.table.init(cc.id, cc.extendss);
		}

		// put class fields and methods into the table
		for (ast.classs.T c : cs) {
			ast.classs.Class cc = (ast.classs.Class) c;
			java.util.LinkedList<codegen.JavaScript.dec.T> newDecs = new java.util.LinkedList<codegen.JavaScript.dec.T>();
			for (ast.dec.T dec : cc.decs) {
				dec.accept(this);
				newDecs.add(this.dec);
			}

			this.table.initDecs(cc.id, newDecs);

			// all methods
			java.util.LinkedList<ast.method.T> methods = cc.methods;
			for (ast.method.T mthd : methods) {
				ast.method.Method m = (ast.method.Method) mthd;
				java.util.LinkedList<codegen.JavaScript.dec.T> newArgs = new java.util.LinkedList<codegen.JavaScript.dec.T>();
				for (ast.dec.T arg : m.formals) {
					arg.accept(this);
					newArgs.add(this.dec);
				}
				m.retType.accept(this);
				this.table.initMethod(cc.id, this.type, newArgs, m.id);
			}
		}
	}

	public void scanProgram(ast.program.T p) {
		ast.program.Program pp = (ast.program.Program) p;
		scanMain(pp.mainClass);
		scanClasses(pp.classes);
	}

	// end of the first pass
	// ////////////////////////////////////////////////////

	// program
	@Override
	public void visit(ast.program.Program p) {
		// The first pass is to scan the whole program "p", and
		// to collect all information of inheritance.
		scanProgram(p);
		// do translations
		p.mainClass.accept(this);
		for (ast.classs.T classs : p.classes) {
			this.methods = new java.util.LinkedList<codegen.JavaScript.method.T>();
			classs.accept(this);
		}

		// for (codegen.JavaScript.vtable.T v : vtables) {
		// codegen.JavaScript.vtable.Vtable vt =
		// (codegen.JavaScript.vtable.Vtable) v;
		// }

		this.program = new codegen.JavaScript.program.Program(this.classes, this.mainMethod);

	}

	@Override
	public void visit(Block s) {
		// TODO Auto-generated method stub
		
	}

}
