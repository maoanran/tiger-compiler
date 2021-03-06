package codegen.C.type;

import codegen.C.Visitor;

public class Id extends T {
	public String id;

	public Id(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "@id";
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
