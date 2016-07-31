package ast.type;

import ast.Visitor;

public class Id extends T
{
  public String id; 
  public Id(String id)
  {
	this.id = id;
  }

  @Override
  public String toString()
  {
    return "@id";
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }

  @Override
  public int getNum()
  {
    return 3;
  }

}
