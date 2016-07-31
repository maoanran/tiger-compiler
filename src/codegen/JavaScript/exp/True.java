package codegen.JavaScript.exp;

public class True extends T
{
  public True()
  {
  }

  @Override
  public void accept(codegen.JavaScript.Visitor v)
  {
    v.visit(this);
    return;
  }
}
