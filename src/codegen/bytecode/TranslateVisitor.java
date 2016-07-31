package codegen.bytecode;

import java.util.Hashtable;
import java.util.LinkedList;

import ast.exp.Block;
import util.Label;

// Given a Java ast, translate it into Java bytecode.

public class TranslateVisitor implements ast.Visitor {
	private ClassTable table;
	private String classId;
	private int index;
	private Hashtable<String, Integer> indexTable;
	private codegen.bytecode.type.T type; // type after translation
	private codegen.bytecode.dec.T dec;
	private LinkedList<codegen.bytecode.stm.T> stms;
	private codegen.bytecode.method.T method;
	private codegen.bytecode.classs.T classs;
	private codegen.bytecode.mainClass.T mainClass;
	public codegen.bytecode.program.T program;

	public TranslateVisitor() {
		this.table = new ClassTable();
		this.classId = null;
		this.indexTable = null;
		this.type = null;
		this.dec = null;
		this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
		this.method = null;
		this.classs = null;
		this.mainClass = null;
		this.program = null;
	}

	private void emit(codegen.bytecode.stm.T s) {
		this.stms.add(s);
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(ast.exp.Add e) {
		e.left.accept(this);
		e.right.accept(this);
		emit(new codegen.bytecode.stm.Iadd());
		return;
	}

	@Override
	public void visit(ast.exp.And e) {
		e.left.accept(this);
		e.right.accept(this);
		emit(new codegen.bytecode.stm.Iand());
		return;
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		e.array.accept(this);
		e.index.accept(this);
		emit(new codegen.bytecode.stm.Iaload());
	}

	@Override
	public void visit(ast.exp.Call e) {

		e.exp.accept(this);
		for (ast.exp.T x : e.args) {
			x.accept(this);
		}
		e.rt.accept(this);
		codegen.bytecode.type.T rt = this.type;
		java.util.LinkedList<codegen.bytecode.type.T> at = new java.util.LinkedList<codegen.bytecode.type.T>();

		for (ast.type.T t : e.at) {
			t.accept(this);
			at.add(this.type);
		}

		ClassBinding cb = this.table.get(e.type);
		codegen.bytecode.Ftuple f = cb.methods.get(e.id);

		for (int i = 0; i < at.size(); i++) {
			codegen.bytecode.dec.Dec d = (codegen.bytecode.dec.Dec) f.args.get(i);
			if (!at.get(i).toString().equals(d.type.toString()))
				at.set(i, d.type);
		}

		emit(new codegen.bytecode.stm.Invokevirtual(e.id, e.type, at, rt));
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
		this.emit(new codegen.bytecode.stm.Ldc(0));
	}

	@Override
	public void visit(ast.exp.Id e) {
		int index = -1;
		if (!e.isField) {
			index = this.indexTable.get(e.id);
			ast.type.T type = e.type;
			if (type.getNum() > 0)// a reference
				emit(new codegen.bytecode.stm.Aload(index));
			else
				emit(new codegen.bytecode.stm.Iload(index));
		} else {
			emit(new codegen.bytecode.stm.Aload(0));
			e.type.accept(this);
			emit(new codegen.bytecode.stm.Getfield(this.classId, e.id, this.type));
		}
		// but what about this is a field?
		return;
	}

	@Override
	public void visit(ast.exp.Length e) {
		e.array.accept(this);
		emit(new codegen.bytecode.stm.Arraylength());
	}

	@Override
	public void visit(ast.exp.Lt e) {
		Label tl = new Label(), fl = new Label(), el = new Label();

		e.left.accept(this);
		e.right.accept(this);

		emit(new codegen.bytecode.stm.Ificmplt(tl));
		emit(new codegen.bytecode.stm.Label(fl));
		emit(new codegen.bytecode.stm.Ldc(0));
		emit(new codegen.bytecode.stm.Goto(el));
		emit(new codegen.bytecode.stm.Label(tl));
		emit(new codegen.bytecode.stm.Ldc(1));
		emit(new codegen.bytecode.stm.Goto(el));
		emit(new codegen.bytecode.stm.Label(el));
		return;
	}

	@Override
	public void visit(ast.exp.Gt e) {
		Label tl = new Label(), fl = new Label(), el = new Label();

		e.left.accept(this);
		e.right.accept(this);

		emit(new codegen.bytecode.stm.Ificmplt(tl));
		emit(new codegen.bytecode.stm.Label(fl));
		emit(new codegen.bytecode.stm.Ldc(0));
		emit(new codegen.bytecode.stm.Goto(el));
		emit(new codegen.bytecode.stm.Label(tl));
		emit(new codegen.bytecode.stm.Ldc(1));
		emit(new codegen.bytecode.stm.Goto(el));
		emit(new codegen.bytecode.stm.Label(el));
		return;
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		e.exp.accept(this);
		emit(new codegen.bytecode.stm.Newarray());
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		emit(new codegen.bytecode.stm.New(e.id));
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
		e.exp.accept(this);
		emit(new codegen.bytecode.stm.Ldc(1));
		emit(new codegen.bytecode.stm.Ixor());
	}

	@Override
	public void visit(ast.exp.Num e) {
		emit(new codegen.bytecode.stm.Ldc(e.num));
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		e.left.accept(this);
		e.right.accept(this);
		emit(new codegen.bytecode.stm.Isub());
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		emit(new codegen.bytecode.stm.Aload(0));
		return;
	}

	@Override
	public void visit(ast.exp.Times e) {
		e.left.accept(this);
		e.right.accept(this);
		emit(new codegen.bytecode.stm.Imul());
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
		this.emit(new codegen.bytecode.stm.Ldc(1));
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		if (!s.id.isField) {
			s.exp.accept(this);
			int index = -1;
			index = this.indexTable.get(s.id.id);
			ast.type.T type = s.type;
			if (type.getNum() > 0)
				emit(new codegen.bytecode.stm.Astore(index));
			else
				emit(new codegen.bytecode.stm.Istore(index));
		} else {
			emit(new codegen.bytecode.stm.Aload(0));
			s.exp.accept(this);
			s.type.accept(this);
			emit(new codegen.bytecode.stm.Putfield(this.classId, s.id.id, this.type));
		}

		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {

		s.id.accept(this);
		s.index.accept(this);
		s.exp.accept(this);
		emit(new codegen.bytecode.stm.Iastore());
		return;
	}

	@Override
	public void visit(ast.stm.Block s) {
		for (ast.stm.T ss : s.stms)
			ss.accept(this);
	}

	@Override
	public void visit(ast.stm.If s) {
		Label tl = new Label(), fl = new Label(), el = new Label();
		s.condition.accept(this);
		emit(new codegen.bytecode.stm.Ifne(tl));
		emit(new codegen.bytecode.stm.Label(fl));
		s.elsee.accept(this);
		emit(new codegen.bytecode.stm.Goto(el));
		emit(new codegen.bytecode.stm.Label(tl));
		s.thenn.accept(this);
		emit(new codegen.bytecode.stm.Goto(el));
		emit(new codegen.bytecode.stm.Label(el));
		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		s.exp.accept(this);
		emit(new codegen.bytecode.stm.Print());
		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		Label wl = new Label(), bl = new Label();
		emit(new codegen.bytecode.stm.Label(wl));
		s.condition.accept(this);
		emit(new codegen.bytecode.stm.Ifeq(bl));
		s.body.accept(this);
		emit(new codegen.bytecode.stm.Goto(wl));
		emit(new codegen.bytecode.stm.Label(bl));
		return;
	}

	// type
	@Override
	public void visit(ast.type.Boolean t) {
		this.type = new codegen.bytecode.type.Int();
	}

	@Override
	public void visit(ast.type.Class t) {
		this.type = new codegen.bytecode.type.Class(t.id);
		return;
	}

	@Override
	public void visit(ast.type.Int t) {
		this.type = new codegen.bytecode.type.Int();
	}

	@Override
	public void visit(ast.type.IntArray t) {
		this.type = new codegen.bytecode.type.IntArray();
	}

	// dec
	@Override
	public void visit(ast.dec.Dec d) {
		d.type.accept(this);
		this.dec = new codegen.bytecode.dec.Dec(this.type, d.id);
		this.indexTable.put(d.id, index++);
		return;
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		// record, in a hash table, each var's index
		// this index will be used in the load store operation
		this.index = 1;
		this.indexTable = new java.util.Hashtable<String, Integer>();

		m.retType.accept(this);
		codegen.bytecode.type.T newRetType = this.type;
		java.util.LinkedList<codegen.bytecode.dec.T> newFormals = new java.util.LinkedList<codegen.bytecode.dec.T>();
		for (ast.dec.T d : m.formals) {
			d.accept(this);
			newFormals.add(this.dec);
		}
		java.util.LinkedList<codegen.bytecode.dec.T> locals = new java.util.LinkedList<codegen.bytecode.dec.T>();
		for (ast.dec.T d : m.locals) {
			d.accept(this);
			locals.add(this.dec);
		}
		this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
		for (ast.stm.T s : m.stms) {
			s.accept(this);
		}

		// return statement is specially treated
		m.retExp.accept(this);

		if (m.retType.getNum() > 0)
			emit(new codegen.bytecode.stm.Areturn());
		else
			emit(new codegen.bytecode.stm.Ireturn());

		this.method = new codegen.bytecode.method.Method(newRetType, m.id, this.classId, newFormals, locals, this.stms, 0, this.index);

		return;
	}

	// class
	@Override
	public void visit(ast.classs.Class c) {

		this.classId = c.id;
		java.util.LinkedList<codegen.bytecode.dec.T> newDecs = new java.util.LinkedList<codegen.bytecode.dec.T>();
		java.util.LinkedList<codegen.bytecode.method.T> newMethods = new java.util.LinkedList<codegen.bytecode.method.T>();

		for (ast.dec.T dec : c.decs) {
			ast.dec.Dec d = (ast.dec.Dec) dec;
			d.type.accept(this);
			this.dec = new codegen.bytecode.dec.Dec(this.type, d.id);
			newDecs.add(this.dec);
		}
		for (ast.method.T m : c.methods) {
			m.accept(this);
			newMethods.add(this.method);
		}

		this.classs = new codegen.bytecode.classs.Class(c.id, c.extendss, newDecs, newMethods);

		return;
	}

	// main class
	@Override
	public void visit(ast.mainClass.MainClass c) {
		for (ast.stm.T s : c.stms)
			s.accept(this);
		this.mainClass = new codegen.bytecode.mainClass.MainClass(c.id, c.arg, this.stms);
		this.stms = new java.util.LinkedList<codegen.bytecode.stm.T>();
		return;
	}

	// /////////////////////////////////////////////////////
	// the first pass
	public void scanMain(ast.mainClass.T m) {
		this.table.init(((ast.mainClass.MainClass) m).id, null);
		// this is a special hacking in that we don't want to
		// enter "main" into the table.
		return;
	}

	public void scanClasses(java.util.LinkedList<ast.classs.T> cs) {
		// put empty chuncks into the table
		for (ast.classs.T c : cs) {
			ast.classs.Class cc = (ast.classs.Class) c;
			this.table.init(cc.id, cc.extendss);
		}

		this.indexTable = new java.util.Hashtable<String, Integer>();
		// put class fields and methods into the table
		for (ast.classs.T c : cs) {
			ast.classs.Class cc = (ast.classs.Class) c;
			java.util.LinkedList<codegen.bytecode.dec.T> newDecs = new java.util.LinkedList<codegen.bytecode.dec.T>();
			for (ast.dec.T dec : cc.decs) {
				dec.accept(this);
				newDecs.add(this.dec);
			}
			this.table.initDecs(cc.id, newDecs);

			// all methods
			java.util.LinkedList<ast.method.T> methods = cc.methods;
			for (ast.method.T mthd : methods) {
				ast.method.Method m = (ast.method.Method) mthd;
				java.util.LinkedList<codegen.bytecode.dec.T> newArgs = new java.util.LinkedList<codegen.bytecode.dec.T>();
				for (ast.dec.T arg : m.formals) {
					arg.accept(this);
					newArgs.add(this.dec);
				}
				m.retType.accept(this);
				codegen.bytecode.type.T newRet = this.type;
				this.table.initMethod(cc.id, newRet, newArgs, m.id);
			}
		}
		// calculate all inheritance information
		for (ast.classs.T c : cs) {
			ast.classs.Class cc = (ast.classs.Class) c;
			this.table.inherit(cc.id);
		}
	}

	public void scanProgram(ast.program.T p) {
		ast.program.Program pp = (ast.program.Program) p;
		scanMain(pp.mainClass);
		scanClasses(pp.classes);
		return;
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

		java.util.LinkedList<codegen.bytecode.classs.T> newClasses = new java.util.LinkedList<codegen.bytecode.classs.T>();
		for (ast.classs.T classs : p.classes) {
			classs.accept(this);
			newClasses.add(this.classs);
		}
		this.program = new codegen.bytecode.program.Program(this.mainClass, newClasses);
		return;
	}

	@Override
	public void visit(ast.type.Id t) {
		System.out.println("id");
	}

	@Override
	public void visit(Block s) {
		// TODO Auto-generated method stub
		
	}
}
