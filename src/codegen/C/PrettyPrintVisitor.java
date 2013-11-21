package codegen.C;

import java.util.Iterator;

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
	public void visit(codegen.C.exp.Add e) {
		e.left.accept(this);
		this.say(" + ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(codegen.C.exp.And e) {
		e.left.accept(this);
		this.say(" && ");
		e.right.accept(this);
	}

	@Override
	public void visit(codegen.C.exp.ArraySelect e) {
		e.array.accept(this);
		this.say("[");
		e.index.accept(this);
		this.say("]");
		return;
	}

	@Override
	public void visit(codegen.C.exp.Call e) {
		this.say("(" + e.assign + "=");
		this.temp = e.assign;
		e.exp.accept(this);
		this.say(", ");

		this.say(e.assign + "->vptr->" + e.id + "(" + e.assign);
		int size = e.args.size();
		if (size == 0) {
			this.say("))");
			return;
		}
		for (codegen.C.exp.T x : e.args) {
			this.say(", ");
			x.accept(this);
		}
		this.say("))");
		return;
	}

	@Override
	public void visit(codegen.C.exp.Id e) {
		if (e.isField)
			this.say("this->");
		this.say(e.id);
	}

	@Override
	public void visit(codegen.C.exp.Length e) {
		this.say("sizeof(");
		e.array.accept(this);
		this.say(") / sizeof(int)");
	}

	@Override
	public void visit(codegen.C.exp.Lt e) {
		e.left.accept(this);
		this.say(" < ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(codegen.C.exp.Gt e) {
		e.left.accept(this);
		this.say(" > ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(codegen.C.exp.NewIntArray e) {
		this.say("(int *)malloc(sizeof(int) * ");
		e.exp.accept(this);
		this.sayln(");");
	}

	// @Override
	// public void visit(codegen.C.exp.NewObject e) {
	//
	// this.say("((struct " + e.id + "*)malloc(sizeof(struct " + e.id + "))), "
	// + this.temp + "->vptr = &" + e.id + "_vtable_");
	// return;
	// }

	@Override
	public void visit(codegen.C.exp.NewObject e) {
		this.say("((struct " + e.id + "*)(Tiger_new (&" + e.id + "_vtable_, sizeof(struct " + e.id + "))))");
		return;
	}

	@Override
	public void visit(codegen.C.exp.Not e) {
		this.say("!(");
		e.exp.accept(this);
		this.say(")");
		return;
	}

	@Override
	public void visit(codegen.C.exp.Num e) {
		this.say(Integer.toString(e.num));
		return;
	}

	@Override
	public void visit(codegen.C.exp.Sub e) {
		e.left.accept(this);
		this.say(" - ");
		e.right.accept(this);
		return;
	}

	@Override
	public void visit(codegen.C.exp.This e) {
		this.say("this");
	}

	@Override
	public void visit(codegen.C.exp.Times e) {
		e.left.accept(this);
		this.say(" * ");
		e.right.accept(this);
		return;
	}

	// statements
	@Override
	public void visit(codegen.C.stm.Assign s) {
		this.printSpaces();
		this.temp = s.id.id;
		if (s.id.isField)
			this.say("this->");
		this.say(s.id.id + " = ");
		s.exp.accept(this);

		if (s.exp.getClass().toString().equals("class codegen.C.exp.NewIntArray")) {
			this.printSpaces();
			this.say("memset(");
			if (s.id.isField)
				this.say("this->");
			this.say(s.id.id + ", 0, sizeof(int) * sz)");
		}

		this.sayln(";");
		return;
	}

	@Override
	public void visit(codegen.C.stm.AssignArray s) {
		this.printSpaces();
		this.temp = s.id.id;
		if (s.id.isField)
			this.say("this->");
		this.say(s.id.id + "[");
		s.index.accept(this);
		this.say("] = ");
		s.exp.accept(this);
		this.sayln(";");
		return;
	}

	@Override
	public void visit(codegen.C.stm.Block s) {
		this.printSpaces();
		this.sayln("{");
		this.indent();
		for (codegen.C.stm.T stm : s.stms)
			stm.accept(this);
		this.unIndent();
		this.printSpaces();
		this.sayln("}");
	}

	@Override
	public void visit(codegen.C.stm.If s) {
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

	// @Override
	// public void visit(codegen.C.stm.Print s) {
	// this.printSpaces();
	// this.say("printf(\"%d\\n\", (");
	// s.exp.accept(this);
	// this.sayln("));");
	// return;
	// }
	@Override
	public void visit(codegen.C.stm.Print s) {
		this.printSpaces();
		this.say("System_out_println (");
		s.exp.accept(this);
		this.sayln(");");
		return;
	}

	@Override
	public void visit(codegen.C.stm.While s) {
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
	public void visit(codegen.C.type.Class t) {
		this.say("struct " + t.id + " *");
	}

	@Override
	public void visit(codegen.C.type.Int t) {
		this.say("int");
	}

	@Override
	public void visit(codegen.C.type.IntArray t) {
		this.say("int *");
	}

	public void visit(codegen.C.type.Id t) {
		this.say(t.id);
	};

	// dec
	@Override
	public void visit(codegen.C.dec.Dec d) {
		d.type.accept(this);
		this.say(" ");
		this.say(d.id);
	}

	// method
	@Override
	public void visit(codegen.C.method.Method m) {
		m.retType.accept(this);
		this.say(" " + m.classId + "_" + m.id + "(");
		int size = m.formals.size();
		for (codegen.C.dec.T d : m.formals) {
			codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
			size--;
			dec.type.accept(this);
			this.say(" " + dec.id);
			if (size > 0)
				this.say(", ");
		}
		this.sayln(")");
		this.sayln("{");

		for (codegen.C.dec.T d : m.locals) {
			codegen.C.dec.Dec dec = (codegen.C.dec.Dec) d;
			this.printSpaces();
			dec.type.accept(this);
			this.sayln(" " + dec.id + ";");
		}
		this.sayln("");
		for (codegen.C.stm.T s : m.stms)
			s.accept(this);
		this.printSpaces();
		this.say("return ");
		m.retExp.accept(this);
		this.sayln(";");
		this.sayln("}");
		return;
	}

	@Override
	public void visit(codegen.C.mainMethod.MainMethod m) {
		// this.sayln("int main ()");
		this.sayln("int Tiger_main ()");
		this.sayln("{");
		for (codegen.C.dec.T dec : m.locals) {
			this.printSpaces();
			codegen.C.dec.Dec d = (codegen.C.dec.Dec) dec;
			d.type.accept(this);

			this.sayln(" " + d.id + ";");
		}
		m.stm.accept(this);
		this.sayln("}\n");
		return;
	}

	// vtables
	@Override
	public void visit(codegen.C.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable");
		this.sayln("{");

		for (Iterator<String> itt = v.ms.keySet().iterator(); itt.hasNext();) {
			this.printSpaces();
			codegen.C.Ftuple t = v.ms.get(itt.next());
			t.ret.accept(this);
			this.sayln(" (*" + v.id + "_" + t.id + ")();");
		}
		this.sayln("};\n");
		return;
	}

	private void outputVtable(codegen.C.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable " + v.id + "_vtable_ = ");
		this.sayln("{");

		for (Iterator<String> itt = v.ms.keySet().iterator(); itt.hasNext();) {
			this.printSpaces();
			codegen.C.Ftuple t = v.ms.get(itt.next());
			this.sayln(t.classs + "_" + t.id + ",");
		}

		this.sayln("};\n");
		return;
	}

	// class
	@Override
	public void visit(codegen.C.classs.Class c) {
		this.sayln("struct " + c.id);
		this.sayln("{");
		this.printSpaces();
		this.sayln("struct " + c.id + "_vtable * vptr;");

		for (Iterator<String> itt = c.decs.keySet().iterator(); itt.hasNext();) {
			this.printSpaces();
			codegen.C.Tuple t = c.decs.get(itt.next());
			t.type.accept(this);
			this.say(" ");
			this.sayln(t.id + ";");
		}

		this.sayln("};");
		return;
	}

	// program
	@Override
	public void visit(codegen.C.program.Program p) {
		// we'd like to output to a file, rather than the "stdout".
		try {
			String outputName = null;
			if (Control.outputName != null)
				outputName = Control.outputName;
			else if (Control.fileName != null)
				outputName = Control.fileName + ".c";
			else
				outputName = "a.c";

			this.writer = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(outputName)));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.sayln("// This is automatically generated by the Tiger compiler.");
		this.sayln("// Do NOT modify!\n");

		this.sayln("// structures");
		for (codegen.C.classs.T c : p.classes) {
			c.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables structures");
		for (codegen.C.vtable.T v : p.vtables) {
			v.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables defines");
		for (codegen.C.vtable.T v : p.vtables) {
			codegen.C.vtable.Vtable vv = (codegen.C.vtable.Vtable) v;
			this.sayln("struct " + vv.id + "_vtable " + vv.id + "_vtable_ ;");
		}
		this.sayln("");

		this.sayln("// methods");
		for (codegen.C.method.T m : p.methods) {
			m.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables");
		for (codegen.C.vtable.T v : p.vtables) {
			outputVtable((codegen.C.vtable.Vtable) v);
		}
		this.sayln("");

		this.sayln("// main method");
		p.mainMethod.accept(this);
		this.sayln("");

		this.say("\n\n");

		try {
			this.writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
