package codegen.C.exp;

import codegen.C.Visitor;

public class Id extends T {
	public String id;
	public codegen.C.type.T type; // type of the id
	public boolean isField; // whether or not this is a class field
	public boolean isLocal;

	public Id(String id, boolean isField, boolean isLocal, codegen.C.type.T type) {
		this.id = id;
		this.type = type;
		this.isField = isField;
		this.isLocal = isLocal;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
		return;
	}
}
