package elaborator;

import java.util.Iterator;

public class ClassTable {
	// map each class name (a string), to the class bindings.
	private java.util.Hashtable<String, ClassBinding> table;

	public ClassTable() {
		this.table = new java.util.Hashtable<String, ClassBinding>();
	}

	// Duplication is not allowed
	public void put(String c, ClassBinding cb) {
		if (this.table.get(c) != null) {
			System.err.println("Error at line : " + cb.lineNum + "\t" + "duplicated class: " + c);
		}
		this.table.put(c, cb);
	}

	// put a field into this table
	// Duplication is not allowed
	public void put(String c, String id, ast.type.T type) {
		ClassBinding cb = this.table.get(c);
		cb.put(id, type);
		return;
	}

	// put a method into this table
	// Duplication is not allowed.
	// Also note that MiniJava does NOT allow overloading.
	public void put(String c, String id, MethodType type) {
		ClassBinding cb = this.table.get(c);
		cb.put(id, type);
		return;
	}

	// return null for non-existing class
	public ClassBinding get(String className) {
		if (className == null)
			return null;
		return this.table.get(className);
	}

	// get type of some field
	// return null for non-existing field.
	public ast.type.T get(String className, String xid) {
		if (xid == null || className == null)
			return null;
		ClassBinding cb = this.table.get(className);
		if (cb == null)
			return null;
		ast.type.T type = cb.fields.get(xid);
		while (type == null) { // search all parent classes until found or fail
			if (cb.extendss == null)
				return type;
			cb = this.table.get(cb.extendss);
			if (cb == null)
				break;
			type = cb.fields.get(xid);
		}
		return type;
	}

	// get type of some method
	// return null for non-existing method
	public MethodType getm(String className, String mid) {
		ClassBinding cb = this.table.get(className);
		MethodType type = cb.methods.get(mid);
		while (type == null) { // search all parent classes until found or fail
			if (cb.extendss == null)
				return type;

			cb = this.table.get(cb.extendss);
			type = cb.methods.get(mid);
		}
		return type;
	}

	public void dump() {
		for (Iterator<String> it = table.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			ClassBinding value = table.get(key);
			System.out.println("classname:\t" + key);

			System.out.print("extends: ");
			if (value.extendss != null)
				System.out.println(value.extendss);
			else
				System.out.println("<>");
			System.out.println("fields:");
			for (Iterator<String> itt = value.fields.keySet().iterator(); itt.hasNext();) {
				String keyy = itt.next();
				ast.type.T valuee = value.fields.get(keyy);
				System.out.println("\t" + keyy + "：" + valuee);
			}
			System.out.println("methods:");
			for (Iterator<String> itt = value.methods.keySet().iterator(); itt.hasNext();) {
				String keyy = itt.next();
				MethodType valuee = value.methods.get(keyy);
				System.out.println("\t" + keyy + "：\t");
				System.out.print("\t\tArguements:\t");
				for (ast.dec.T dec : valuee.argsType) {
					ast.dec.Dec decc = (ast.dec.Dec) dec;
					System.out.print(decc.type.toString() + "*\t");
				}
				System.out.println("\n\t\tReturn value:\t" + valuee.retType);
			}

			System.out.println("======================================");
		}
	}

	@Override
	public String toString() {
		return this.table.toString();
	}
}
