package codegen.JavaScript.type;

import codegen.JavaScript.Visitor;

public class Var extends T
{
  public Var()
  {
  }

  @Override
  public String toString()
  {
    return "@var";
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
