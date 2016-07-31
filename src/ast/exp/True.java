package ast.exp;

public class True extends T
{
  public True()
  {
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
    return;
  }
  
  @Override
  public String toString() {
	return "@True";
  }
}
