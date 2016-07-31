package codegen.JavaScript.stm;

import codegen.JavaScript.Visitor;

public class Write extends T
{
  public codegen.JavaScript.exp.T exp;

  public Write(codegen.JavaScript.exp.T exp)
  {
    this.exp = exp;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
