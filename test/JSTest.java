class Test {
	public static void main(String[] a) {
		System.out.println(new B().getA());
	}
}

class A {
	int a;
	public int getA(){
		B bb;
		A aa;
		
		bb = new B();
		aa = bb;

		return a;
	}
}

class B extends A{
	int b;
	public int getB(){
		return a + 100;
	}
}