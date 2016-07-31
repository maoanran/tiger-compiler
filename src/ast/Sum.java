package ast;

import util.Flist;

public class Sum {
	// Lab2, exercise 2: you should write some code to
	// represent the program "test/Sum.java".
	// Your code here:

	static ast.exp.NewObject obj = new ast.exp.NewObject("Doit");
	static ast.exp.Num num = new ast.exp.Num(101);
	static ast.exp.Call exp = new ast.exp.Call(obj, "doit", new util.Flist<ast.exp.T>().addAll(num));
	static ast.stm.Print stm = new ast.stm.Print(exp);
	static ast.mainClass.MainClass mainClass = new ast.mainClass.MainClass("Sum", "a", new Flist<ast.stm.T>().addAll(stm));
	static java.util.LinkedList<ast.dec.T> formals = new util.Flist<ast.dec.T>().addAll(new ast.dec.Dec(new ast.type.Int(), "n"));
	static java.util.LinkedList<ast.dec.T> locals = new util.Flist<ast.dec.T>().addAll(new ast.dec.Dec(new ast.type.Int(), "sum"),
			new ast.dec.Dec(new ast.type.Int(), "i"));

	static ast.stm.Assign stm1 = new ast.stm.Assign(new ast.exp.Id("i"), new ast.exp.Num(0));
	static ast.stm.Assign stm2 = new ast.stm.Assign(new ast.exp.Id("sum"), new ast.exp.Num(0));

	static ast.stm.Assign stm3_1 = new ast.stm.Assign(new ast.exp.Id("sum"), new ast.exp.Add(new ast.exp.Id("sum"), new ast.exp.Id("i")));
	static ast.stm.Block block = new ast.stm.Block(new util.Flist<ast.stm.T>().addAll(stm3_1));
	static ast.stm.While stm3 = new ast.stm.While(new ast.exp.Lt(new ast.exp.Id("i"), new ast.exp.Id("n")), block);

	static java.util.LinkedList<ast.stm.T> stms = new util.Flist<ast.stm.T>().addAll(stm1, stm2, stm3);
	static ast.method.Method methods = new ast.method.Method(new ast.type.Int(), "doit", formals, locals, stms, new ast.exp.Id("sum"));
	static ast.classs.Class classes = new ast.classs.Class("Doit", null, new util.Flist<ast.dec.T>().addAll(),
			new util.Flist<ast.method.T>().addAll(methods), 0);

	public static ast.program.Program prog = new ast.program.Program(mainClass, new util.Flist<ast.classs.T>().addAll(classes));
}

// class Sum {
// public static void main(String[] a) {
// System.out.println(new Doit().doit(101));
// }
// }
//
// class Doit {
// public int doit(int n) {
// int sum;
// int i;
//
// i = 0;
// sum = 0;
// while (i<n)
// sum = sum + i;
// return sum;
// }
// }

