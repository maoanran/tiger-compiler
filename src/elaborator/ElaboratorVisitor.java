package elaborator;

public class ElaboratorVisitor implements ast.Visitor {
	public ClassTable classTable; // symbol table for class
	public MethodTable methodTable; // symbol table for each method
	public String currentClass; // the class name being elaborated
	public ast.type.T type; // type of the expression being elaborated
	public boolean existError;

	public ElaboratorVisitor() {
		this.classTable = new ClassTable();
		this.currentClass = null;
		this.type = null;
	}

	private void error(String info) {
		System.err.println(info);
		existError = true;
	}

	// /////////////////////////////////////////////////////
	// expressions
	@Override
	public void visit(ast.exp.Add e) {
		e.left.accept(this);
		ast.type.T leftty = this.type;
		e.right.accept(this);
		if (!leftty.toString().equals("@int") || !leftty.toString().equals(this.type.toString()))
			error("Error at line : " + (e.left.lineNum == 0 ? e.right.lineNum : e.left.lineNum) + "\t" + "不能执行加法操作  between "
					+ leftty.toString() + " and " + this.type.toString());
		this.type = new ast.type.Int();
	}

	@Override
	public void visit(ast.exp.And e) {
		e.left.accept(this);
		ast.type.T leftty = this.type;
		e.right.accept(this);
		if (leftty == null || !leftty.toString().equals("@boolean") || !this.type.toString().equals("@boolean"))
			error("Error at line : " + (e.left.lineNum == 0 ? e.right.lineNum : e.left.lineNum) + "\t" + "不能执行且操作  between "
					+ leftty.toString() + " and " + this.type.toString());
		this.type = new ast.type.Boolean();
	}

	@Override
	public void visit(ast.exp.ArraySelect e) {
		e.array.accept(this);
		if (!this.type.toString().equals("@int[]"))
			error("Error at line : " + e.array.lineNum + "\t" + "错误的数组类型");
		e.index.accept(this);
		if (!this.type.toString().equals("@int"))
			error("Error at line : " + e.array.lineNum + "\t" + "错误的数组下标");
	}

	@Override
	public void visit(ast.exp.Call e) {
		ast.type.T leftty;
		ast.type.Class ty = null;
		e.exp.accept(this);
		leftty = this.type;
		if (leftty.toString().startsWith("未知类型"))
			return;
		ty = (ast.type.Class) leftty;

		e.type = ty.id;
		MethodType mty = this.classTable.getm(ty.id, e.id);
		if (mty == null) {
			error("Error at line : " + e.lineNum + "\t" + "不存在的方法:" + e.id);
			return;
		}

		java.util.LinkedList<ast.type.T> argsty = new java.util.LinkedList<ast.type.T>();
		for (ast.exp.T a : e.args) {
			a.accept(this);
			argsty.addLast(this.type);
		}
		if (mty.argsType.size() != argsty.size()) {
			error("Error at line : " + e.lineNum + "\t" + e.id + "方法的参数个数错误,应为" + mty.argsType.size() + "个,得到" + argsty.size() + "个");
			return;
		}

		for (int i = 0; i < argsty.size(); i++) {
			ast.dec.Dec dec = (ast.dec.Dec) mty.argsType.get(i);
			if (dec.type.toString().equals(argsty.get(i).toString()))
				;
			else {
				ClassBinding cb = this.classTable.get(argsty.get(i).toString());
				boolean findParent = false;
				while (cb != null && cb.extendss != null) {
					if (!dec.type.toString().equals(cb.extendss))
						cb = this.classTable.get(cb.extendss);
					else {
						findParent = true;
						break;
					}
				}
				if (!findParent) {
					error("Error at line : " + e.lineNum + "\t" + e.id + "参数类型错误,应为" + dec.type.toString() + ",得到"
							+ argsty.get(i).toString());
					return;
				}
			}

		}
		this.type = mty.retType;
		e.at = argsty;
		e.rt = this.type;
		return;
	}

	@Override
	public void visit(ast.exp.False e) {
		this.type = new ast.type.Boolean();
	}

	@Override
	public void visit(ast.exp.Id e) {
		// first look up the id in method table
		ast.type.T type = this.methodTable.get(e.id);
		methodTable.useLocalVal(e.id);
		// if search failed, then s.id must be a class field.
		if (type == null) {
			type = this.classTable.get(this.currentClass, e.id);
			// mark this id as a field id, this fact will be
			// useful in later phase.
			e.isField = true;
		} else {
			if(this.methodTable.localValTable.get(e.id) != null)
				e.isLocal = true;
		}

		if (type == null) {
			this.type = new ast.type.Id("未知类型@" + e.id);
			return;
		}
		
		if (!methodTable.isInit(e.id)) {
			error("Error at line : " + e.lineNum + "\t" + "参数" + e.id + "未初始化!");
		}

		this.type = type;
		// record this type on this node for future use.
		e.type = type;

		return;
	}

	@Override
	public void visit(ast.exp.Length e) {
		e.array.accept(this);
		if (!this.type.toString().equals("@int[]"))
			error("Error at line : " + e.array.lineNum + "\t" + "错误的数组类型,应为@int[]");
		this.type = new ast.type.Int();
	}

	@Override
	public void visit(ast.exp.Lt e) {
		e.left.accept(this);
		ast.type.T leftty = this.type;
		e.right.accept(this);
		if (leftty == null || !leftty.toString().equals("@int") || !this.type.toString().equals("@int"))
			error("Error at line : " + (e.left.lineNum == 0 ? e.right.lineNum : e.left.lineNum) + "\t" + "不能执行小于操作  between "
					+ leftty.toString() + " and " + this.type.toString());
		this.type = new ast.type.Boolean();
	}

	@Override
	public void visit(ast.exp.Gt e) {
		e.left.accept(this);
		ast.type.T leftty = this.type;
		e.right.accept(this);
		if (leftty == null || !leftty.toString().equals("@int") || !this.type.toString().equals("@int"))
			error("Error at line : " + (e.left.lineNum == 0 ? e.right.lineNum : e.left.lineNum) + "\t" + "不能执行大于操作  between "
					+ leftty.toString() + " and " + this.type.toString());
		this.type = new ast.type.Boolean();
	}

	@Override
	public void visit(ast.exp.NewIntArray e) {
		e.exp.accept(this);
		if (!this.type.toString().equals("@int"))
			error("Error at line : " + e.exp.lineNum + "\t" + "错误的数组下标,下标类型必须为@int");
		this.type = new ast.type.IntArray();
		return;
	}

	@Override
	public void visit(ast.exp.NewObject e) {
		ClassBinding cd = this.classTable.get(e.id);
		if (cd == null) {
			error("Error at line : " + e.lineNum + "\t" + "未知类型:" + e.id);
			this.type = new ast.type.Class("未知类型@" + e.id);
		} else
			this.type = new ast.type.Class(e.id);

		e.type = new ast.type.Id(this.type.toString());
		return;
	}

	@Override
	public void visit(ast.exp.Not e) {
		e.exp.accept(this);
		if (!this.type.toString().equals("@boolean"))
			error("Error at line : " + e.lineNum + "\t" + "类型错误,!后表达式应为boolean类型");
		return;
	}

	@Override
	public void visit(ast.exp.Num e) {
		this.type = new ast.type.Int();
		return;
	}

	@Override
	public void visit(ast.exp.Sub e) {
		e.left.accept(this);
		ast.type.T leftty = this.type;
		e.right.accept(this);
		if (!leftty.toString().equals("@int") || !leftty.toString().equals(this.type.toString()))
			error("Error at line : " + (e.left.lineNum == 0 ? e.right.lineNum : e.left.lineNum) + "\t" + "不能执行减法操作  between "
					+ leftty.toString() + " and " + this.type.toString());
		this.type = new ast.type.Int();
		return;
	}

	@Override
	public void visit(ast.exp.This e) {
		this.type = new ast.type.Class(this.currentClass);
		return;
	}

	@Override
	public void visit(ast.exp.Times e) {

		e.left.accept(this);
		ast.type.T leftty = this.type;
		e.right.accept(this);
		if (!leftty.toString().equals("@int") || !leftty.toString().equals(this.type.toString()))
			error("Error at line : " + (e.left.lineNum == 0 ? e.right.lineNum : e.left.lineNum) + "\t" + "不能执行乘法操作  between "
					+ leftty.toString() + " and " + this.type.toString());
		this.type = new ast.type.Int();
		return;
	}

	@Override
	public void visit(ast.exp.True e) {
		this.type = new ast.type.Boolean();
		return;
	}

	// statements
	@Override
	public void visit(ast.stm.Assign s) {
		// first look up the id in method table
		ast.type.T type = this.methodTable.get(s.id.id);
		// if search failed, then s.id must
		if (type == null) {
			type = this.classTable.get(this.currentClass, s.id.id);
			s.id.isField = true;
		} else {
			if(this.methodTable.localValTable.get(s.id.id) != null)
				s.id.isLocal = true;
		}
		
		if (type == null) {
			error("Error at line : " + s.exp.lineNum + "\t" + "不存在的类型:" + s.id.id);
			return;
		}

		this.methodTable.initLocalVal(s.id.id);

		s.exp.accept(this);
		s.type = type;
		s.id.type = type;
		// error ! 未考虑extends
		if (!this.type.toString().equals(type.toString()))
			error("Error at line : " + s.exp.lineNum + "\t" + "不能执行赋值操作 between " + type.toString() + " and " + this.type.toString());
		return;
	}

	@Override
	public void visit(ast.stm.AssignArray s) {
		ast.type.T type = this.methodTable.get(s.id.id);
		this.methodTable.initLocalVal(s.id.id);

		s.id.type = new ast.type.IntArray();

		if (type == null) {
			type = this.classTable.get(this.currentClass, s.id.id);
			s.id.isField = true;
		} else {
			if(this.methodTable.localValTable.get(s.id.id) != null)
				s.id.isLocal = true;
		}
		
		if (type == null || type.toString() != "@int[]")
			error("Error at line : " + s.exp.lineNum + "\t" + "错误的数组类型,数组类型必须为@int[]");
		s.index.accept(this);
		if (this.type.toString() != "@int")
			error("Error at line : " + s.exp.lineNum + "\t" + "错误的数组下标,下标类型必须为@int");
		s.exp.accept(this);
		if (this.type.toString() != "@int")
			error("Error at line : " + s.exp.lineNum + "\t" + "不能执行assign操作 between @int and " + this.type.toString());
	}

	@Override
	public void visit(ast.stm.Block s) {
		for (ast.stm.T stm : s.stms)
			stm.accept(this);
	}

	@Override
	public void visit(ast.stm.Stms s) {
		for (ast.stm.T stm : s.stms)
			stm.accept(this);
	}

	@Override
	public void visit(ast.stm.If s) {
		s.condition.accept(this);
		if (!this.type.toString().equals("@boolean"))
			error("Error at line : " + s.lineNum + "\t" + "if语句的条件必须为@boolean类型");

		s.thenn.accept(this);
		if (s.elsee != null)
			s.elsee.accept(this);
		return;
	}

	@Override
	public void visit(ast.stm.Print s) {
		s.exp.accept(this);

		if (!this.type.toString().equals("@int"))
			error("Error at line : " + s.exp.lineNum + "\t" + "println方法的参数必须为@int类型,但是传递的类型是:" + this.type.toString());
		return;
	}

	@Override
	public void visit(ast.stm.While s) {
		s.condition.accept(this);
		if (!this.type.toString().equals("@boolean"))
			error("Error at line : " + s.lineNum + "\t" + "while语句的条件必须为@boolean类型");
		s.body.accept(this);
		return;
	}

	// type
	@Override
	public void visit(ast.type.Boolean t) {
		this.type = new ast.type.Boolean();
		return;
	}

	@Override
	public void visit(ast.type.Class t) {
		this.type = new ast.type.Class(t.id);
		return;
	}

	@Override
	public void visit(ast.type.Int t) {
		this.type = new ast.type.Int();
		return;
	}

	@Override
	public void visit(ast.type.IntArray t) {
		this.type = new ast.type.IntArray();
		return;
	}

	@Override
	public void visit(ast.type.Id t) {
		this.type = new ast.type.Id(t.id);
		return;
	}

	// dec
	@Override
	public void visit(ast.dec.Dec d) {
		String dt = d.type.toString();
		if (!dt.equals("@int") && !dt.equals("@boolean") && !dt.equals("@int[]")) {
			ClassBinding cb = this.classTable.get(dt);
			if (cb == null)
				error("Error at line : " + d.lineNum + "\t" + "非法的参数类型 " + dt);
		}
	}

	// method
	@Override
	public void visit(ast.method.Method m) {
		// construct the method table
		this.methodTable = new MethodTable(m.id, currentClass);
		this.methodTable.put(m.formals, m.locals);

		if (control.Control.elabMethodTable)
			this.methodTable.dump();

		MethodType mt = classTable.getm(this.currentClass, m.id);

		// 检测方法的参数类型是否合法
		for (ast.dec.T t : mt.argsType) {
			ast.dec.Dec dec = (ast.dec.Dec) t;
			dec.accept(this);
		}

		for (ast.dec.T d : m.locals) {
			d.accept(this);
		}

		for (ast.stm.T s : m.stms) {
			s.accept(this);
		}

		// 检测返回值的类型是否合法
		String retType = mt.retType.toString();
		mt.retExp.accept(this);
		if (this.type == null)
			error("Error at line : " + mt.retExp.lineNum + "\t" + "非法的返回值");
		else if (!retType.equals(this.type.toString()))
			error("Error at line : " + mt.retExp.lineNum + "\t" + "期待返回  " + retType + " 但是获得 " + this.type.toString());
		java.util.Iterator<String> it = methodTable.localValIsUseTable.keySet().iterator();

		// 检测未使用的变量
		StringBuffer buf = new StringBuffer();
		for (; it.hasNext();) {
			String key = it.next();
			Boolean value = methodTable.localValIsUseTable.get(key);
			if (!value) {
				buf.append("warning! unused variable: " + key + " in method: " + m.id + "\n");
			}
		}
		System.err.print(buf.toString());
		return;
	}

	// class
	@Override
	public void visit(ast.classs.Class c) {
		this.currentClass = c.id;
		// 检查继承是否合法
		ClassBinding cb = this.classTable.get(c.id);
		if (cb.extendss != null) {
			ClassBinding temp = this.classTable.get(cb.extendss);
			if (temp == null) {
				error("Error at line : " + c.lineNum + "\t" + "不存在类  " + cb.extendss);
			} else {
				String name = temp.extendss;
				while (name != null && temp != null) {
					if (c.id.equals(name)) {
						error("Error at line : " + c.lineNum + "\t" + "存在循环继承    between class " + c.id + " and class " + c.extendss);
						break;
					}
					temp = this.classTable.get(temp.extendss);
				}
			}
		}

		for (ast.method.T m : c.methods) {
			m.accept(this);
		}
		return;
	}

	// main class
	@Override
	public void visit(ast.mainClass.MainClass c) {
		this.currentClass = c.id;
		// "main" has an argument "arg" of type "String[]", but
		// one has no chance to use it. So it's safe sto skip it...
		for (ast.stm.T s : c.stms)
			s.accept(this);
		return;
	}

	// ////////////////////////////////////////////////////////
	// step 1: build class table
	// class table for Main class
	private void buildMainClass(ast.mainClass.MainClass main) {
		this.classTable.put(main.id, new ClassBinding(null, 1));
	}

	// class table for normal classes
	private void buildClass(ast.classs.Class c) {
		this.classTable.put(c.id, new ClassBinding(c.extendss, c.lineNum));
		for (ast.dec.T dec : c.decs) {
			ast.dec.Dec d = (ast.dec.Dec) dec;
			this.classTable.put(c.id, d.id, d.type);
		}
		for (ast.method.T method : c.methods) {
			ast.method.Method m = (ast.method.Method) method;
			this.classTable.put(c.id, m.id, new MethodType(m.formals, m.retType, m.retExp, m.lineNum));
		}
	}

	// step 1: end
	// ///////////////////////////////////////////////////

	// program
	@Override
	public void visit(ast.program.Program p) {
		// ////////////////////////////////////////////////
		// step 1: build a symbol table for class (the class table)
		// a class table is a mapping from class names to class bindings
		// classTable: className -> ClassBinding{extends, fields, methods}
		buildMainClass((ast.mainClass.MainClass) p.mainClass);
		for (ast.classs.T c : p.classes) {
			buildClass((ast.classs.Class) c);
		}

		// we can double check that the class table is OK!
		if (control.Control.elabClassTable) {
			this.classTable.dump();
		}

		// ////////////////////////////////////////////////
		// step 2: elaborate each class in turn, under the class table
		// built above.
		p.mainClass.accept(this);
		for (ast.classs.T c : p.classes) {
			c.accept(this);
		}

	}
}
