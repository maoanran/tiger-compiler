package ast.exp;

public class Id extends T {
	public String id; // name of the id
	public ast.type.T type; // type of the id
	public boolean isField; // whether or not this is a class field
	public boolean isLocal;

	public Id(String id) {
		this.id = id;
		this.type = null;
		this.isField = false;
	}

	public Id(String id, int lineNum) {
		this.id = id;
		this.type = null;
		this.isField = false;
		this.lineNum = lineNum;
	}

	@Override
	public void accept(ast.Visitor v) {
		v.visit(this);
		return;
	}
}
