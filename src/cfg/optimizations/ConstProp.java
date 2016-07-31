package cfg.optimizations;

public class ConstProp implements cfg.Visitor {
	public cfg.program.T program;
	private cfg.method.T methodd;
	private cfg.block.Block blockk;
	private cfg.stm.T stmm;
	private java.util.HashMap<Object, cfg.operand.T> propMap;

	public ConstProp() {
		this.program = null;
		this.methodd = null;
		this.blockk = null;
		this.stmm = null;
		propMap = new java.util.HashMap<Object, cfg.operand.T>();
	}

	public boolean isInt(cfg.operand.T t) {
		if (t == null)
			return false;
		return t.getClass().getName().equals("cfg.operand.Int");
	}

	public cfg.operand.T propInt(cfg.operand.T operand, cfg.stm.T stm) {
		java.util.LinkedHashSet<cfg.stm.T> set = ReachingDefinition.stmIn.get(stm);

		for (cfg.stm.T t : set) {
			if (t.dst.equals(operand.toString())) {

				for (cfg.stm.T t2 : set)
					if (t != t2 && t.dst.equals(t2.dst))
						return operand;

				cfg.operand.T temp = this.propMap.get(t);
				if (temp != null)
					return temp;

				if (t.getClass().getName().toString().equals("cfg.stm.Move")) {
					cfg.stm.Move move = (cfg.stm.Move) t;
					if (move.src.getClass().getName().toString().equals("cfg.operand.Int"))
						return move.src;
				}
			}

		}
		return operand;
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

		if (!isInt(s.left))
			s.left = propInt(s.left, s);
		if (!isInt(s.right))
			s.right = propInt(s.right, s);

		if (isInt(s.left) && isInt(s.right)) {
			int leftt = ((cfg.operand.Int) s.left).i;
			int rightt = ((cfg.operand.Int) s.right).i;
			cfg.operand.Int result = new cfg.operand.Int(leftt + rightt);
			this.stmm = new cfg.stm.Move(s.dst, s.ty, result);

			this.propMap.put(s, result);
		} else
			this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.And s) {
		if (!isInt(s.left))
			s.left = propInt(s.left, s);
		if (!isInt(s.right))
			s.right = propInt(s.right, s);

		if (isInt(s.left) && isInt(s.right)) {
			int leftt = ((cfg.operand.Int) s.left).i;
			int rightt = ((cfg.operand.Int) s.right).i;
			cfg.operand.Int result = new cfg.operand.Int(leftt & rightt);
			this.stmm = new cfg.stm.Move(s.dst, s.ty, result);
			this.propMap.put(s, result);
		} else
			this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.InvokeVirtual s) {
		this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Lt s) {
		if (!isInt(s.left))
			s.left = propInt(s.left, s);
		if (!isInt(s.right))
			s.right = propInt(s.right, s);
		if (isInt(s.left) && isInt(s.right)) {
			int leftt = ((cfg.operand.Int) s.left).i;
			int rightt = ((cfg.operand.Int) s.right).i;
			cfg.operand.Int result = new cfg.operand.Int(leftt < rightt ? 1 : 0);
			this.stmm = new cfg.stm.Move(s.dst, s.ty, result);
			this.propMap.put(s, result);
		} else
			this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Gt s) {
		if (!isInt(s.left))
			s.left = propInt(s.left, s);
		if (!isInt(s.right))
			s.right = propInt(s.right, s);

		if (isInt(s.left) && isInt(s.right)) {
			int leftt = ((cfg.operand.Int) s.left).i;
			int rightt = ((cfg.operand.Int) s.right).i;
			cfg.operand.Int result = new cfg.operand.Int(leftt > rightt ? 1 : 0);
			this.stmm = new cfg.stm.Move(s.dst, s.ty, result);
			this.propMap.put(s, result);
		} else
			this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Move s) {
		if (!isInt(s.src))
			s.src = propInt(s.src, s);
		if (isInt(s.src)) {
			this.stmm = new cfg.stm.Move(s.dst, s.ty, s.src);
			this.propMap.put(s, s.src);
		} else
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
		if (!isInt(s.left))
			s.left = propInt(s.left, s);
		if (!isInt(s.right))
			s.right = propInt(s.right, s);

		if (isInt(s.left) && isInt(s.right)) {
			int leftt = ((cfg.operand.Int) s.left).i;
			int rightt = ((cfg.operand.Int) s.right).i;
			cfg.operand.Int result = new cfg.operand.Int(leftt - rightt);
			this.stmm = new cfg.stm.Move(s.dst, s.ty, result);
			this.propMap.put(s, result);
		} else
			this.stmm = s;
	}

	@Override
	public void visit(cfg.stm.Times s) {
		if (!isInt(s.left))
			s.left = propInt(s.left, s);
		if (!isInt(s.right))
			s.right = propInt(s.right, s);

		if (isInt(s.left) && isInt(s.right)) {
			int leftt = ((cfg.operand.Int) s.left).i;
			int rightt = ((cfg.operand.Int) s.right).i;
			cfg.operand.Int result = new cfg.operand.Int(leftt * rightt);
			this.stmm = new cfg.stm.Move(s.dst, s.ty, result);
			this.propMap.put(s, result);
		} else
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
