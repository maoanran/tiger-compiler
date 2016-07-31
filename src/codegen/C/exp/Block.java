package codegen.C.exp;

import codegen.C.Visitor;

public class Block extends T
{
  public T exp;

  public Block(T exp)
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
