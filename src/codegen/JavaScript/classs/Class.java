package codegen.JavaScript.classs;

import codegen.JavaScript.Visitor;

public class Class extends T {
	public String id;
	public String extendss;
	public java.util.LinkedHashMap<String, codegen.JavaScript.Tuple> decs;
	public java.util.LinkedList<codegen.JavaScript.method.T> methods;

	public Class(String id, String extendss, java.util.LinkedHashMap<String, codegen.JavaScript.Tuple> decs, java.util.LinkedList<codegen.JavaScript.method.T> methods) {
		this.id = id;
		this.extendss = extendss;
		this.decs = decs;
		this.methods = methods;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
