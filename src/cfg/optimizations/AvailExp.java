package cfg.optimizations;

public class AvailExp implements cfg.Visitor {
	public cfg.program.T program;
	private cfg.method.T methodd;
	private cfg.block.Block blockk;
	private cfg.stm.T stmm;

	public AvailExp() {
		this.program = null;
		this.methodd = null;
		this.blockk = null;
		this.stmm = null;
	}

	// /////////////////////////////////////////////////////
	// operand
	@Override
	public void visit(cfg.operand.Int operand) {
	}

	@Override
	public void visit(cfg.operand.Var operand) {
	}

	// statements
	@Override
	public void visit(cfg.stm.Add s) {

		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.And s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.InvokeVirtual s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Lt s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Gt s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Move s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.NewObject s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.NewIntArray s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Print s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Sub s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Times s) {
		this.stmm = s;
	}

	// transfer
	@Override
	public void visit(cfg.transfer.If s) {
	}

	@Override
	public void visit(cfg.transfer.Goto s) {
	}

	@Override
	public void visit(cfg.transfer.Return s) {
	}

	// type
	@Override
	public void visit(cfg.type.Class t) {
	}

	@Override
	public void visit(cfg.type.Int t) {
	}

	@Override
	public void visit(cfg.type.IntArray t) {
	}

	// dec
	@Override
	public void visit(cfg.dec.Dec d) {
	}

	// block
	@Override
	public void visit(cfg.block.Block b) {
		java.util.LinkedList<cfg.stm.T> newStms = new java.util.LinkedList<cfg.stm.T>();

		for (cfg.stm.T s : b.stms) {
			s.accept(this);
			newStms.add(this.stmm);
			this.stmm = null;
		}
		this.blockk = new cfg.block.Block(b.label, newStms, b.transfer);
	}

	// method
	@Override
	public void visit(cfg.method.Method m) {
		java.util.LinkedList<cfg.block.T> newBlocks = new java.util.LinkedList<cfg.block.T>();

		for (cfg.block.T block : m.blocks) {
			cfg.block.Block b = (cfg.block.Block) block;
			b.accept(this);
			newBlocks.add(this.blockk);
		}

		this.methodd = new cfg.method.Method(m.retType, m.id, m.classId, m.formals, m.locals, newBlocks, m.entry, m.exit, m.retValue);
		return;
	}

	@Override
	public void visit(cfg.mainMethod.MainMethod m) {
	}

	// vtables
	@Override
	public void visit(cfg.vtable.Vtable v) {
	}

	// class
	@Override
	public void visit(cfg.classs.Class c) {
	}

	// program
	@Override
	public void visit(cfg.program.Program p) {
		java.util.LinkedList<cfg.method.T> newMethods = new java.util.LinkedList<cfg.method.T>();

		p.mainMethod.accept(this);
		for (cfg.method.T method : p.methods) {
			method.accept(this);
			newMethods.add(this.methodd);
		}

		this.program = new cfg.program.Program(p.classes, p.vtables, newMethods, p.mainMethod);
		return;
	}

}
