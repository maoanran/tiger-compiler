class AlgSimp {
	public static void main(String[] a) {
		System.out.println(new Doit().doit());
	}
}

class Doit {
	public int doit() {
		int x;

		x = 999;
		x = 0 + x;
		x = x - 0;
		x = 0 * x;
		x = 1 * x;
		x = 2 * x;
		x = x * 0;
		x = x * 1;
		x = x * 2;

		if ((1 + 2) < 2)
			System.out.println(1);
		else
			System.out.println(0 * 9);

		return 0;
	}
}