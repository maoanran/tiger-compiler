package codegen.bytecode.stm;

import util.Label;
import codegen.bytecode.Visitor;

public class Ifeq extends T
{
  public Label l;

  public Ifeq(Label l)
  {
    this.l = l;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
