package codegen.JavaScript.exp;

import codegen.JavaScript.Visitor;

public class Call extends T
{
  public T exp;
  public String id;
  public java.util.LinkedList<T> args;

  public Call(T exp, String id, java.util.LinkedList<T> args)
  {
    this.exp = exp;
    this.id = id;
    this.args = args;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
    return;
  }
}
