class ConstFold {
	public static void main(String[] a) {
		System.out.println(new Doit().doit());
	}
}

class Doit {
	public int doit() {
		int x;

		x = (999 + 32) * 1 - 16 * (1 + 1);

		if ((1 + 2) < 2)
			System.out.println(1);
		else
			System.out.println(0 * 9);

		if ((true && true) && (1 > 2))
			System.out.println(1);
		else
			System.out.println(0 * 9);
		return x;
	}
}
