package entity.dataStructure;

public class Variable extends Node {
  private String variable = null;
  private int index = 0;

  public Variable(String variable){
    this.variable = variable;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  @Override
  public String getAsString() {
    return variable;
  }
}
