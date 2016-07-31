package cfg.transfer;

public abstract class T implements cfg.Acceptable {
	public java.util.LinkedList<cfg.block.T> succ;
	public Object pred;

	T() {
		succ = new java.util.LinkedList<>();
	}
}
