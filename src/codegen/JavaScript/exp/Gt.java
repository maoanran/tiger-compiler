package codegen.JavaScript.exp;

import codegen.JavaScript.Visitor;

public class Gt extends T
{
  public T left;
  public T right;

  public Gt(T left, T right)
  {
    this.left = left;
    this.right = right;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}
