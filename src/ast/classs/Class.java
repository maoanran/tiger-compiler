package ast.classs;

import ast.Visitor;

public class Class extends T
{
  public String id;
  public String extendss; // null for non-existing "extends"
  public int lineNum;
  public java.util.LinkedList<ast.dec.T> decs;
  public java.util.LinkedList<ast.method.T> methods;

  public Class(String id, String extendss,
      java.util.LinkedList<ast.dec.T> decs,
      java.util.LinkedList<ast.method.T> methods,int lineNum)
  {
    this.id = id;
    this.extendss = extendss;
    this.decs = decs;
    this.methods = methods;
    this.lineNum = lineNum;
  }

  @Override
  public void accept(Visitor v)
  {
    v.visit(this);
  }
}
