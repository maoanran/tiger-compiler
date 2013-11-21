package elaborator;

import java.util.Iterator;

public class MethodTable {
	private java.util.Hashtable<String, ast.type.T> table;
	// 方法内变量初始化与否
	public java.util.Hashtable<String, Boolean> localValTable;
	// 方法内变量是否使用过
	public java.util.Hashtable<String, Boolean> localValIsUseTable;

	private String className;
	private String methodName;

	public MethodTable(String methodName, String className) {
		this.table = new java.util.Hashtable<String, ast.type.T>();
		this.localValTable = new java.util.Hashtable<String, Boolean>();
		this.localValIsUseTable = new java.util.Hashtable<String, Boolean>();
		this.methodName = methodName;
		this.className = className;
	}

	// Duplication is not allowed
	public void put(java.util.LinkedList<ast.dec.T> formals, java.util.LinkedList<ast.dec.T> locals) {
		for (ast.dec.T dec : formals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.err.println("Error at line : " + decc.lineNum + "\t" + "duplicated parameter: " + decc.id);
			}
			this.table.put(decc.id, decc.type);
		}

		for (ast.dec.T dec : locals) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			if (this.table.get(decc.id) != null) {
				System.err.println("Error at line : " + decc.lineNum + "\t" + "duplicated variable: " + decc.id);
			}
			this.table.put(decc.id, decc.type);
			this.localValTable.put(decc.id, false);
			this.localValIsUseTable.put(decc.id, false);
		}
	}

	// return null for non-existing keys
	public ast.type.T get(String id) {
		if (id == null)
			return null;
		return this.table.get(id);
	}

	public void initLocalVal(String id) {
		if (id == null || !this.localValTable.containsKey(id))
			return;
		this.localValTable.remove(id);
		this.localValTable.put(id, true);
	}

	public boolean isInit(String id) {
		if (id == null || !this.localValTable.containsKey(id))
			return true;
		return this.localValTable.get(id);
	}

	public void useLocalVal(String id) {
		if (id == null || !this.localValIsUseTable.containsKey(id))
			return;
		this.localValIsUseTable.remove(id);
		this.localValIsUseTable.put(id, true);
	}

	public boolean isuse(String id) {
		if (id == null || !this.localValIsUseTable.containsKey(id))
			return true;
		return this.localValIsUseTable.get(id);
	}

	public void dump() {
		System.out.println("class：" + className);
		System.out.println("method：" + methodName);
		System.out.println("variables：");
		for (Iterator<String> it = table.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			ast.type.T value = table.get(key);
			System.out.println("\t" + key + ":\t" + value);
		}
		System.out.println("==============");
	}

	@Override
	public String toString() {
		return this.table.toString();
	}
}
