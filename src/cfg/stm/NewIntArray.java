package cfg.stm;

import cfg.Visitor;

public class NewIntArray extends T {
	// type of the destination variable
	public cfg.operand.T c;

	public NewIntArray(String dst, cfg.operand.T c) {
		this.dst = dst;
		this.c = c;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		return dst + " = (int *)Tiger_new_array(sizeof(int) * " + this.c + ");\n";
	}
}
