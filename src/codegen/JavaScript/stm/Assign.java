package codegen.JavaScript.stm;

import codegen.JavaScript.Visitor;

public class Assign extends T
{
  public codegen.JavaScript.exp.Id id;
  public codegen.JavaScript.exp.T exp;

  public Assign(codegen.JavaScript.exp.Id id, codegen.JavaScript.exp.T exp)
  {
    this.id = id;
    this.exp = exp;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
