package codegen.JavaScript.exp;

import codegen.JavaScript.Visitor;

public class NewObject extends T
{
  public String id;

  public NewObject(String id)
  {
    this.id = id;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}
