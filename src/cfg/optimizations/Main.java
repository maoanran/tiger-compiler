package cfg.optimizations;

public class Main {
	public cfg.program.T program;

	public void accept(cfg.program.T cfg) {
		// reaching definition
		ReachingDefinition reachingDef = new ReachingDefinition();
		control.CompilerPass reachingDefPass = new control.CompilerPass("Reaching definition", cfg, reachingDef);
		if (control.Control.skipPass("cfg.reaching")) {
		} else {
			System.out.println("CFG Optimization:Reaching definition analysis...");
			reachingDefPass.doit();
			// Export necessary data structures
			// Your code here:

			// I change the code into static in ReachingDefinition.java
			// So I don't need to export data structures here
		}

		// constant propagation
		ConstProp constProp = new ConstProp();
		control.CompilerPass constPropPass = new control.CompilerPass("Constant propagation", cfg, constProp);
		if (control.Control.skipPass("cfg.constProp")) {
		} else {
			System.out.println("CFG Optimization:Constant propagation...");
			constPropPass.doit();
			cfg = constProp.program;
		}

		reachingDef = new ReachingDefinition();
		reachingDefPass = new control.CompilerPass("Reaching definition", cfg, reachingDef);
		if (control.Control.skipPass("cfg.reaching")) {
		} else {
			System.out.println("CFG Optimization:Reaching definition analysis...");
			reachingDefPass.doit();
			// Export necessary data structures
			// Your code here:

			// I change the code into static in ReachingDefinition.java
			// So I don't need to export data structures here
		}

		// copy propagation
		CopyProp copyProp = new CopyProp();
		control.CompilerPass copyPropPass = new control.CompilerPass("Copy propagation", cfg, copyProp);
		if (control.Control.skipPass("cfg.copyProp")) {
		} else {
			System.out.println("CFG Optimization:Copy propagation...");
			copyPropPass.doit();
			cfg = copyProp.program;
		}
		// liveness analysis
		LivenessVisitor liveness = new LivenessVisitor();
		control.CompilerPass livenessPass = new control.CompilerPass("Liveness analysis", cfg, liveness);
		if (control.Control.skipPass("cfg.Linvess")) {
		} else {
			System.out.println("CFG Optimization:liveness analysis...");
			livenessPass.doit();
			// Export necessary data structures from the
			// liveness analysis.
			// Your code here:

			// I change the code into static in LivenessVisitor.java
			// So I don't need to export data structures here
		}

		// dead-code elimination
		DeadCode deadCode = new DeadCode();
		control.CompilerPass deadCodePass = new control.CompilerPass("Dead-code elimination", cfg, deadCode);
		if (control.Control.skipPass("cfg.deadCode")) {
		} else {
			System.out.println("CFG Optimization:Dead-code elimination...");
			deadCodePass.doit();
			cfg = deadCode.program;
		}

		// available expression
		AvailExp availExp = new AvailExp();
		control.CompilerPass availExpPass = new control.CompilerPass("Available expression", cfg, availExp);
		if (control.Control.skipPass("cfg.availExp")) {
		} else {
			System.out.println("CFG Optimization:Available expression...");
			availExpPass.doit();
			// Export necessary data structures
			// Your code here:
		}

		// CSE
		Cse cse = new Cse();
		control.CompilerPass csePass = new control.CompilerPass("Common subexpression elimination", cfg, cse);
		if (control.Control.skipPass("cfg.cse")) {
		} else {
			System.out.println("CFG Optimization:Common subexpression elimination...");
			csePass.doit();
			cfg = cse.program;
		}

		program = cfg;

		return;
	}
}
