package codegen.JavaScript.exp;

public class False extends T
{
  public False()
  {
  }

  @Override
  public void accept(codegen.JavaScript.Visitor v)
  {
    v.visit(this);
    return;
  }
}
