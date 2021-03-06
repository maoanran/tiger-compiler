package codegen.JavaScript;

public class ClassBinding {
	public String extendss; // null for non-existing extends
	public boolean visited; // whether or not this class has been visited
	public java.util.LinkedHashMap<String, Tuple> fields; // all fields
	public java.util.LinkedHashMap<String, Ftuple> methods; // all methods

	public ClassBinding(String extendss) {
		this.extendss = extendss;
		this.visited = false;
		this.fields = new java.util.LinkedHashMap<String, Tuple>();
		this.methods = new java.util.LinkedHashMap<String, Ftuple>();
	}

	// put a single field
	public void put(String c, codegen.JavaScript.type.T type, String var) {
		
		this.fields.put(var, new Tuple(c, type, var));
	}

	public void put(Tuple t) {
		this.fields.put(t.id, t);
	}

	public void updateFields(java.util.LinkedHashMap<String, Tuple> fs) {
		this.fields = fs;
	}

	public void updateMethods(java.util.LinkedHashMap<String, Ftuple> ms) {
		this.methods = ms;
	}

	public void putm(String c, codegen.JavaScript.type.T ret, java.util.LinkedList<codegen.JavaScript.dec.T> args, String mthd) {
		Ftuple t = new Ftuple(c, ret, args, mthd);
		this.methods.put(mthd, t);
		return;
	}

	@Override
	public String toString() {
		System.out.print("extends: ");
		if (this.extendss != null)
			System.out.println(this.extendss);
		else
			System.out.println("<>");
		System.out.println("\nfields:\n  ");
		System.out.println(fields.toString());
		System.out.println("\nmethods:\n  ");
		System.out.println(methods.toString());

		return "";
	}

}
