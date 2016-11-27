package entity.dataStructure;

public class Node {
  protected Node left = null;
  protected Node right = null;

  public Node getLeft() {
    return left;
  }

  public void setLeft(Node left) {
    this.left = left;
  }

  public Node getRight() {
    return right;
  }

  public void setRight(Node right) {
    this.right = right;
  }

  /**
   * 以字符串类型返回结点内容.
   * @return
   */
  public String getAsString(){
    return null;
  }
  
}
