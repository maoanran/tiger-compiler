package cfg.stm;

import cfg.Visitor;

public class NewObject extends T {
	// type of the destination variable
	public String c;

	public NewObject(String dst, String c) {
		this.dst = dst;
		this.c = c;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return dst + " = ((struct " + c + "*)(Tiger_new (&" + c + "_vtable_, sizeof(struct " + c + "))));\n";
		
	}
}
