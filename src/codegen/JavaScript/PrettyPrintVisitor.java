package codegen.JavaScript;

import control.Control;

public class PrettyPrintVisitor implements Visitor {
	private int indentLevel;
	@SuppressWarnings("unused")
	private String temp;
	private java.io.BufferedWriter writer;

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
		say(s);
		try {
			this.writer.write("\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void say(String s) {
		try {
			this.writer.write(s);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(codegen.JavaScript.exp.Add e) {
		e.left.accept(this);
		this.say(" + ");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.JavaScript.exp.And e) {
		e.left.accept(this);
		this.say(" && ");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.JavaScript.exp.ArraySelect e) {
		e.array.accept(this);
		this.say("[");
		e.index.accept(this);
		this.say("]");
	}

	@Override
	public void visit(codegen.JavaScript.exp.Call e) {
		e.exp.accept(this);
		this.say("." + e.id + "(");
		for (codegen.JavaScript.exp.T x : e.args) {
			x.accept(this);
			if (e.args.getLast() != x)
				this.say(", ");
		}
		this.say(")");
		return;
	}

	@Override
	public void visit(codegen.JavaScript.exp.Id e) {
		if (e.isField)
			this.say("this.");
		this.say(e.id);
	}

	@Override
	public void visit(codegen.JavaScript.exp.Length e) {
		e.array.accept(this);
		this.say(".length");
	}

	@Override
	public void visit(codegen.JavaScript.exp.Lt e) {
		e.left.accept(this);
		this.say(" < ");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.JavaScript.exp.False e) {
		this.say("false");
	}

	@Override
	public void visit(codegen.JavaScript.exp.Gt e) {
		e.left.accept(this);
		this.say(" > ");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.JavaScript.exp.NewIntArray e) {
		this.say("new Array(");
		e.exp.accept(this);
		this.say(")");
	}

	@Override
	public void visit(codegen.JavaScript.exp.NewObject e) {
		this.say("new " + e.id + "()");
	}

	@Override
	public void visit(codegen.JavaScript.exp.Not e) {
		this.say("!(");
		e.exp.accept(this);
		this.say(")");
	}

	@Override
	public void visit(codegen.JavaScript.exp.Num e) {
		this.say(Integer.toString(e.num));
	}

	@Override
	public void visit(codegen.JavaScript.exp.Sub e) {
		e.left.accept(this);
		this.say(" - ");
		e.right.accept(this);

	}

	@Override
	public void visit(codegen.JavaScript.exp.This e) {
		this.say("this");
	}

	@Override
	public void visit(codegen.JavaScript.exp.Times e) {
		e.left.accept(this);
		this.say(" * ");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.JavaScript.exp.True e) {
		this.say("true");
	}

	// statements
	@Override
	public void visit(codegen.JavaScript.stm.Assign s) {
		this.printSpaces();
		this.temp = s.id.id;
		if (s.id.isField)
			this.say("this.");
		this.say(s.id.id + " = ");
		s.exp.accept(this);
		this.sayln(";");
	}

	@Override
	public void visit(codegen.JavaScript.stm.AssignArray s) {
		this.printSpaces();
		this.temp = s.id.id;
		if (s.id.isField)
			this.say("this.");
		this.say(s.id.id + "[");
		s.index.accept(this);
		this.say("] = ");
		s.exp.accept(this);
		this.sayln(";");
	}

	@Override
	public void visit(codegen.JavaScript.stm.Block s) {
		this.printSpaces();
		this.sayln("{");
		this.indent();
		for (codegen.JavaScript.stm.T stm : s.stms)
			stm.accept(this);
		this.unIndent();
		this.printSpaces();
		this.sayln("}");
	}

	@Override
	public void visit(codegen.JavaScript.stm.If s) {
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
	}

	@Override
	public void visit(codegen.JavaScript.stm.Write s) {
		this.printSpaces();
		this.say("document.write(");
		s.exp.accept(this);
		this.sayln(");");
		this.printSpaces();
		this.sayln("document.write('<br/>');");
	}

	@Override
	public void visit(codegen.JavaScript.stm.While s) {
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

	}

	public void visit(codegen.JavaScript.type.Var t) {
		this.say("var");
	}

	// dec
	@Override
	public void visit(codegen.JavaScript.dec.Dec d) {
		d.type.accept(this);
		this.say(" ");
		this.say(d.id);
	}

	// method
	@Override
	public void visit(codegen.JavaScript.method.Method m) {

		this.say(m.id + " = function(");

		int size = m.formals.size();
		for (codegen.JavaScript.dec.T d : m.formals) {
			codegen.JavaScript.dec.Dec dec = (codegen.JavaScript.dec.Dec) d;
			size--;
			this.say(dec.id);
			if (size > 0)
				this.say(", ");
		}
		this.sayln(")");
		this.printSpaces();
		this.sayln("{");

		this.indent();
		for (codegen.JavaScript.dec.T d : m.locals) {
			codegen.JavaScript.dec.Dec dec = (codegen.JavaScript.dec.Dec) d;
			this.printSpaces();
			dec.accept(this);
			this.sayln(";");
		}

		for (codegen.JavaScript.stm.T s : m.stms)
			s.accept(this);

		this.printSpaces();
		this.say("return ");
		m.retExp.accept(this);
		this.sayln(";");
		this.unIndent();
		this.printSpaces();
		this.sayln("}");
		this.sayln("");
	}

	@Override
	public void visit(codegen.JavaScript.mainMethod.MainMethod m) {
		m.stm.accept(this);
	}

	// vtables
	@Override
	public void visit(codegen.JavaScript.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable");
		this.sayln("{");

		this.printSpaces();
		this.sayln("char * " + v.id + "_gc_map;");
		for (java.util.Iterator<String> itt = v.ms.keySet().iterator(); itt.hasNext();) {
			this.printSpaces();
			codegen.JavaScript.Ftuple t = v.ms.get(itt.next());
			t.ret.accept(this);
			this.sayln(" (*" + v.id + "_" + t.id + ")();");
		}
		this.sayln("};\n");

	}

	// class
	@Override
	public void visit(codegen.JavaScript.classs.Class c) {

		this.printSpaces();
		this.sayln("function " + c.id + "()");
		this.printSpaces();
		this.sayln("{");
		this.indent();
		for (java.util.Iterator<String> itt = c.decs.keySet().iterator(); itt.hasNext();) {
			this.printSpaces();
			codegen.JavaScript.Tuple t = c.decs.get(itt.next());

			this.sayln("this." + t.id + " = 0;");
		}

		this.unIndent();
		this.printSpaces();
		this.sayln("}");
		this.sayln("");
		
		if (c.extendss != null) {
			this.printSpaces();
			this.sayln(c.id + ".prototype = new " + c.extendss + "();");
			this.sayln("");
		}

		for (codegen.JavaScript.method.T m : c.methods) {
			this.printSpaces();
			this.say(c.id + ".prototype.");
			m.accept(this);
		}
	}

	// program
	@Override
	public void visit(codegen.JavaScript.program.Program p) {

		// we'd like to output to a file, rather than the "stdout".
		try {
			String outputName = null;
			if (Control.outputName != null)
				outputName = Control.outputName;
			else if (Control.fileName != null)
				outputName = Control.fileName + ".html";
			else
				outputName = "a.html";

			this.writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(outputName)));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.sayln("<!doctype html>");
		this.sayln("<html>");
		this.printSpaces();
		this.sayln("<head>");
		this.indent();
		this.printSpaces();
		this.sayln("<meta http-equiv=Content-Type content=\"text/html;charset=utf-8\">");
		this.printSpaces();
		this.sayln("<script type=\"text/javascript\">");

		for (codegen.JavaScript.classs.T classs : p.classes) {
			classs.accept(this);
		}

		this.sayln("");
		p.mainMethod.accept(this);
		this.sayln("");
		this.printSpaces();
		this.sayln("document.close();");
		this.sayln("");
		this.printSpaces();
		this.sayln("</script>");
		this.unIndent();
		this.printSpaces();
		this.sayln("</head>");
		this.sayln("</html>");
		try {
			this.writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
