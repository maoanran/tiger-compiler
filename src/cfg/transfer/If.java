package cfg.transfer;

import cfg.Visitor;

public class If extends T {
	public cfg.operand.T operand;
	public util.Label truee;
	public util.Label falsee;

	public If(cfg.operand.T operand, util.Label truee, util.Label falsee) {
		this.operand = operand;
		this.truee = truee;
		this.falsee = falsee;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("if (");
		sb.append(this.operand.toString());
		sb.append(")\n");
		sb.append("  goto " + this.truee.toString() + ";\n");
		sb.append("else\n");
		sb.append("  goto " + this.falsee.toString() + ";\n");
		return sb.toString();
	}
}
