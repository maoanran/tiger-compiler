package ast;


public class PrettyPrintVisitor implements Visitor {
	private int indentLevel;

	public PrettyPrintVisitor() {
		this.indentLevel = 1;
	}

	private void indent() {
		this.indentLevel += 1;
	}

	private void unIndent() {
		this.indentLevel -= 1;
	}

	private void printSpaces() {
		int i = this.indentLevel;
		while (i-- != 0)
			this.say("\t");
	}

	private void sayln(String s) {
		System.out.println(s);
	}

	private void say(String s) {
		System.out.print(s);
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(ast.exp.Add e) {
		// Lab2, exercise4: filling in missing code.
		// Similar for other methods with empty bodies.
		// Your code here:
		e.left.accept(this);
		this.say(" + ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(ast.exp.And e) {
		e.left.accept(this);
		this.say(" && ");
		e.right.accept(this);
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		e.array.accept(this);
		this.say("[");
		e.index.accept(this);
		this.say("]");
		return;
	}

	@Override
	public void visit(ast.exp.Call e) {
		e.exp.accept(this);
		this.say("." + e.id + "(");
		for (ast.exp.T x : e.args) {
			x.accept(this);
			if (e.args.getLast() != x)
				this.say(", ");
		}
		this.say(")");
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
		this.say("false");
	}

	@Override
	public void visit(ast.exp.Id e) {
		this.say(e.id);
	}

	@Override
	public void visit(ast.exp.Length e) {
		e.array.accept(this);
		this.say(".length");
	}

	@Override
	public void visit(ast.exp.Lt e) {
		e.left.accept(this);
		this.say(" < ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(ast.exp.Gt e) {
		e.left.accept(this);
		this.say(" > ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		this.say("new int[");
		e.exp.accept(this);
		this.say("]");
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		this.say("new " + e.id + "()");
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
		this.say("!");
		e.exp.accept(this);
		this.say("");
		return;
	}

	@Override
	public void visit(ast.exp.Num e) {
		this.say(Integer.toString(e.num));
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		e.left.accept(this);
		this.say(" - ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		this.say("this");
	}

	@Override
	public void visit(ast.exp.Times e) {
		e.left.accept(this);
		this.say(" * ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
		this.say("true");
	}

	@Override
	public void visit(ast.exp.Block s) {
		this.say("(");
		s.exp.accept(this);
		this.say(")");
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		this.printSpaces();
		this.say(s.id.id + " = ");
		s.exp.accept(this);
		this.sayln(";");
		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		this.printSpaces();
		this.say(s.id.id + "[");
		s.index.accept(this);
		this.say("] = ");
		s.exp.accept(this);
		this.sayln(";");
		return;
	}

	@Override
	public void visit(ast.stm.Block s) {
		this.printSpaces();
		this.sayln("{");
		this.indent();
		for (ast.stm.T stm : s.stms)
			stm.accept(this);
		this.unIndent();
		this.printSpaces();
		this.sayln("}");
	}

	@Override
	public void visit(ast.stm.If s) {
		this.printSpaces();
		this.say("if (");
		s.condition.accept(this);
		this.sayln(")");
		if (!s.thenn.getClass().getSimpleName().equals("Block")) {
			this.indent();
			s.thenn.accept(this);
			this.unIndent();
		} else {
			s.thenn.accept(this);
		}
		this.printSpaces();
		this.sayln("else");
		if (s.elsee.getClass().getSimpleName().equals("Block")) {
			s.elsee.accept(this);
		} else {
			this.indent();
			s.elsee.accept(this);
			this.unIndent();
		}

		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		this.printSpaces();
		this.say("System.out.println (");
		s.exp.accept(this);
		this.sayln(");");
		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		this.printSpaces();
		this.say("while (");
		s.condition.accept(this);
		this.sayln(")");
		if (s.body.getClass().getSimpleName().equals("Block")) {
			s.body.accept(this);
		} else {
			this.indent();
			s.body.accept(this);
			this.unIndent();
		}

		return;
	}

	// type
	@Override
	public void visit(ast.type.Boolean t) {
		this.say("boolean");
	}

	@Override
	public void visit(ast.type.Class t) {
		this.say(t.id);
	}

	@Override
	public void visit(ast.type.Int t) {
		this.say("int");
	}

	@Override
	public void visit(ast.type.IntArray t) {
		this.say("int[]");
	}

	@Override
	public void visit(ast.type.Id t) {
		this.say(t.id);
	}

	// dec
	@Override
	public void visit(ast.dec.Dec d) {
		d.type.accept(this);
		this.say(" ");
		this.say(d.id);
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		this.printSpaces();
		this.say("public ");
		m.retType.accept(this);
		this.say(" " + m.id + "(");
		for (ast.dec.T d : m.formals) {
			d.accept(this);
			if (m.formals.getLast() != d)
				this.say(", ");
		}
		this.sayln(")");
		this.printSpaces();
		this.sayln("{");

		this.indent();
		for (ast.dec.T d : m.locals) {
			ast.dec.Dec dec = (ast.dec.Dec) d;
			this.printSpaces();
			dec.type.accept(this);
			this.sayln(" " + dec.id + ";");
		}
		if (m.locals.size() != 0)
			this.sayln("");
		for (ast.stm.T s : m.stms) {
			s.accept(this);
		}
		this.printSpaces();
		this.say("return ");
		m.retExp.accept(this);
		this.sayln(";");
		this.unIndent();
		this.printSpaces();
		this.sayln("}");
		this.sayln("");
		return;
	}

	// class
	@Override
	public void visit(ast.classs.Class c) {
		this.say("class " + c.id);
		if (c.extendss != null)
			this.sayln(" extends " + c.extendss);
		else
			this.sayln("");

		this.sayln("{");

		for (ast.dec.T d : c.decs) {
			ast.dec.Dec dec = (ast.dec.Dec) d;
			this.printSpaces();
			dec.type.accept(this);
			this.say(" ");
			this.sayln(dec.id + ";");
		}
		for (ast.method.T mthd : c.methods)
			mthd.accept(this);
		this.sayln("}");
		this.sayln("");
		return;
	}

	// main class
	@Override
	public void visit(ast.mainClass.MainClass c) {
		this.sayln("class " + c.id);
		this.sayln("{");

		this.printSpaces();
		this.sayln("public static void main (String [] " + c.arg + ")");
		this.printSpaces();
		this.sayln("{");
		this.indent();
		for (ast.stm.T s : c.stms)
			s.accept(this);
		this.unIndent();
		this.printSpaces();
		this.sayln("}");
		this.sayln("}");
		return;
	}

	// program
	@Override
	public void visit(ast.program.Program p) {
		p.mainClass.accept(this);
		this.sayln("");
		for (ast.classs.T classs : p.classes) {
			classs.accept(this);
		}
		System.out.println("\n\n");
	}

}
