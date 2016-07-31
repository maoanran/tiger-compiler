package ast.dec;

import ast.Visitor;

public class Dec extends T {
	public ast.type.T type;
	public String id;
	public boolean isUsed;

	public Dec(ast.type.T type, String id) {
		this.type = type;
		this.id = id;
		this.isUsed = true;
	}

	public Dec(ast.type.T type, String id, int lineNum) {
		this.type = type;
		this.id = id;
		this.lineNum = lineNum;
		this.isUsed = true;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
