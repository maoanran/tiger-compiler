class DeadClass { 
	public static void main(String[] a) {
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Junk{
}
class Fac extends Junk {
	int k;
    public int ComputeFac(int num) {
        int num_aux;
        if (num < 1)
            num_aux = 1;
        else
            num_aux = num * (this.ComputeFac(num-1));
        return num_aux;
    }
    
    public int yes(){
    	return 1;
    }
}

class A{
	
}
