package cfg;

import java.util.HashMap;

import control.Control;

public class PrettyPrintVisitor implements Visitor {
	private java.io.BufferedWriter writer;
	private HashMap<String, String> classGcMap;

	public PrettyPrintVisitor() {
		this.classGcMap = new HashMap<String, String>();
	}

	private void printSpaces() {
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
	// operand
	@Override
	public void visit(cfg.operand.Int operand) {
		this.say(new Integer(operand.i).toString());
	}

	@Override
	public void visit(cfg.operand.Var operand) {
		this.say(operand.id);
	}

	// statements
	@Override
	public void visit(cfg.stm.Add s) {
		this.printSpaces();
		this.say(s.dst + " = ");
		s.left.accept(this);
		this.say(" + ");
		s.right.accept(this);
		this.say(";");
		return;
	}

	@Override
	public void visit(cfg.stm.And s) {
		this.printSpaces();
		this.say(s.dst + " = ");
		s.left.accept(this);
		this.say(" && ");
		s.right.accept(this);
		this.say(";");
		return;
	}

	@Override
	public void visit(cfg.stm.InvokeVirtual s) {

		this.printSpaces();
		this.say(s.dst + " = " + s.obj);
		this.say("->vptr->" + s.f + "(" + s.obj);
		for (cfg.operand.T x : s.args) {
			this.say(", ");
			x.accept(this);
		}
		this.say(");");
		return;
	}

	@Override
	public void visit(cfg.stm.Lt s) {
		this.printSpaces();
		this.say(s.dst + " = ");
		s.left.accept(this);
		this.say(" < ");
		s.right.accept(this);
		this.say(";");
		return;
	}

	@Override
	public void visit(cfg.stm.Gt s) {
		this.printSpaces();
		this.say(s.dst + " = ");
		s.left.accept(this);
		this.say(" > ");
		s.right.accept(this);
		this.say(";");
		return;
	}

	@Override
	public void visit(cfg.stm.Move s) {
		if (s.dst.equals(s.src.toString()))
			return;
		this.printSpaces();
		this.say(s.dst + " = ");
		s.src.accept(this);
		this.say(";");
		return;
	}

	@Override
	public void visit(cfg.stm.NewObject s) {
		this.printSpaces();
		this.say("frame." + s.dst + " = ((struct " + s.c + "*)(Tiger_new (&" + s.c + "_vtable_, sizeof(struct " + s.c + "))));");
		return;
	}

	@Override
	public void visit(cfg.stm.NewIntArray s) {
		this.printSpaces();
		this.say(s.dst + " = (int *)Tiger_new_array(sizeof(int) * " + s.c + ");");
		return;
	}

	@Override
	public void visit(cfg.stm.Print s) {
		this.printSpaces();
		this.say("System_out_println (");
		s.arg.accept(this);

		this.sayln(");");
		return;
	}

	@Override
	public void visit(cfg.stm.Sub s) {
		this.printSpaces();
		this.say(s.dst + " = ");
		s.left.accept(this);
		this.say(" - ");
		s.right.accept(this);
		this.say(";");
		return;
	}

	@Override
	public void visit(cfg.stm.Times s) {
		this.printSpaces();
		this.say(s.dst + " = ");
		s.left.accept(this);
		this.say(" * ");
		s.right.accept(this);
		this.say(";");

		return;
	}

	// transfer
	@Override
	public void visit(cfg.transfer.If s) {
		this.printSpaces();
		this.say("if (");
		s.operand.accept(this);
		this.say(")\n");
		this.printSpaces();
		this.say("  goto " + s.truee.toString() + ";\n");
		this.printSpaces();
		this.say("else\n");
		this.printSpaces();
		this.say("  goto " + s.falsee.toString() + ";\n");
		return;
	}

	@Override
	public void visit(cfg.transfer.Goto s) {
		this.printSpaces();
		this.say("goto " + s.label.toString() + ";\n");
		return;
	}

	@Override
	public void visit(cfg.transfer.Return s) {
		this.printSpaces();
		this.sayln("frame_prev = frame.frame_prev;");
		this.printSpaces();
		this.say("return ");
		s.operand.accept(this);
		this.say(";");
		return;
	}

	// type
	@Override
	public void visit(cfg.type.Class t) {
		this.say("struct " + t.id + " *");
	}

	@Override
	public void visit(cfg.type.Int t) {
		this.say("int");
	}

	@Override
	public void visit(cfg.type.IntArray t) {
		this.say("int *");
	}

	// dec
	@Override
	public void visit(cfg.dec.Dec d) {
		d.type.accept(this);
		this.say(" " + d.id);
		return;
	}

	// dec
	@Override
	public void visit(cfg.block.Block b) {
		this.say(b.label.toString() + ":\n");
		for (cfg.stm.T s : b.stms) {
			s.accept(this);
			this.say("\n");
		}
		b.transfer.accept(this);
		return;
	}

	// method
	@Override
	public void visit(cfg.method.Method m) {

		String arguments = "";
		String locals = "";
		String locals2 = "";
		for (cfg.dec.T d : m.formals) {
			cfg.dec.Dec dec = (cfg.dec.Dec) d;
			if (dec.type.toString().equals("@int") || dec.type.toString().equals("@int[]")) {
				arguments += "0";
			} else {
				arguments += "1";
			}
		}

		for (cfg.dec.T d : m.locals) {
			cfg.dec.Dec dec = (cfg.dec.Dec) d;
			if (!dec.type.toString().equals("@int")) {
				locals2 += "\t";
				locals += "1";

				if (dec.type.toString().equals("@int[]"))
					locals2 += "int * " + dec.id + ";\n";
				else
					locals2 += "struct " + dec.type + " * " + dec.id + ";\n";
			}
		}

		this.sayln("");
		this.sayln("struct " + m.classId + "_" + m.id + "_gc_frame{");
		this.printSpaces();
		this.sayln("void * frame_prev;");
		this.printSpaces();
		this.sayln("int * arguments_base_address;");
		this.printSpaces();
		this.sayln("char * arguments_gc_map;");
		this.printSpaces();
		this.sayln("char * locals_gc_map;");
		this.say(locals2);
		this.sayln("};");
		this.sayln("");

		m.retType.accept(this);
		this.say(" " + m.classId + "_" + m.id + "(");
		int size = m.formals.size();
		for (cfg.dec.T d : m.formals) {
			cfg.dec.Dec dec = (cfg.dec.Dec) d;
			size--;
			dec.type.accept(this);
			this.say(" " + dec.id);
			if (size > 0)
				this.say(", ");
		}
		this.sayln(")");
		this.sayln("{");

		for (cfg.dec.T d : m.locals) {
			cfg.dec.Dec dec = (cfg.dec.Dec) d;
			if (dec.type.toString().equals("@int") || dec.type.toString().equals("@int[]")) {
				this.printSpaces();
				dec.type.accept(this);
				this.say(" " + dec.id + ";\n");
			}
		}
		this.sayln("");

		this.printSpaces();
		this.sayln("char * " + m.classId + "_" + m.id + "_arguments_gc_map = \"" + arguments + "\";");
		this.printSpaces();
		this.sayln("char * " + m.classId + "_" + m.id + "_locals_gc_map = \"" + locals + "\";");
		this.printSpaces();
		this.sayln("//put the GC stack frame onto the call stack.");
		this.printSpaces();
		this.sayln("struct " + m.classId + "_" + m.id + "_gc_frame frame;");
		this.printSpaces();
		this.sayln("//push this frame onto the GC stack by setting up \"prev\".");
		this.printSpaces();
		this.sayln("frame.frame_prev = frame_prev;");
		this.printSpaces();
		this.sayln("frame_prev = &frame;");
		// this.printSpaces();
		// this.sayln("printf(\"frame_prev:%x\\n\",frame.frame_prev);");
		this.printSpaces();
		this.sayln("//setting up memory GC maps and corresponding base addresses");
		this.printSpaces();
		this.sayln("frame.arguments_gc_map = " + m.classId + "_" + m.id + "_arguments_gc_map;");
		this.printSpaces();
		this.sayln("frame.arguments_base_address = (int *)&this;");
		this.printSpaces();
		this.sayln("frame.locals_gc_map = " + m.classId + "_" + m.id + "_locals_gc_map;");
		this.sayln("");
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}
		this.sayln("");
		this.sayln("}");
		return;
	}

	@Override
	public void visit(cfg.mainMethod.MainMethod m) {

		String arguments = "";
		String locals = "";
		String locals2 = "";
		for (cfg.dec.T d : m.locals) {
			cfg.dec.Dec dec = (cfg.dec.Dec) d;
			if (!dec.type.toString().equals("@int") && !dec.type.toString().equals("@int[]")) {
				locals2 += "\t";
				locals += "1";
				locals2 += "struct " + dec.type + " * " + dec.id + ";\n";
			}
		}

		this.sayln("");
		this.sayln("struct Tiger_main_gc_frame{");
		this.printSpaces();
		this.sayln("void * frame_prev;");
		this.printSpaces();
		this.sayln("int * arguments_base_address;");
		this.printSpaces();
		this.sayln("char * arguments_gc_map;");
		this.printSpaces();
		this.sayln("char * locals_gc_map;");
		this.say(locals2);
		this.sayln("};");
		this.sayln("");

		this.sayln("int Tiger_main ()");
		this.sayln("{");
		for (cfg.dec.T d : m.locals) {
			cfg.dec.Dec dec = (cfg.dec.Dec) d;
			if (dec.type.toString().equals("@int") || dec.type.toString().equals("@int[]")) {
				this.printSpaces();
				dec.type.accept(this);
				this.say(" ");
				this.sayln(dec.id + ";");
			}

		}

		this.printSpaces();
		this.sayln("char * tiger_main_arguments_gc_map = \"" + arguments + "\";");
		this.printSpaces();
		this.sayln("char * tiger_main_locals_gc_map = \"" + locals + "\";");
		this.printSpaces();
		this.sayln("//put the GC stack frame onto the call stack.");
		this.printSpaces();
		this.sayln("struct Tiger_main_gc_frame frame;");
		this.printSpaces();
		this.sayln("//push this frame onto the GC stack by setting up \"prev\".");
		this.printSpaces();
		this.sayln("frame.frame_prev = frame_prev;");
		this.printSpaces();
		this.sayln("frame_prev = &frame;");
		this.printSpaces();
		this.sayln("//setting up memory GC maps and corresponding base addresses");
		this.printSpaces();
		this.sayln("frame.arguments_gc_map = tiger_main_arguments_gc_map;");
		this.printSpaces();
		this.sayln("frame.locals_gc_map = tiger_main_locals_gc_map;");
		this.sayln("");

		for (cfg.block.T block : m.blocks) {
			cfg.block.Block b = (cfg.block.Block) block;
			b.accept(this);
		}
		this.sayln("");
		this.sayln("}\n");
		return;
	}

	// vtables
	@Override
	public void visit(cfg.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable");
		this.sayln("{");

		this.printSpaces();
		this.sayln("char * " + v.id + "_gc_map;");

		for (cfg.Ftuple t : v.ms) {
			this.printSpaces();
			t.ret.accept(this);
			this.sayln(" (*" + t.classs + "_" + t.id + ")();");
		}
		this.sayln("};\n");
		return;
	}

	private void outputVtable(cfg.vtable.Vtable v) {
		this.sayln("struct " + v.id + "_vtable " + v.id + "_vtable_ = ");
		this.sayln("{");
		this.printSpaces();
		this.sayln("\"" + this.classGcMap.get(v.id) + "\",");
		for (cfg.Ftuple t : v.ms) {
			this.printSpaces();
			this.sayln(t.classs + "_" + t.id + ",");
		}
		this.sayln("};\n");
		return;
	}

	// class
	@Override
	public void visit(cfg.classs.Class c) {

		String locals = "";

		for (cfg.Tuple dec : c.decs) {
			if (dec.type.toString().equals("@int")) {
				locals += "0";
			} else {
				locals += "1";
			}
		}

		this.classGcMap.put(c.id, locals);

		this.sayln("struct " + c.id);
		this.sayln("{");
		this.printSpaces();
		this.sayln("struct " + c.id + "_vtable *vptr;");
		this.printSpaces();
		this.sayln("int isObjOrArray;");
		this.printSpaces();
		this.sayln("unsigned length;");
		this.printSpaces();
		this.sayln("void * forwarding;");
		for (cfg.Tuple t : c.decs) {
			this.printSpaces();
			t.type.accept(this);
			this.say(" ");
			this.sayln(t.id + ";");
		}
		this.sayln("};");
		return;
	}

	// program
	@Override
	public void visit(cfg.program.Program p) {
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
		this.sayln("// Control-flow Graph\n");

		this.sayln("// structures");
		for (cfg.classs.T c : p.classes) {
			c.accept(this);
		}

		this.sayln("// vtables structures");
		for (cfg.vtable.T v : p.vtables) {
			v.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables defines");
		for (cfg.vtable.T v : p.vtables) {
			cfg.vtable.Vtable vv = (cfg.vtable.Vtable) v;
			this.sayln("struct " + vv.id + "_vtable " + vv.id + "_vtable_ ;");
		}
		this.sayln("");

		this.sayln("// methods");
		this.sayln("void * frame_prev;");
		for (cfg.method.T m : p.methods) {
			m.accept(this);
		}
		this.sayln("");

		this.sayln("// vtables");
		for (cfg.vtable.T v : p.vtables) {
			outputVtable((cfg.vtable.Vtable) v);
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
