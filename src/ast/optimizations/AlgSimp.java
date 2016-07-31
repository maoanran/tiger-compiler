package ast.optimizations;

// Algebraic simplification optimizations on an AST.

public class AlgSimp implements ast.Visitor {
	private ast.classs.T newClass;
	private ast.mainClass.T mainClass;
	public ast.program.T program;
	private ast.method.T methodd;
	private ast.exp.T expp;
	private ast.stm.T stmm;
	public ast.exp.T retExpp;

	public AlgSimp() {
		this.newClass = null;
		this.mainClass = null;
		this.program = null;
		this.methodd = null;
		this.expp = null;
		this.stmm = null;
		this.retExpp = null;
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
		e.left.accept(this);
		ast.exp.T left = this.expp == null ? e.left : this.expp;
		this.expp = null;

		e.right.accept(this);
		ast.exp.T right = this.expp == null ? e.right : this.expp;
		this.expp = null;

		if (left.toString().startsWith("ast.exp.Num@") && right.toString().startsWith("ast.exp.Id@")) {
			if (((ast.exp.Num) e.left).num == 0) {
				this.expp = e.right;
				ast.optimizations.Main.modified();
			}
		} else if (left.toString().startsWith("ast.exp.Id@") && right.toString().startsWith("ast.exp.Num@")) {
			if (((ast.exp.Num) e.right).num == 0) {
				this.expp = e.left;
				ast.optimizations.Main.modified();
			}
		}
		return;
	}

	@Override
	public void visit(ast.exp.And e) {
		e.left.accept(this);
		ast.exp.T left = this.expp == null ? e.left : this.expp;
		this.expp = null;

		e.right.accept(this);
		ast.exp.T right = this.expp == null ? e.right : this.expp;
		this.expp = null;
		
		this.expp = new ast.exp.And(left, right);

		return;
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {

		e.array.accept(this);
		ast.exp.T array = this.expp == null ? e.array : this.expp;
		this.expp = null;

		e.index.accept(this);
		ast.exp.T index = this.expp == null ? e.index : this.expp;
		this.expp = null;

		this.expp = new ast.exp.ArraySelect(array, index, e.lineNum);
	}

	@Override
	public void visit(ast.exp.Call e) {
		e.exp.accept(this);
		ast.exp.T exp = this.expp == null ? e.exp : this.expp;
		ast.exp.Call call = new ast.exp.Call(exp, e.id, e.args, e.lineNum);
		call.rt = e.rt;
		call.at = e.at;
		call.type = e.type;
		this.expp = call;
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
		this.expp = new ast.exp.False();
		return;
	}

	@Override
	public void visit(ast.exp.Id e) {
		return;
	}

	@Override
	public void visit(ast.exp.Length e) {
		e.array.accept(this);
		ast.exp.T array = this.expp == null ? e.array : this.expp;
		this.expp = new ast.exp.Length(array);

		return;
	}

	@Override
	public void visit(ast.exp.Lt e) {
		e.left.accept(this);
		ast.exp.T left = this.expp == null ? e.left : this.expp;
		this.expp = null;

		e.right.accept(this);
		ast.exp.T right = this.expp == null ? e.right : this.expp;
		this.expp = null;

		this.expp = new ast.exp.Lt(left, right);
		return;
	}

	@Override
	public void visit(ast.exp.Gt e) {
		e.left.accept(this);
		ast.exp.T left = this.expp == null ? e.left : this.expp;
		this.expp = null;

		e.right.accept(this);
		ast.exp.T right = this.expp == null ? e.right : this.expp;
		this.expp = null;

		this.expp = new ast.exp.Lt(left, right);
		return;
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		e.exp.accept(this);
		ast.exp.T exp = this.expp == null ? e.exp : this.expp;

		this.expp = new ast.exp.NewIntArray(exp);

		return;
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
		e.exp.accept(this);
		ast.exp.T newExp = this.expp == null ? e.exp : this.expp;
		this.expp = new ast.exp.Not(newExp);
		return;
	}

	@Override
	public void visit(ast.exp.Num e) {
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		e.left.accept(this);
		ast.exp.T left = this.expp == null ? e.left : this.expp;
		this.expp = null;

		e.right.accept(this);
		ast.exp.T right = this.expp == null ? e.right : this.expp;
		this.expp = null;

		if (left.toString().startsWith("ast.exp.Id@") && right.toString().startsWith("ast.exp.Num@")) {
			if (((ast.exp.Num) e.right).num == 0) {
				this.expp = e.left;
				ast.optimizations.Main.modified();
			}
		}
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		return;
	}

	@Override
	public void visit(ast.exp.Times e) {
		e.left.accept(this);
		ast.exp.T left = this.expp == null ? e.left : this.expp;
		this.expp = null;

		e.right.accept(this);
		ast.exp.T right = this.expp == null ? e.right : this.expp;
		this.expp = null;

		if (left.toString().startsWith("ast.exp.Num@") && right.toString().startsWith("ast.exp.Id@")) {
			if (((ast.exp.Num) e.left).num == 0) {
				this.expp = new ast.exp.Num(0);
				ast.optimizations.Main.modified();
			} else if (((ast.exp.Num) e.left).num == 1) {
				this.expp = e.right;
				ast.optimizations.Main.modified();
			} else if (((ast.exp.Num) e.left).num == 2) {
				this.expp = new ast.exp.Add(right, right);
				ast.optimizations.Main.modified();
			}
		} else if (left.toString().startsWith("ast.exp.Id@") && right.toString().startsWith("ast.exp.Num@")) {
			if (((ast.exp.Num) e.right).num == 0) {
				this.expp = new ast.exp.Num(0);
				ast.optimizations.Main.modified();
			} else if (((ast.exp.Num) e.right).num == 1) {
				this.expp = e.left;
				ast.optimizations.Main.modified();
			} else if (((ast.exp.Num) e.right).num == 2) {
				this.expp = new ast.exp.Add(left, left);
				ast.optimizations.Main.modified();
			}
		}
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
		this.expp = new ast.exp.True();
		return;
	}

	@Override
	public void visit(ast.exp.Block s) {
		s.exp.accept(this);
		ast.exp.T newExp = this.expp == null ? s.exp : this.expp;
		if (!newExp.toString().startsWith("ast.exp.Num@") && !newExp.toString().startsWith("ast.exp.Id@")
				&& !newExp.toString().startsWith("@True") && !newExp.toString().startsWith("@False")) {
			this.expp = new ast.exp.Block(newExp);
		}
		return;
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		s.exp.accept(this);
		ast.exp.T newExp = this.expp == null ? s.exp : this.expp;
		this.expp = null;

		this.stmm = new ast.stm.Assign(s.id, newExp);
		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		s.index.accept(this);
		ast.exp.T newIndex = this.expp == null ? s.index : this.expp;
		this.expp = null;

		s.exp.accept(this);
		ast.exp.T newExp = this.expp == null ? s.exp : this.expp;
		this.expp = null;

		this.stmm = new ast.stm.AssignArray(s.id, newIndex, newExp);
		return;
	}

	@Override
	public void visit(ast.stm.Block s) {
		java.util.LinkedList<ast.stm.T> newStms = new java.util.LinkedList<ast.stm.T>();
		for (ast.stm.T t : s.stms) {
			t.accept(this);
			newStms.add(this.stmm);
		}
		this.stmm = new ast.stm.Block(newStms);
	}

	@Override
	public void visit(ast.stm.If s) {
		s.condition.accept(this);
		ast.exp.T newCondition = this.expp == null ? s.condition : this.expp;
		this.expp = null;

		s.thenn.accept(this);
		ast.stm.T newThenn = this.stmm == null ? s.thenn : this.stmm;
		this.stmm = null;

		s.elsee.accept(this);
		ast.stm.T elsee = this.stmm == null ? s.elsee : this.stmm;
		this.stmm = null;

		this.stmm = new ast.stm.If(newCondition, newThenn, elsee, s.lineNum);
		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		s.exp.accept(this);
		this.stmm = new ast.stm.Print(this.expp == null ? s.exp : this.expp);
		this.expp = null;

		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		s.condition.accept(this);

		ast.exp.T newCondition = this.expp == null ? s.condition : this.expp;
		this.expp = null;

		s.body.accept(this);
		ast.stm.T newBody = this.stmm == null ? s.body : this.stmm;
		this.stmm = null;

		this.stmm = new ast.stm.While(newCondition, newBody, s.lineNum);
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
		return;
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		java.util.LinkedList<ast.stm.T> newStms = new java.util.LinkedList<ast.stm.T>();
		for (ast.stm.T s : m.stms) {
			s.accept(this);
			newStms.add(this.stmm);
			this.stmm = null;
		}

		m.retExp.accept(this);

		this.methodd = new ast.method.Method(m.retType, m.id, m.formals, m.locals, newStms, this.expp == null ? m.retExp : this.expp);
		this.expp = null;

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

		if (control.Control.isTracing("ast.AlgSimp")) {
			System.out.println("before optimization:");
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			p.accept(pp);
			System.out.println("after optimization:");
			this.program.accept(pp);
		}

		return;
	}
}
