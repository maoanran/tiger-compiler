class DeadCode {
	public static void main(String[] a) {
		System.out.println(new Doit().doit());
	}
}

class Doit {
	public int doit() {
		int k;
		boolean a;
		
		k = 0;
		if (true)
			System.out.println(1);
		else
			System.out.println(k);

		while (false)
			System.out.println(2);
		return k;
	}
}
