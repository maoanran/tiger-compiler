package codegen.JavaScript.dec;

import codegen.JavaScript.Visitor;

public class Dec extends T
{
  public codegen.JavaScript.type.T type;
  public String id;

  public Dec(codegen.JavaScript.type.T type, String id)
  {
    this.type = type;
    this.id = id;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
