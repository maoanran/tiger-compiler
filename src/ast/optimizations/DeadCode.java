package ast.optimizations;

// Dead code elimination optimizations on an AST.

public class DeadCode implements ast.Visitor {
	private ast.classs.T newClass;
	private ast.mainClass.T mainClass;
	public ast.program.T program;
	private ast.method.T methodd;
	private ast.stm.T stmm;
	private ast.dec.T locall;

	public DeadCode() {
		this.newClass = null;
		this.mainClass = null;
		this.program = null;
		this.methodd = null;
		this.stmm = null;
	}

	// //////////////////////////////////////////////////////
	//
	public String genId() {
		return util.Temp.next();
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(ast.exp.Add e) {
		return;
	}

	@Override
	public void visit(ast.exp.And e) {
		return;
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		return;
	}

	@Override
	public void visit(ast.exp.Call e) {
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
		return;
	}

	@Override
	public void visit(ast.exp.Id e) {
		return;
	}

	@Override
	public void visit(ast.exp.Length e) {
		return;
	}

	@Override
	public void visit(ast.exp.Lt e) {
		return;
	}

	@Override
	public void visit(ast.exp.Gt e) {
		return;
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		return;
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
		return;
	}

	@Override
	public void visit(ast.exp.Num e) {
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		return;
	}

	@Override
	public void visit(ast.exp.Times e) {
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
		return;
	}

	@Override
	public void visit(ast.exp.Block s) {
		return;
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		this.stmm = s;
		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		this.stmm = s;
		return;
	}

	@Override
	public void visit(ast.stm.Block s) {
		java.util.LinkedList<ast.stm.T> newStms = new java.util.LinkedList<ast.stm.T>();
		for (ast.stm.T t : s.stms) {
			t.accept(this);
			if (this.stmm != null)
				newStms.add(this.stmm);
		}
		this.stmm = new ast.stm.Block(newStms);
	}

	@Override
	public void visit(ast.stm.If s) {

		if (s.condition.toString().equals("@True")) {
			this.stmm = s.thenn;
			ast.optimizations.Main.modified();
		} else if (s.condition.toString().equals("@False")) {
			this.stmm = s.elsee;
			ast.optimizations.Main.modified();
		} else
			this.stmm = s;
		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		this.stmm = s;
		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		if (s.condition.toString().equals("@False")) {
			this.stmm = null;
			ast.optimizations.Main.modified();
		} else
			this.stmm = s;
		return;
	}

	@Override
	public void visit(ast.type.Id t) {
		return;
	}

	// type
	@Override
	public void visit(ast.type.Boolean t) {
	}

	@Override
	public void visit(ast.type.Class t) {
	}

	@Override
	public void visit(ast.type.Int t) {
	}

	@Override
	public void visit(ast.type.IntArray t) {
	}

	// dec
	@Override
	public void visit(ast.dec.Dec d) {
		if (d.isUsed)
			this.locall = d;
		else {
			ast.optimizations.Main.modified();
			this.locall = null;
		}
		return;
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		java.util.LinkedList<ast.dec.T> newLocals = new java.util.LinkedList<ast.dec.T>();
		java.util.LinkedList<ast.stm.T> newStms = new java.util.LinkedList<ast.stm.T>();

		for (ast.dec.T local : m.locals) {
			local.accept(this);
			if (this.locall != null)
				newLocals.add(this.locall);
		}

		for (ast.stm.T s : m.stms) {
			s.accept(this);
			if (this.stmm != null)
				newStms.add(this.stmm);
		}

		m.retExp.accept(this);

		this.methodd = new ast.method.Method(m.retType, m.id, m.formals, newLocals, newStms, m.retExp);

		return;
	}

	// class
	@Override
	public void visit(ast.classs.Class c) {

		java.util.LinkedList<ast.method.T> newMethods = new java.util.LinkedList<ast.method.T>();
		for (ast.method.T m : c.methods) {
			m.accept(this);
			newMethods.add(this.methodd);
		}
		this.newClass = new ast.classs.Class(c.id, c.extendss, c.decs, newMethods, c.lineNum);
		return;
	}

	// main class
	@Override
	public void visit(ast.mainClass.MainClass c) {
		java.util.LinkedList<ast.stm.T> newStms = new java.util.LinkedList<ast.stm.T>();
		for (ast.stm.T s : c.stms) {
			s.accept(this);
			newStms.add(this.stmm);
			this.stmm = null;
		}
		this.mainClass = new ast.mainClass.MainClass(c.id, c.arg, newStms);
		return;
	}

	// program
	@Override
	public void visit(ast.program.Program p) {

		// You should comment out this line of code:
		// this.program = p;

		p.mainClass.accept(this);

		java.util.LinkedList<ast.classs.T> newClasses = new java.util.LinkedList<ast.classs.T>();
		for (ast.classs.T c : p.classes) {
			c.accept(this);
			newClasses.add(this.newClass);
		}

		this.program = new ast.program.Program(this.mainClass, newClasses);

		if (control.Control.isTracing("ast.DeadCode")) {
			System.out.println("before optimization:");
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			p.accept(pp);
			System.out.println("after optimization:");
			this.program.accept(pp);
		}

		return;
	}

}
