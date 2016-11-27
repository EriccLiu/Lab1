package entity.dataStructure;

import java.util.ArrayList;

public class Operator extends Node {
  private char operator = ' ';
  /**
   * 作为树的根节点时，用于保存叶子节点.
   */
  private ArrayList<Node> son = new ArrayList<Node>();

  public Operator(char operator){
    this.operator = operator;
  }
  
  public char getOperator() {
    return operator;
  }

  public void setOperator(char operator) {
    this.operator = operator;
  }

  public ArrayList<Node> getSon() {
    return son;
  }

  public void addSon(final Node node) {
    son.add(node);
  }
  
  @Override
  public String getAsString() {
    char[] ope = new char[1];
    ope[0] = this.operator;
    return new String(ope);
  }

  /**
   * 获取第i个叶子结点.
   * @param i.
   * @return node.
   */
  public Node getLeaf(int i) {
    Node node = null;

    if (son.size() >= i) {
      node = son.get(i);
    }

    return node;
  }

}
