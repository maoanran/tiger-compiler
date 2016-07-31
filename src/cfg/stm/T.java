package cfg.stm;

public abstract class T implements cfg.Acceptable {
	public Object succ;
	public Object pred;
	public String dst;

	T() {
		pred = new Object();
	}
}
