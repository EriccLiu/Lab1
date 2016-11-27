package entity;

/**
 * 用于传递接受的指令、获取的结果.
 * 
 * @author liuyx
 */
public class Information {
  /**
   * 指令类型.
   */
  private String cmdType = null;
  /**
   * 输入的指令.
   */
  private String input = null;
  /**
   * 执行过程中判断指令是否合法.
   */
  private boolean illegal = false;
  /**
   * 获取的结果.
   */
  private String result = "";
  /**
   * 当然保存的多项式树.
   */
  private TreePolynomial tree;

  public String getCmdType() {
    return cmdType;
  }

  public void setCmdType(String cmdType) {
    this.cmdType = cmdType;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public boolean isIllegal() {
    return illegal;
  }

  public void setIllegal(boolean illegal) {
    this.illegal = illegal;
  }

  public String getResult(){
    return result;
  }
  
  public TreePolynomial getTree() {
    return tree;
  }

  public void setTree(TreePolynomial tree) {
    this.tree = tree;
  }

  /**
   * 增加结果信息.
   * @param addition
   */
  public void addResult(String addition) {
    this.result = new String(this.result + addition + "\n");
  }

  /**
   * 显示出结果信息.
   */
  public void sayResult() {
    System.out.println(result);
  }

  /**
   * 清空结果信息.
   */
  public void clear() {
    result = "";
    illegal = false;
  }

}
