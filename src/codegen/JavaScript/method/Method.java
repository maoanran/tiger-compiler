package codegen.JavaScript.method;

import codegen.JavaScript.Visitor;

public class Method extends T
{
  public codegen.JavaScript.type.T retType;
  public String classId;
  public String id;
  public java.util.LinkedList<codegen.JavaScript.dec.T> formals;
  public java.util.LinkedList<codegen.JavaScript.dec.T> locals;
  public java.util.LinkedList<codegen.JavaScript.stm.T> stms;
  public codegen.JavaScript.exp.T retExp;

  public Method(codegen.JavaScript.type.T retType, String classId, String id,
      java.util.LinkedList<codegen.JavaScript.dec.T> formals,
      java.util.LinkedList<codegen.JavaScript.dec.T> locals,
      java.util.LinkedList<codegen.JavaScript.stm.T> stms, codegen.JavaScript.exp.T retExp)
  {
    this.retType = retType;
    this.classId = classId;
    this.id = id;
    this.formals = formals;
    this.locals = locals;
    this.stms = stms;
    this.retExp = retExp;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }

}
