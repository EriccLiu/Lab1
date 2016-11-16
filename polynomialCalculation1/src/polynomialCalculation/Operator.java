package polynomialCalculation;

import java.util.ArrayList;

public class Operator extends Node {

  private char ch;
  public ArrayList<Node> son = new ArrayList<Node>();

  public Operator(final char ch) {
    this.ch = ch;
  }

  public final void set(final char ch) {
    this.ch = ch;
  }

  public char getContent() {
    return ch;
  }

  public void addSon(final Node n) {
    son.add(n);
  }

  @Override
  public char[] get() {
    char[] ch = new char[1];
    ch[0] = this.ch;
    return ch;
  }

  @Override
  public Node getLeft() {
    return left;
  }

  @Override
  public Node getRight() {
    return right;
  }

}
