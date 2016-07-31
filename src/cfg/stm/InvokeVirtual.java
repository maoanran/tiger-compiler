package cfg.stm;

import cfg.Visitor;

public class InvokeVirtual extends T {
	public String obj;
	public String f;
	// type of the destination variable
	public java.util.LinkedList<cfg.operand.T> args;

	public InvokeVirtual(String dst, String obj, String f, java.util.LinkedList<cfg.operand.T> args) {
		this.dst = dst;
		this.obj = obj;
		this.f = f;
		this.args = args;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.dst + " = " + this.obj);
		sb.append("->vptr->" + this.f + "(" + this.obj);
		for (cfg.operand.T x : this.args) {
			sb.append(", ");
			sb.append(x.toString());
		}
		sb.append(");\n");
		return sb.toString();
	}
}
