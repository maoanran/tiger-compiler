package ast.exp;

public class Block extends T
{
  public ast.exp.T exp;

  public Block(ast.exp.T exp)
  {
    this.exp = exp;
  }

  @Override
  public void accept(ast.Visitor v)
  {
    v.visit(this);
  }
}
