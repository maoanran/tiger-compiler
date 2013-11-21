class BinarySearch {
    public static void main(String[] a){
	System.out.println(new BS().Start(20));
    }
}
// This class contains the array of integers and
// methods to initialize, print and sort the array
// using Bublesort
class BS{
    int[] number ;
    int size ;
    // Invoke the Initialization, Sort and Printing
    // Methods
    public int Start(int sz){
	int aux01 ;
	aux01 = this.Init(sz);
	aux01 = this.Print();
	System.out.println(99999);
	aux01 = this.Sort();
	aux01 = this.Print();
	return 0 ;
    }

 
    // Sort array of integers using Bublesort method
    public int Sort(){
	int nt ;
	int i ;
	int aux02 ;
	int aux04 ;
	int aux05 ;
	int aux06 ;
	int aux07 ;
	int j ;
	int t ;
	i = size - 1 ;
	aux02 = 0 - 1 ;
	while (aux02 < i) {
	    j = 1 ;
	    //aux03 = i+1 ;
	    while (j < (i+1)){
		aux07 = j - 1 ;
		aux04 = number[aux07] ;
		aux05 = number[j] ;
		if (aux05 < aux04) {
		    aux06 = j - 1 ;
		    t = number[aux06] ;
		    number[aux06] = number[j] ;
		    number[j] = t;
		}
		else nt = 0 ;
		j = j + 1 ;
	    }
	    i = i - 1 ;
	}
	return 0 ;
    }

    // Printing method
    public int Print(){
	int j ;
	boolean b;
	
	j = 0 ;
	
	System.out.println(j);
	System.out.println(size);
	b = j < size; 
	if(b)
		System.out.println(1);
	else
		System.out.println(0);
	while (j < (size)) {
	    System.out.println(number[j]);
	    j = j + 1 ;
	}
	return 0 ;
    }
    
    // Initialize array of integers
    public int Init(int sz){
	size = sz ;
	number = new int[sz] ;
	
	number[0] = 20 ;
	number[1] = 7  ; 
	number[2] = 12 ;
	number[3] = 18 ;
	number[4] = 2  ; 
	number[5] = 11 ;
	number[6] = 6  ; 
	number[7] = 9  ; 
	number[8] = 19 ; 
	number[9] = 5  ;

	return 0 ;	
    }

}
