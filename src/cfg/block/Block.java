package cfg.block;

import cfg.Visitor;

public class Block extends T {
	public util.Label label;
	public java.util.LinkedList<cfg.stm.T> stms;
	public cfg.transfer.T transfer;
	public java.util.LinkedList<cfg.block.T> pred;
	public java.util.LinkedList<cfg.block.T> succ;

	public Block(util.Label label, java.util.LinkedList<cfg.stm.T> stms, cfg.transfer.T transfer) {
		this.label = label;
		this.stms = stms;
		this.transfer = transfer;
		succ = new java.util.LinkedList<cfg.block.T>();
		pred = new java.util.LinkedList<cfg.block.T>();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof Block))
			return false;

		Block ob = (Block) o;
		return this.label.equals(ob.label);
	}

	@Override
	public String toString() {
		StringBuffer strb = new StringBuffer();
		strb.append(this.label.toString() + ":\n");
		// Lab5. Your code here:
		for (cfg.stm.T t : stms) {
			strb.append(t);
		}
		strb.append(transfer.toString());
		return strb.toString();
		// return label.toString();
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
