package codegen.JavaScript.exp;

import codegen.JavaScript.Visitor;

public class This extends T
{
  public This()
  {
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}
