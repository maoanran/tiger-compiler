package elaborator;

public class MethodType {
	public java.util.LinkedList<ast.dec.T> argsType;
	public ast.type.T retType;
	public ast.exp.T retExp;
	public int lineNum;

	public MethodType(java.util.LinkedList<ast.dec.T> decs, ast.type.T retType, ast.exp.T retExp, int lineNum) {
		this.retType = retType;
		this.argsType = decs;
		this.lineNum = lineNum;
		this.retExp = retExp;
	}

	@Override
	public String toString() {
		String s = "";
		for (ast.dec.T dec : this.argsType) {
			ast.dec.Dec decc = (ast.dec.Dec) dec;
			s = decc.type.toString() + "*" + s;
		}
		s = s + " -> " + this.retType.toString();
		return s;
	}
}
