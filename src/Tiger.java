import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import lexer.Lexer;
import lexer.Token;
import lexer.Token.Kind;
import parser.Parser;
import control.CommandLine;
import control.Control;

public class Tiger {
	public static void main(String[] args) {
		InputStream fstream;
		Parser parser;

		// ///////////////////////////////////////////////////////
		// handle command line arguments
		CommandLine cmd = new CommandLine();
		String fname = cmd.scan(args);

		// /////////////////////////////////////////////////////
		// to test the pretty printer on the "test/Fac.java" program
		if (control.Control.testFac) {
			System.out.println("Testing the Tiger compiler on Fac.java starting:");
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			ast.Fac.prog.accept(pp);

			// elaborate the given program, this step is necessary
			// for that it will annotate the AST with some
			// informations used by later phase.
			elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
			ast.Fac.prog.accept(elab);
			if (elab.existError)
				System.exit(1);

			// Compile this program to C.
			System.out.println("code generation starting");
			// code generation
			switch (control.Control.codegen) {
			case Bytecode:
				System.out.println("bytecode codegen");
				codegen.bytecode.TranslateVisitor trans = new codegen.bytecode.TranslateVisitor();
				ast.Fac.prog.accept(trans);
				codegen.bytecode.program.T bytecodeAst = trans.program;
				codegen.bytecode.PrettyPrintVisitor ppbc = new codegen.bytecode.PrettyPrintVisitor();
				bytecodeAst.accept(ppbc);
				break;
			case C:
				System.out.println("C codegen");
				codegen.C.TranslateVisitor transC = new codegen.C.TranslateVisitor();
				ast.Fac.prog.accept(transC);
				codegen.C.program.T cAst = transC.program;
				codegen.C.PrettyPrintVisitor ppc = new codegen.C.PrettyPrintVisitor();
				cAst.accept(ppc);
				break;
			case X86:
				// similar
				break;
			case JavaScript:
				System.out.println("JavaScript codegen");
				codegen.JavaScript.TranslateVisitor transJavaScript = new codegen.JavaScript.TranslateVisitor();
				ast.Fac.prog.accept(transJavaScript);
				codegen.JavaScript.program.T JavaScriptAst = transJavaScript.program;
				codegen.JavaScript.PrettyPrintVisitor ppJavaScript = new codegen.JavaScript.PrettyPrintVisitor();
				JavaScriptAst.accept(ppJavaScript);
				break;
			default:
				break;
			}
			System.out.println("Testing the Tiger compiler on Fac.java finished.");
			System.exit(1);
		}

		if (fname == null) {
			cmd.usage();
			return;
		}
		Control.fileName = fname;

		// /////////////////////////////////////////////////////
		// it would be helpful to be able to test the lexer
		// independently.
		if (control.Control.testlexer) {
			System.out.println("Testing the lexer. All tokens:");
			try {
				fstream = new BufferedInputStream(new FileInputStream(fname));
				Lexer lexer = new Lexer(fname, fstream);
				Token token = lexer.nextToken();
				while (token.kind != Kind.TOKEN_EOF) {
					System.out.println(token.toString());
					token = lexer.nextToken();
				}
				fstream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(1);
		}

		// /////////////////////////////////////////////////////////
		// normal compilation phases.
		ast.program.T theAst = null;

		// parsing the file, get an AST.
		try {
			fstream = new BufferedInputStream(new FileInputStream(fname));
			parser = new Parser(fname, fstream);

			theAst = parser.parse();

			fstream.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		// pretty printing the AST, if necessary
		if (control.Control.dumpAst) {
			ast.PrettyPrintVisitor pp = new ast.PrettyPrintVisitor();
			theAst.accept(pp);
		}

		// elaborate the AST, report all possible errors.
		elaborator.ElaboratorVisitor elab = new elaborator.ElaboratorVisitor();
		theAst.accept(elab);
		if (elab.existError)
			System.exit(1);
		// code generation
		switch (control.Control.codegen) {
		case Bytecode:
			codegen.bytecode.TranslateVisitor trans = new codegen.bytecode.TranslateVisitor();
			theAst.accept(trans);
			codegen.bytecode.program.T bytecodeAst = trans.program;
			codegen.bytecode.PrettyPrintVisitor ppbc = new codegen.bytecode.PrettyPrintVisitor();
			bytecodeAst.accept(ppbc);

			try {
				String osName = System.getProperty("os.name");
				codegen.bytecode.program.Program pro = (codegen.bytecode.program.Program) trans.program;
				String mainId = ((codegen.bytecode.mainClass.MainClass) pro.mainClass).id;
				String winCmd = "";
				if (osName.toLowerCase().indexOf("windows") > -1) {
					winCmd = "cmd /c ";
				}

				Process p = Runtime.getRuntime().exec(winCmd + "java -jar jasmin.jar " + mainId + ".j");
				p.waitFor();
				System.out.println("生成j文件 " + mainId + ".j");

				for (codegen.bytecode.classs.T t : pro.classes) {
					codegen.bytecode.classs.Class cla = (codegen.bytecode.classs.Class) t;
					p = Runtime.getRuntime().exec(winCmd + "java -jar jasmin.jar " + cla.id + ".j");
					p.waitFor();
					System.out.println("生成j文件 " + cla.id + ".j");
				}

				System.out.println("执行程序 " + mainId);
				p = Runtime.getRuntime().exec(winCmd + "java " + mainId);
				p.getOutputStream().close();
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String tmp = null;
				while ((tmp = br.readLine()) != null) {
					System.out.println(tmp);
				}
				p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case C:
			codegen.C.TranslateVisitor transC = new codegen.C.TranslateVisitor();
			theAst.accept(transC);
			codegen.C.program.T cAst = transC.program;
			codegen.C.PrettyPrintVisitor ppc = new codegen.C.PrettyPrintVisitor();
			cAst.accept(ppc);
			try {
				ProcessBuilder pb = null;
				String osName = System.getProperty("os.name");
				if (osName.toLowerCase().indexOf("windows") > -1) {
					Process p = Runtime.getRuntime().exec("cmd /c gcc " + fname + ".c" + " runtime\\runtime.c  -o " + fname + ".exe");
					System.out.println("生成c文件 " + fname + ".c 和可执行程序 " + fname + ".exe");
					p.waitFor();
					System.out.println("执行程序 " + fname + ".exe");
					// Runtime.getRuntime().exec(fname +".exe");
					pb = new ProcessBuilder(fname + ".exe");
				} else {
					Process p = Runtime.getRuntime().exec("gcc " + fname + ".c" + " runtime/runtime.c  -o " + fname + ".out");
					System.out.println("生成c文件 " + fname + ".c 和可执行程序 " + fname + ".out");
					p.waitFor();
					System.out.println("执行程序 " + fname + ".out");
					pb = new ProcessBuilder(fname + ".out");
				}
				pb.redirectErrorStream(true);
				Process p = pb.start();
				p.getOutputStream().close();
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String tmp = null;
				while ((tmp = br.readLine()) != null) {
					System.out.println(tmp);
				}
				p.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case X86:
			// similar
			break;
		case JavaScript:
			codegen.JavaScript.TranslateVisitor transJavaScript = new codegen.JavaScript.TranslateVisitor();
			theAst.accept(transJavaScript);
			codegen.JavaScript.program.T JavaScriptAst = transJavaScript.program;
			codegen.JavaScript.PrettyPrintVisitor ppJavaScript = new codegen.JavaScript.PrettyPrintVisitor();
			JavaScriptAst.accept(ppJavaScript);
			break;
		default:
			break;
		}

		// Lab3, exercise 6: add some glue code to
		// call gcc to compile the generated C or x86
		// file, or call java to run the bytecode file,
		// or dalvik to run the dalvik bytecode.
		// Your code here:

		// I wrote upon
		return;
	}
}
