package codegen.JavaScript.stm;

import codegen.JavaScript.Visitor;

public class While extends T
{
  public codegen.JavaScript.exp.T condition;
  public T body;

  public While(codegen.JavaScript.exp.T condition, T body)
  {
    this.condition = condition;
    this.body = body;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
