class ConstFold {
	public static void main(String[] a) {
		System.out.println(new Doit().doit());
	}
}

class Doit {
	public int doit() {
		Doit x;
		Doit y;
		Doit z;

		x = new Doit();
		z = x;
		y = z;

		return y.get();
	}

	public int get() {
		return 1;
	}
}
