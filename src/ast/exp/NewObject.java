package ast.exp;

public class NewObject extends T {
	public String id;
	public ast.type.Id type;

	public NewObject(String id) {
		this.id = id;
	}

	public NewObject(String id, ast.type.Id type, int lineNum) {
		this.id = id;
		this.type = type;
		this.lineNum = lineNum;
	}

	@Override
	public void accept(ast.Visitor v) {
		v.visit(this);
		return;
	}
}
