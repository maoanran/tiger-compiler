package codegen.JavaScript.program;

import codegen.JavaScript.Visitor;

public class Program extends T {
	public java.util.LinkedList<codegen.JavaScript.classs.T> classes;
	public codegen.JavaScript.mainMethod.T mainMethod;

	public Program(java.util.LinkedList<codegen.JavaScript.classs.T> classes, codegen.JavaScript.mainMethod.T mainMethod) {
		this.classes = classes;
		this.mainMethod = mainMethod;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
		return;
	}
}
