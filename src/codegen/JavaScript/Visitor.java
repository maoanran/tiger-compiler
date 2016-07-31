package codegen.JavaScript;

public interface Visitor
{
  // expressions
  public void visit(codegen.JavaScript.exp.Add e);

  public void visit(codegen.JavaScript.exp.And e);

  public void visit(codegen.JavaScript.exp.ArraySelect e);

  public void visit(codegen.JavaScript.exp.Call e);

  public void visit(codegen.JavaScript.exp.Id e);

  public void visit(codegen.JavaScript.exp.Length e);

  public void visit(codegen.JavaScript.exp.Lt e);

  public void visit(codegen.JavaScript.exp.False e);
  
  public void visit(codegen.JavaScript.exp.Gt e);
  
  public void visit(codegen.JavaScript.exp.NewIntArray e);

  public void visit(codegen.JavaScript.exp.NewObject e);

  public void visit(codegen.JavaScript.exp.Not e);

  public void visit(codegen.JavaScript.exp.Num e);

  public void visit(codegen.JavaScript.exp.Sub e);

  public void visit(codegen.JavaScript.exp.This e);

  public void visit(codegen.JavaScript.exp.Times e);
  
  public void visit(codegen.JavaScript.exp.True e);
  
  // statements
  public void visit(codegen.JavaScript.stm.Assign s);

  public void visit(codegen.JavaScript.stm.AssignArray s);

  public void visit(codegen.JavaScript.stm.Block s);

  public void visit(codegen.JavaScript.stm.If s);

  public void visit(codegen.JavaScript.stm.Write s);

  public void visit(codegen.JavaScript.stm.While s);

  // type
  public void visit(codegen.JavaScript.type.Var t);
  
  // dec
  public void visit(codegen.JavaScript.dec.Dec d);

  // method
  public void visit(codegen.JavaScript.method.Method m);

  // main method
  public void visit(codegen.JavaScript.mainMethod.MainMethod m);

  // vtable
  public void visit(codegen.JavaScript.vtable.Vtable v);

  // class
  public void visit(codegen.JavaScript.classs.Class c);

  // program
  public void visit(codegen.JavaScript.program.Program p);
}
