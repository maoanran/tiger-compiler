package codegen.JavaScript.stm;

import codegen.JavaScript.Visitor;

public class AssignArray extends T
{
  public codegen.JavaScript.exp.Id id;
  public codegen.JavaScript.exp.T index;
  public codegen.JavaScript.exp.T exp;

  public AssignArray(codegen.JavaScript.exp.Id id, codegen.JavaScript.exp.T index, codegen.JavaScript.exp.T exp)
  {
    this.id = id;
    this.index = index;
    this.exp = exp;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
