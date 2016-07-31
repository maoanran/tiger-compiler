class ConstFold {
	public static void main(String[] a) {
		System.out.println(new Doit().doit());
	}
}

class Doit {
	public int doit() {
		int x;
		int y;
		int z;

		x = 1;
		z = 2;
		y = x + z;

		return y;
	}
}
