package cfg.optimizations;

import java.util.ListIterator;

public class LivenessVisitor implements cfg.Visitor {
	// gen, kill for one statement
	private java.util.LinkedHashSet<String> oneStmGen;
	private java.util.LinkedHashSet<String> oneStmKill;

	// gen, kill for one transfer
	private java.util.LinkedHashSet<String> oneTransferGen;
	private java.util.LinkedHashSet<String> oneTransferKill;

	// gen, kill for statements
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<String>> stmGen;
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<String>> stmKill;

	// gen, kill for transfers
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<String>> transferGen;
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<String>> transferKill;

	// gen, kill for blocks
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<String>> blockGen;
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<String>> blockKill;

	// liveIn, liveOut for blocks
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<String>> blockLiveIn;
	public static java.util.LinkedHashMap<cfg.block.T, java.util.LinkedHashSet<String>> blockLiveOut;

	// liveIn, liveOut for statements
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<String>> stmLiveIn;
	public static java.util.LinkedHashMap<cfg.stm.T, java.util.LinkedHashSet<String>> stmLiveOut;

	// liveIn, liveOut for transfer
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<String>> transferLiveIn;
	public static java.util.LinkedHashMap<cfg.transfer.T, java.util.LinkedHashSet<String>> transferLiveOut;

	// As you will walk the tree for many times, so
	// it will be useful to recored which is which:
	enum Liveness_Kind_t {
		None, StmGenKill, BlockGenKill, BlockInOut, StmInOut,
	}

	private Liveness_Kind_t kind = Liveness_Kind_t.None;

	public LivenessVisitor() {
		this.oneStmGen = new java.util.LinkedHashSet<>();
		this.oneStmKill = new java.util.LinkedHashSet<>();

		this.oneTransferGen = new java.util.LinkedHashSet<>();
		this.oneTransferKill = new java.util.LinkedHashSet<>();

		LivenessVisitor.stmGen = new java.util.LinkedHashMap<>();
		LivenessVisitor.stmKill = new java.util.LinkedHashMap<>();

		LivenessVisitor.transferGen = new java.util.LinkedHashMap<>();
		LivenessVisitor.transferKill = new java.util.LinkedHashMap<>();

		LivenessVisitor.blockGen = new java.util.LinkedHashMap<>();
		LivenessVisitor.blockKill = new java.util.LinkedHashMap<>();

		LivenessVisitor.blockLiveIn = new java.util.LinkedHashMap<>();
		LivenessVisitor.blockLiveOut = new java.util.LinkedHashMap<>();

		LivenessVisitor.stmLiveIn = new java.util.LinkedHashMap<>();
		LivenessVisitor.stmLiveOut = new java.util.LinkedHashMap<>();

		LivenessVisitor.transferLiveIn = new java.util.LinkedHashMap<>();
		LivenessVisitor.transferLiveOut = new java.util.LinkedHashMap<>();

		this.kind = Liveness_Kind_t.None;
	}

	// /////////////////////////////////////////////////////
	// operand
	@Override
	public void visit(cfg.operand.Int operand) {
		return;
	}

	@Override
	public void visit(cfg.operand.Var operand) {

		if (operand.id.startsWith("!")) {
			this.oneStmGen.add(operand.id.substring(1));
		} else if (operand.id.startsWith("(this->")) {
			this.oneStmGen.add(operand.id.substring(operand.id.indexOf("[") + 1, operand.id.indexOf("]")));
		} else if (operand.id.startsWith("frame.")) {
			this.oneStmGen.add(operand.id.substring(6));
		}
		this.oneStmGen.add(operand.id);

		return;
	}

	// statements
	@Override
	public void visit(cfg.stm.Add s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.And s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.InvokeVirtual s) {
		this.oneStmKill.add(s.dst);
		this.oneStmGen.add(s.obj);
		for (cfg.operand.T arg : s.args) {
			arg.accept(this);
		}
		return;
	}

	@Override
	public void visit(cfg.stm.Lt s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Gt s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Move s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.src.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.NewObject s) {
		this.oneStmKill.add(s.dst);
		return;
	}

	@Override
	public void visit(cfg.stm.NewIntArray s) {
		this.oneStmKill.add(s.dst);
		return;
	}

	@Override
	public void visit(cfg.stm.Print s) {
		s.arg.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Sub s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	@Override
	public void visit(cfg.stm.Times s) {
		this.oneStmKill.add(s.dst);
		// Invariant: accept() of operand modifies "gen"
		s.left.accept(this);
		s.right.accept(this);
		return;
	}

	// transfer
	@Override
	public void visit(cfg.transfer.If s) {
		// Invariant: accept() of operand modifies "gen"
		if (s.operand.toString().startsWith("!")) {
			this.oneTransferGen.add(s.operand.toString().substring(1));
		} else
			this.oneTransferGen.add(s.operand.toString());
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
		if (s.operand.getClass().getName().equals("cfg.operand.Var")) {
			if (s.operand.toString().startsWith("!")) {
				this.oneTransferGen.add(s.operand.toString().substring(1));
			} else
				this.oneTransferGen.add(s.operand.toString());

		}
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
		if (control.Control.isTracing("liveness.step1"))
			System.out.print("\n" + b.label + ":");
		for (cfg.stm.T s : b.stms) {
			this.oneStmGen = new java.util.LinkedHashSet<>();
			this.oneStmKill = new java.util.LinkedHashSet<>();

			s.accept(this);

			LivenessVisitor.stmGen.put(s, this.oneStmGen);
			LivenessVisitor.stmKill.put(s, this.oneStmKill);

			if (control.Control.isTracing("liveness.step1")) {
				System.out.print("\ngen, kill for statement:");
				s.toString();
				System.out.print("\ngen  is:\t");
				for (String str : this.oneStmGen) {
					System.out.print(str + ", ");
				}
				System.out.print("\nkill is:\t");
				for (String str : this.oneStmKill) {
					System.out.print(str + ", ");
				}
			}
		}

		this.oneTransferGen = new java.util.LinkedHashSet<>();
		this.oneTransferKill = new java.util.LinkedHashSet<>();

		b.transfer.accept(this);

		LivenessVisitor.transferGen.put(b.transfer, this.oneTransferGen);
		LivenessVisitor.transferKill.put(b.transfer, this.oneTransferKill);
		if (control.Control.isTracing("liveness.step1")) {
			System.out.print("\ngen, kill for transfer:");
			b.toString();
			System.out.print("\ngen  is:\t");
			for (String str : this.oneTransferGen) {
				System.out.print(str + ", ");
			}
			System.out.println("\nkill is:\t");
			for (String str : this.oneTransferKill) {
				System.out.print(str + ", ");
			}
		}

		return;
	}

	private void calculateBlockGenKill(cfg.block.Block b) {
		java.util.LinkedHashSet<String> newKill = new java.util.LinkedHashSet<String>();
		java.util.LinkedHashSet<String> newGen = new java.util.LinkedHashSet<String>();
		java.util.LinkedHashSet<String> tempGen = null;

		newKill.addAll(LivenessVisitor.transferKill.get(b.transfer));
		newGen.addAll(LivenessVisitor.transferGen.get(b.transfer));

		ListIterator<cfg.stm.T> iterator = b.stms.listIterator();
		while (iterator.hasNext())
			iterator.next();
		while (iterator.hasPrevious()) {
			cfg.stm.T s = iterator.previous();
			tempGen = newGen;
			newGen = new java.util.LinkedHashSet<String>();
			newGen.addAll(LivenessVisitor.stmGen.get(s));
			for (String string : tempGen) {
				if (!LivenessVisitor.stmKill.get(s).contains(string))
					newGen.add(string);
			}
			newKill.addAll(LivenessVisitor.stmKill.get(s));
		}

		if (control.Control.isTracing("liveness.step2")) {
			System.out.print("\n" + b.label + ":");
			System.out.print("\ngen, kill for block:");
			System.out.print("\ngen  is:\t");
			System.out.print(newGen);
			System.out.print("\nkill is:\t");
			System.out.println(newKill);
		}

		LivenessVisitor.blockKill.put(b, newKill);
		LivenessVisitor.blockGen.put(b, newGen);
		return;
	}

	private void calculateBlockInOut(cfg.block.Block b) {
		java.util.LinkedHashSet<String> newIn = LivenessVisitor.blockLiveIn.get(b);
		java.util.LinkedHashSet<String> newOut = LivenessVisitor.blockLiveOut.get(b);

		if (newIn == null)
			newIn = new java.util.LinkedHashSet<String>();
		if (newOut == null)
			newOut = new java.util.LinkedHashSet<String>();

		if (LivenessVisitor.blockGen.get(b) != null && newIn.addAll(LivenessVisitor.blockGen.get(b)))
			continuee = true;

		for (String str : newOut) {
			if (!LivenessVisitor.blockKill.get(b).contains(str) && newIn.add(str)) {
				continuee = true;
			}
		}
		for (cfg.block.T sb : b.succ) {
			java.util.LinkedHashSet<String> block = LivenessVisitor.blockLiveIn.get(sb);
			if (block != null && newOut.addAll(block))
				continuee = true;
		}

		LivenessVisitor.blockLiveIn.put(b, newIn);
		LivenessVisitor.blockLiveOut.put(b, newOut);

		return;
	}

	private void calculateStmTransferInOut(cfg.block.Block b) {

		for (cfg.stm.T stm : b.stms) {
			java.util.LinkedHashSet<String> newIn = LivenessVisitor.stmLiveIn.get(stm);
			java.util.LinkedHashSet<String> newOut = LivenessVisitor.stmLiveOut.get(stm);

			if (newIn == null)
				newIn = new java.util.LinkedHashSet<String>();
			if (newOut == null)
				newOut = new java.util.LinkedHashSet<String>();

			if (LivenessVisitor.stmGen.get(stm) != null && newIn.addAll(LivenessVisitor.stmGen.get(stm)))
				continuee = true;
			for (String str : newOut) {
				if (!LivenessVisitor.stmKill.get(stm).contains(str) && newIn.add(str))
					continuee = true;
			}

			if (stm.succ.getClass().getName().toString().startsWith("cfg.transfer")) {
				java.util.LinkedHashSet<String> set = LivenessVisitor.transferLiveIn.get(stm.succ);
				if (set != null && set.size() > 0 && newOut.addAll(set))
					continuee = true;
			} else if (LivenessVisitor.stmLiveIn.get(stm.succ) != null && newOut.addAll(LivenessVisitor.stmLiveIn.get(stm.succ)))
				continuee = true;

			LivenessVisitor.stmLiveIn.put(stm, newIn);
			LivenessVisitor.stmLiveOut.put(stm, newOut);
		}
		{
			java.util.LinkedHashSet<String> newIn = LivenessVisitor.transferLiveIn.get(b.transfer);
			java.util.LinkedHashSet<String> newOut = LivenessVisitor.transferLiveOut.get(b.transfer);
			if (newIn == null)
				newIn = new java.util.LinkedHashSet<String>();
			if (newOut == null)
				newOut = new java.util.LinkedHashSet<String>();

			if (LivenessVisitor.transferGen.get(b.transfer) != null && newIn.addAll(LivenessVisitor.transferGen.get(b.transfer)))
				continuee = true;
			for (String str : newOut)
				if (!LivenessVisitor.transferKill.get(b.transfer).contains(str) && newIn.add(str))
					continuee = true;

			for (cfg.block.T t : b.transfer.succ) {
				cfg.block.Block block = (cfg.block.Block) t;
				if (0 != block.stms.size()) {
					cfg.stm.T stm = block.stms.getFirst();
					if (LivenessVisitor.stmLiveIn.get(stm) != null && newOut.addAll(LivenessVisitor.stmLiveIn.get(stm)))
						continuee = true;
				} else if (block.transfer != null) {
					if (LivenessVisitor.transferLiveIn.get(block.transfer) != null
							&& newOut.addAll(LivenessVisitor.transferLiveIn.get(block.transfer)))
						continuee = true;
				} else
					System.err.println("11111111111");
			}

			LivenessVisitor.transferLiveIn.put(b.transfer, newIn);
			LivenessVisitor.transferLiveOut.put(b.transfer, newOut);

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
		// Four steps:
		// Step 1: calculate the "gen" and "kill" sets for each
		// statement and transfer
		this.kind = Liveness_Kind_t.StmGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}
		// Step 2: calculate the "gen" and "kill" sets for each block.
		// For this, you should visit statements and transfers in a
		// block in a reverse order.
		// Your code here:
		this.kind = Liveness_Kind_t.BlockGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}
		// Step 3: calculate the "liveIn" and "liveOut" sets for each block
		// Note that to speed up the calculation, you should first
		// calculate a reverse topo-sort order of the CFG blocks, and
		// crawl through the blocks in that order.
		// And also you should loop until a fix-point is reached.
		// Your code here:
		this.kind = Liveness_Kind_t.BlockInOut;

		java.util.HashMap<String, cfg.block.T> blockMap = new java.util.HashMap<>();
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			blockMap.put(block.label.toString(), block);
		}

		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			if (block.transfer.getClass().getName().toString().equals("cfg.transfer.If")) {
				cfg.transfer.If iff = (cfg.transfer.If) block.transfer;
				block.succ.add(blockMap.get(iff.truee.toString()));
				block.succ.add(blockMap.get(iff.falsee.toString()));
			} else if (block.transfer.getClass().getName().toString().equals("cfg.transfer.Goto")) {
				cfg.transfer.Goto Gotoo = (cfg.transfer.Goto) block.transfer;
				block.succ.add(blockMap.get(Gotoo.label.toString()));
			}
		}

		ListIterator<cfg.block.T> iterator = m.blocks.listIterator();
		do {
			continuee = false;
			while (iterator.hasNext())
				iterator.next().accept(this);
			while (iterator.hasPrevious()) {
				iterator.previous();
			}
		} while (continuee);

		if (control.Control.isTracing("liveness.step3")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;

				System.out.print("\n" + b.label + ":");
				System.out.print("\nin, out for block:");
				System.out.print("\nIn is:\t");
				System.out.print(LivenessVisitor.blockLiveIn.get(block));
				System.out.print("\nOut is:\t");
				System.out.println(LivenessVisitor.blockLiveOut.get(block));

			}
		}
		// Step 4: calculate the "liveIn" and "liveOut" sets for each
		// statement and transfer
		// Your code here:
		this.kind = Liveness_Kind_t.StmInOut;
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;

			cfg.stm.T last = null;
			for (cfg.stm.T s : block.stms) {
				if (last != null)
					last.succ = s;
				last = s;
			}

			if (last != null)
				last.succ = block.transfer;

			if (block.transfer.getClass().getName().toString().equals("cfg.transfer.If")) {
				cfg.transfer.If iff = (cfg.transfer.If) block.transfer;
				block.transfer.succ.add(blockMap.get(iff.truee.toString()));
				block.transfer.succ.add(blockMap.get(iff.falsee.toString()));
			} else if (block.transfer.getClass().getName().toString().equals("cfg.transfer.Goto")) {
				cfg.transfer.Goto Gotoo = (cfg.transfer.Goto) block.transfer;
				block.transfer.succ.add(blockMap.get(Gotoo.label.toString()));
			}
		}

		do {
			continuee = false;
			for (cfg.block.T block : m.blocks) {
				block.accept(this);
			}
		} while (continuee);

		if (control.Control.isTracing("liveness.step4")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;
				System.out.print("\n" + b.label + ":");

				for (cfg.stm.T stm : b.stms) {
					System.out.print("\nin, out for stm:");
					System.out.print("\nIn is:\t");
					System.out.print(LivenessVisitor.stmLiveIn.get(stm));
					System.out.print("\nOut is:\t");
					System.out.print(LivenessVisitor.stmLiveOut.get(stm));
				}

				System.out.print("\nin, out for transfer:");
				System.out.print("\nIn is:\t");
				System.out.print(LivenessVisitor.transferLiveIn.get(b.transfer));
				System.out.print("\nOut is:\t");
				System.out.println(LivenessVisitor.transferLiveOut.get(b.transfer));
			}
		}
	}

	@Override
	public void visit(cfg.mainMethod.MainMethod m) {
		// Four steps:
		// Step 1: calculate the "gen" and "kill" sets for each
		// statement and transfer
		this.kind = Liveness_Kind_t.StmGenKill;
		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}

		// Step 2: calculate the "gen" and "kill" sets for each block.
		// For this, you should visit statements and transfers in a
		// block in a reverse order.
		// Your code here:
		this.kind = Liveness_Kind_t.BlockGenKill;

		for (cfg.block.T block : m.blocks) {
			block.accept(this);
		}
		// Step 3: calculate the "liveIn" and "liveOut" sets for each block
		// Note that to speed up the calculation, you should first
		// calculate a reverse topo-sort order of the CFG blocks, and
		// crawl through the blocks in that order.
		// And also you should loop until a fix-point is reached.
		// Your code here:
		this.kind = Liveness_Kind_t.BlockInOut;

		java.util.HashMap<String, cfg.block.T> blockMap = new java.util.HashMap<>();
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			blockMap.put(block.label.toString(), block);
		}

		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;
			if (block.transfer.getClass().getName().toString().equals("cfg.transfer.If")) {
				cfg.transfer.If iff = (cfg.transfer.If) block.transfer;
				block.succ.add(blockMap.get(iff.truee.toString()));
				block.succ.add(blockMap.get(iff.falsee.toString()));
			} else if (block.transfer.getClass().getName().toString().equals("cfg.transfer.Goto")) {
				cfg.transfer.Goto Gotoo = (cfg.transfer.Goto) block.transfer;
				block.succ.add(blockMap.get(Gotoo.label.toString()));
			}
		}

		ListIterator<cfg.block.T> iterator = m.blocks.listIterator();
		while (iterator.hasNext())
			iterator.next();
		while (iterator.hasPrevious()) {
			iterator.previous().accept(this);
		}

		// Step 4: calculate the "liveIn" and "liveOut" sets for each
		// statement and transfer
		// Your code here:
		this.kind = Liveness_Kind_t.StmInOut;
		for (cfg.block.T b : m.blocks) {
			cfg.block.Block block = (cfg.block.Block) b;

			cfg.stm.T last = null;
			for (cfg.stm.T s : block.stms) {
				if (last != null)
					last.succ = s;
				last = s;
			}

			if (last != null)
				last.succ = block.transfer;

			if (block.transfer.getClass().getName().toString().equals("cfg.transfer.If")) {
				cfg.transfer.If iff = (cfg.transfer.If) block.transfer;
				block.transfer.succ.add(blockMap.get(iff.truee.toString()));
				block.transfer.succ.add(blockMap.get(iff.falsee.toString()));
			} else if (block.transfer.getClass().getName().toString().equals("cfg.transfer.Goto")) {
				cfg.transfer.Goto Gotoo = (cfg.transfer.Goto) block.transfer;
				block.transfer.succ.add(blockMap.get(Gotoo.label.toString()));
			}
		}

		do {
			continuee = false;
			for (cfg.block.T block : m.blocks) {
				block.accept(this);
			}
		} while (continuee);
		if (control.Control.isTracing("liveness.step4")) {
			for (cfg.block.T block : m.blocks) {
				cfg.block.Block b = (cfg.block.Block) block;
				System.out.print("\n" + b.label + ":");

				for (cfg.stm.T stm : b.stms) {
					System.out.print("\nin, out for stm:");
					System.out.print("\nIn is:\t");
					System.out.print(LivenessVisitor.stmLiveIn.get(stm));
					System.out.print("\nOut is:\t");
					System.out.print(LivenessVisitor.stmLiveOut.get(stm));
				}

				System.out.print("\nin, out for transfer:");
				System.out.print("\nIn is:\t");
				System.out.print(LivenessVisitor.transferLiveIn.get(b.transfer));
				System.out.print("\nOut is:\t");
				System.out.println(LivenessVisitor.transferLiveOut.get(b.transfer));
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
