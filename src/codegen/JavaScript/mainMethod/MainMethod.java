package codegen.JavaScript.mainMethod;

import codegen.JavaScript.Visitor;

public class MainMethod extends T {
	public codegen.JavaScript.stm.T stm;

	public MainMethod(codegen.JavaScript.stm.T stm) {
		this.stm = stm;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

}
