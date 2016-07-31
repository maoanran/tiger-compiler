package cfg.optimizations;

public class ReachingDefinition implements cfg.Visitor {
	// gen, kill for one statement
	private java.util.LinkedHashSet<cfg.stm.T> oneStmGen;
	private java.util.LinkedHashSet<cfg.stm.T> oneStmKill;

	// gen, kill for one transfer
	private java.util.LinkedHashSet<cfg.stm.T> oneTransferGen;
	private java.util.LinkedHashSet<cfg.stm.T> oneTransferKill;

	// gen, kill for statements
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<cfg.stm.T>> stmGen;
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<cfg.stm.T>> stmKill;

	// gen, kill for transfers
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<cfg.stm.T>> transferGen;
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<cfg.stm.T>> transferKill;

	// gen, kill for blocks
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<cfg.stm.T>> blockGen;
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<cfg.stm.T>> blockKill;

	// in, out for blocks
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<cfg.stm.T>> blockIn;
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<cfg.stm.T>> blockOut;

	// in, out for statements
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<cfg.stm.T>> stmIn;
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<cfg.stm.T>> stmOut;

	// liveIn, liveOut for transfer
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<cfg.stm.T>> transferIn;
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<cfg.stm.T>> transferOut;

	java.util.LinkedHashMap<String, java.util.LinkedList<cfg.stm.T>> def;

	public ReachingDefinition() {
		this.oneStmGen = new java.util.LinkedHashSet<>();
		this.oneStmKill = new java.util.LinkedHashSet<>();

		this.oneTransferGen = new java.util.LinkedHashSet<>();
		this.oneTransferKill = new java.util.LinkedHashSet<>();

		ReachingDefinition.stmGen = new java.util.LinkedHashMap<>();
		ReachingDefinition.stmKill = new java.util.LinkedHashMap<>();

		ReachingDefinition.transferGen = new java.util.LinkedHashMap<>();
		ReachingDefinition.transferKill = new java.util.LinkedHashMap<>();

		ReachingDefinition.blockGen = new java.util.LinkedHashMap<>();
		ReachingDefinition.blockKill = new java.util.LinkedHashMap<>();

		ReachingDefinition.blockIn = new java.util.LinkedHashMap<>();
		ReachingDefinition.blockOut = new java.util.LinkedHashMap<>();

		ReachingDefinition.stmIn = new java.util.LinkedHashMap<>();
		ReachingDefinition.stmOut = new java.util.LinkedHashMap<>();

		ReachingDefinition.transferIn = new java.util.LinkedHashMap<>();
		ReachingDefinition.transferOut = new java.util.LinkedHashMap<>();

		def = new java.util.LinkedHashMap<>();
	}

	enum ReachingDefinition_Kind_t {
		None, StmGenKill, BlockGenKill, BlockInOut, StmInOut,
	}

	private ReachingDefinition_Kind_t kind = ReachingDefinition_Kind_t.None;

	// /////////////////////////////////////////////////////
	// utilities
	private void calcOneStmKill(cfg.stm.T s, String t) {
		java.util.LinkedList<cfg.stm.T> stms = def.get(t);
		for (cfg.stm.T s2 : stms) {
			if (s2 != s) {
				this.oneStmKill.add(s2);
			}
		}

	}

	// /////////////////////////////////////////////////////
	// operand
	@Override
	public void visit(cfg.operand.Int operand) {
		return;
	}

	@Override
	public void visit(cfg.operand.Var operand) {
		return;
	}

	// statements
	@Override
	public void visit(cfg.stm.Add s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.And s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.InvokeVirtual s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		for (cfg.operand.T arg : s.args) {
			arg.accept(this);
		}
		return;
	}

	@Override
	public void visit(cfg.stm.Lt s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Gt s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Move s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.src.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.NewObject s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		return;
	}

	@Override
	public void visit(cfg.stm.NewIntArray s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		return;
	}

	@Override
	public void visit(cfg.stm.Print s) {
		s.arg.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Sub s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Times s) {
		calcOneStmKill(s, s.dst);
		this.oneStmGen.add(s);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	// transfer
	@Override
	public void visit(cfg.transfer.If s) {
		// Invariant: accept() of operand modifies "gen"
		// s.operand.accept(this);
		return;
	}

	@Override
	public void visit(cfg.transfer.Goto s) {
		return;
	}

	@Override
	public void visit(cfg.transfer.Return s) {
		// Invariant: accept() of operand modifies "gen"
		// s.operand.accept(this);
		return;
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

	// utility functions:

	private boolean continuee;

	private void calculateStmTransferGenKill(cfg.block.Block b) {
		oneStmGen = new java.util.LinkedHashSet<cfg.stm.T>();
		for (cfg.stm.T s : b.stms) {
			this.oneStmGen = new java.util.LinkedHashSet<>();
			this.oneStmKill = new java.util.LinkedHashSet<>();
			s.accept(this);

			if (control.Control.isTracing("ReachingDefinition.step1")) {
				System.out.print("\ngen, kill for stm: ");
				System.out.print(s);
				System.out.print("\ngen is:\n");
				System.out.print(this.oneStmGen);
				System.out.println();
				System.out.print("\nkill is:\n");
				System.out.println(this.oneStmKill);
				System.out.println();
			}
			ReachingDefinition.stmGen.put(s, this.oneStmGen);
			ReachingDefinition.stmKill.put(s, this.oneStmKill);
		}

		this.oneTransferGen = new java.util.LinkedHashSet<>();
		this.oneTransferKill = new java.util.LinkedHashSet<>();
		b.transfer.accept(this);

		if (control.Control.isTracing("ReachingDefinition.step1")) {
			System.out.print("\ngen, kill for transfer: ");
			System.out.print("\ngen is:\n");
			System.out.print(this.oneTransferGen);
			System.out.println();
			System.out.print("\nkill is:\n");
			System.out.println(this.oneTransferKill);
			System.out.println();
		}
		ReachingDefinition.transferGen.put(b.transfer, this.oneTransferGen);
		ReachingDefinition.transferKill.put(b.transfer, this.oneTransferKill);

		return;
	}

	private void calculateBlockGenKill(cfg.block.Block b) {
		java.util.LinkedHashSet<cfg.stm.T> newGen = new java.util.LinkedHashSet<cfg.stm.T>();
		java.util.LinkedHashSet<cfg.stm.T> newKill = new java.util.LinkedHashSet<cfg.stm.T>();
		java.util.LinkedHashSet<cfg.stm.T> tempGen = null;
		for (cfg.stm.T stm : b.stms) {
			tempGen = newGen;
			newGen = new java.util.LinkedHashSet<cfg.stm.T>();

			newGen.addAll(ReachingDefinition.stmGen.get(stm));

			for (cfg.stm.T t : tempGen) {
				if (!ReachingDefinition.stmKill.get(stm).contains(t))
					newGen.add(t);
			}

			newKill.addAll(ReachingDefinition.stmKill.get(stm));
		}

		ReachingDefinition.blockGen.put(b, newGen);
		ReachingDefinition.blockKill.put(b, newKill);

		if (control.Control.isTracing("ReachingDefinition.step2")) {
			System.out.print("\ngen, kill for block: ");
			System.out.print("\ngen is:\n");
			System.out.print(newGen);
			System.out.println();
			System.out.print("\nkill is:\n");
			System.out.println(newKill);
			System.out.println();
		}
		return;
	}

	private void calculateBlockInOut(cfg.block.Block b) {
		java.util.LinkedHashSet<cfg.stm.T> newIn = ReachingDefinition.blockIn.get(b);
		java.util.LinkedHashSet<cfg.stm.T> newOut = ReachingDefinition.blockOut.get(b);

		if (newIn == null)
			newIn = new java.util.LinkedHashSet<cfg.stm.T>();
		if (newOut == null)
			newOut = new java.util.LinkedHashSet<cfg.stm.T>();

		if (ReachingDefinition.blockGen.get(b) != null && newOut.addAll(ReachingDefinition.blockGen.get(b)))
			this.continuee = true;

		for (cfg.stm.T t : newIn) {
			if (!ReachingDefinition.blockKill.get(b).contains(t) && newOut.add(t)) {
				this.continuee = true;
			}
		}

		for (cfg.block.T sb : b.pred) {
			java.util.LinkedHashSet<cfg.stm.T> block = ReachingDefinition.blockOut.get(sb);
			if (block != null && newIn.addAll(block))
				this.continuee = true;
		}

		ReachingDefinition.blockIn.put(b, newIn);
		ReachingDefinition.blockOut.put(b, newOut);

		return;
	}

	private void calculateStmTransferInOut(cfg.block.Block b) {
		for (cfg.stm.T stm : b.stms) {
			java.util.LinkedHashSet<cfg.stm.T> newIn = ReachingDefinition.stmIn.get(stm);
			java.util.LinkedHashSet<cfg.stm.T> newOut = ReachingDefinition.stmOut.get(stm);

			if (newIn == null)
				newIn = new java.util.LinkedHashSet<cfg.stm.T>();
			if (newOut == null)
				newOut = new java.util.LinkedHashSet<cfg.stm.T>();

			if (ReachingDefinition.stmGen.get(stm) != null && newOut.addAll(ReachingDefinition.stmGen.get(stm)))
				this.continuee = true;
			for (cfg.stm.T t : newIn) {
				if (!ReachingDefinition.stmKill.get(stm).contains(t) && newOut.add(t))
					this.continuee = true;
			}

			if (stm.pred == null) {
				if (b.pred.size() > 0) {
					for (cfg.block.T t : b.pred) {
						cfg.block.Block block = (cfg.block.Block) t;
						if (block.stms.size() > 0) {
							if (ReachingDefinition.stmOut.get(block.stms.getLast()) != null
									&& newIn.addAll(ReachingDefinition.stmOut.get(block.stms.getLast())))
								this.continuee = true;
						} else {
							if (ReachingDefinition.transferOut.get(block.transfer) != null
									&& newIn.addAll(ReachingDefinition.transferOut.get(block.transfer)))
								this.continuee = true;
						}
					}
				}
			} else {
				if (newIn.addAll(ReachingDefinition.stmOut.get(stm.pred)))
					this.continuee = true;
			}

			ReachingDefinition.stmIn.put(stm, newIn);
			ReachingDefinition.stmOut.put(stm, newOut);
		}
		{
			java.util.LinkedHashSet<cfg.stm.T> newIn = ReachingDefinition.transferIn.get(b.transfer);
			java.util.LinkedHashSet<cfg.stm.T> newOut = ReachingDefinition.transferOut.get(b.transfer);
			if (newIn == null)
				newIn = new java.util.LinkedHashSet<cfg.stm.T>();
			if (newOut == null)
				newOut = new java.util.LinkedHashSet<cfg.stm.T>();

			if (ReachingDefinition.transferGen.get(b.transfer) != null && newOut.addAll(ReachingDefinition.transferGen.get(b.transfer)))
				this.continuee = true;
			for (cfg.stm.T t : newIn) {
				if (!ReachingDefinition.transferKill.get(b.transfer).contains(t) && newOut.add(t))
					this.continuee = true;
			}

			if (b.transfer.pred == null) {
				if (b.pred.size() > 0) {
					for (cfg.block.T t : b.pred) {
						cfg.block.Block block = (cfg.block.Block) t;
						if (block.stms.size() > 0) {
							if (ReachingDefinition.stmOut.get(block.stms.getLast()) != null
									&& newIn.addAll(ReachingDefinition.stmOut.get(block.stms.getLast())))
								this.continuee = true;
						} else {
							if (ReachingDefinition.transferOut.get(block.transfer) != null
									&& newIn.addAll(ReachingDefinition.transferOut.get(block.transfer)))
								this.continuee = true;
						}
					}
				}
			} else {
				if (newIn.addAll(ReachingDefinition.stmOut.get(b.transfer.pred)))
					this.continuee = true;
			}

			ReachingDefinition.transferIn.put(b.transfer, newIn);
			ReachingDefinition.transferOut.put(b.transfer, newOut);

		}
		return;
	}

	// block
	@Override
	public void visit(cfg.block.Block b) {
		switch (this.kind) {
		case StmGenKill:
			calculateStmTransferGenKill(b);
			break;
		case BlockGenKill:
			calculateBlockGenKill(b);
			break;
		case BlockInOut:
			calculateBlockInOut(b);
			break;
		case StmInOut:
			calculateStmTransferInOut(b);
			break;
		default:
			return;
		}
	}

	// method
	@Override
	public void visit(cfg.method.Method m) {
		// Five steps:
		// Step 0: for each argument or local variable "x" in the
		// method m, calculate x's definition site set def(x).
		// Your code here:
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			for (cfg.stm.T s : block.stms) {
				if (s.dst != null) {
					java.util.LinkedList<cfg.stm.T> set = def.get(s.dst);
					if (set != null) {
						set.add(s);
					} else {
						set = new java.util.LinkedList<cfg.stm.T>();
						set.add(s);
						def.put(s.dst, set);
					}
				}
			}
		}

		// Step 1: calculate the "gen" and "kill" sets for each
		// statement and transfer

		this.kind = ReachingDefinition_Kind_t.StmGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}

		// Step 2: calculate the "gen" and "kill" sets for each block.
		// For this, you should visit statements and transfers in a
		// block sequentially.
		// Your code here:
		this.kind = ReachingDefinition_Kind_t.BlockGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}

		// Step 3: calculate the "in" and "out" sets for each block
		// Note that to speed up the calculation, you should use
		// a topo-sort order of the CFG blocks, and
		// crawl through the blocks in that order.
		// And also you should loop until a fix-point is reached.
		// Your code here:
		this.kind = ReachingDefinition_Kind_t.BlockInOut;

		java.util.HashMap<String, cfg.block.T> blockMap = new java.util.HashMap<>();
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			blockMap.put(block.label.toString(), block);
		}

		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			if (block.transfer.getClass().getName().toString().equals("cfg.transfer.If")) {
				cfg.transfer.If iff = (cfg.transfer.If) block.transfer;
				((cfg.block.Block) blockMap.get(iff.truee.toString())).pred.add(block);
				((cfg.block.Block) blockMap.get(iff.falsee.toString())).pred.add(block);
			} else if (block.transfer.getClass().getName().toString().equals("cfg.transfer.Goto")) {
				cfg.transfer.Goto Gotoo = (cfg.transfer.Goto) block.transfer;
				((cfg.block.Block) blockMap.get(Gotoo.label.toString())).pred.add(block);
			}
		}

		do {
			this.continuee = false;
			for (cfg.block.T block : m.blocks) {
				block.accept(this);
			}
		} while (this.continuee);

		if (control.Control.isTracing("ReachingDefinition.step3")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;

				System.out.print("\n" + b.label + ":");
				System.out.print("\nin, out for block:");
				System.out.print("\nIn is:\t");
				System.out.print(ReachingDefinition.blockIn.get(block));
				System.out.print("\nOut is:\t");
				System.out.println(ReachingDefinition.blockOut.get(block));

			}
		}
		// Step 4: calculate the "in" and "out" sets for each
		// statement and transfer
		// Your code here:
		this.kind = ReachingDefinition_Kind_t.StmInOut;

		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			Object pred = null;
			for (cfg.stm.T s : block.stms) {
				s.pred = pred;
				pred = s;
			}

			block.transfer.pred = pred;
		}

		do {
			this.continuee = false;
			for (cfg.block.T block : m.blocks) {
				block.accept(this);
			}
		} while (this.continuee);

		if (control.Control.isTracing("ReachingDefinition.step4")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;
				System.out.print("\n" + b.label + ":");

				for (cfg.stm.T stm : b.stms) {
					System.out.print("\nin, out for stm:");
					System.out.print("\nIn is:\t");
					System.out.print(ReachingDefinition.stmIn.get(stm));
					System.out.print("\nOut is:\t");
					System.out.print(ReachingDefinition.stmOut.get(stm));
					System.out.println();
				}

				System.out.print("\nin, out for transfer:");
				System.out.print("\nIn is:\t");
				System.out.print(ReachingDefinition.transferIn.get(b.transfer));
				System.out.print("\nOut is:\t");
				System.out.println(ReachingDefinition.transferOut.get(b.transfer));
			}
		}
	}

	@Override
	public void visit(cfg.mainMethod.MainMethod m) {
		// Five steps:
		// Step 0: for each argument or local variable "x" in the
		// method m, calculate x's definition site set def(x).
		// Your code here:
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			for (cfg.stm.T s : block.stms) {
				if (s.dst != null) {
					java.util.LinkedList<cfg.stm.T> set = def.get(s.dst);
					if (set != null) {
						set.add(s);
					} else {
						set = new java.util.LinkedList<cfg.stm.T>();
						set.add(s);
						def.put(s.dst, set);
					}
				}
			}
		}

		// Step 1: calculate the "gen" and "kill" sets for each
		// statement and transfer

		this.kind = ReachingDefinition_Kind_t.StmGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}

		// Step 2: calculate the "gen" and "kill" sets for each block.
		// For this, you should visit statements and transfers in a
		// block sequentially.
		// Your code here:
		this.kind = ReachingDefinition_Kind_t.BlockGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}

		// Step 3: calculate the "in" and "out" sets for each block
		// Note that to speed up the calculation, you should use
		// a topo-sort order of the CFG blocks, and
		// crawl through the blocks in that order.
		// And also you should loop until a fix-point is reached.
		// Your code here:
		this.kind = ReachingDefinition_Kind_t.BlockInOut;

		java.util.HashMap<String, cfg.block.T> blockMap = new java.util.HashMap<>();
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			blockMap.put(block.label.toString(), block);
		}

		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			if (block.transfer.getClass().getName().toString().equals("cfg.transfer.If")) {
				cfg.transfer.If iff = (cfg.transfer.If) block.transfer;
				((cfg.block.Block) blockMap.get(iff.truee.toString())).pred.add(block);
				((cfg.block.Block) blockMap.get(iff.falsee.toString())).pred.add(block);
			} else if (block.transfer.getClass().getName().toString().equals("cfg.transfer.Goto")) {
				cfg.transfer.Goto Gotoo = (cfg.transfer.Goto) block.transfer;
				((cfg.block.Block) blockMap.get(Gotoo.label.toString())).pred.add(block);
			}
		}

		do {
			this.continuee = false;
			for (cfg.block.T block : m.blocks) {
				block.accept(this);
			}
		} while (this.continuee);

		if (control.Control.isTracing("ReachingDefinition.step3")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;

				System.out.print("\n" + b.label + ":");
				System.out.print("\nin, out for block:");
				System.out.print("\nIn is:\t");
				System.out.print(ReachingDefinition.blockIn.get(block));
				System.out.print("\nOut is:\t");
				System.out.println(ReachingDefinition.blockOut.get(block));

			}
		}
		// Step 4: calculate the "in" and "out" sets for each
		// statement and transfer
		// Your code here:
		this.kind = ReachingDefinition_Kind_t.StmInOut;

		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			Object pred = null;
			for (cfg.stm.T s : block.stms) {
				s.pred = pred;
				pred = s;
			}

			block.transfer.pred = pred;
		}

		do {
			this.continuee = false;
			for (cfg.block.T block : m.blocks) {
				block.accept(this);
			}
		} while (this.continuee);

		if (control.Control.isTracing("ReachingDefinition.step4")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;
				System.out.print("\n" + b.label + ":");

				for (cfg.stm.T stm : b.stms) {
					System.out.print("\nin, out for stm:");
					System.out.print("\nIn is:\t");
					System.out.print(ReachingDefinition.stmIn.get(stm));
					System.out.print("\nOut is:\t");
					System.out.print(ReachingDefinition.stmOut.get(stm));
					System.out.println();
				}

				System.out.print("\nin, out for transfer:");
				System.out.print("\nIn is:\t");
				System.out.print(ReachingDefinition.transferIn.get(b.transfer));
				System.out.print("\nOut is:\t");
				System.out.println(ReachingDefinition.transferOut.get(b.transfer));
			}
		}
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
		p.mainMethod.accept(this);
		for (cfg.method.T mth : p.methods) {
			mth.accept(this);
		}
		return;
	}

}
