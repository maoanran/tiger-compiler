package codegen.JavaScript.stm;

import codegen.JavaScript.Visitor;

public class If extends T
{
  public codegen.JavaScript.exp.T condition;
  public T thenn;
  public T elsee;

  public If(codegen.JavaScript.exp.T condition, T thenn, T elsee)
  {
    this.condition = condition;
    this.thenn = thenn;
    this.elsee = elsee;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
