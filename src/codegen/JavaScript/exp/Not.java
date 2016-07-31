package codegen.JavaScript.exp;

import codegen.JavaScript.Visitor;

public class Not extends T
{
  public T exp;

  public Not(T exp)
  {
    this.exp = exp;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}
