package entity.dataStructure;

public class Digit extends Node {
  private int digit = 0;

  public Digit(int digit){
    this.digit = digit;
  }
  
  public int getDigit() {
    return digit;
  }

  public void setDigit(int digit) {
    this.digit = digit;
  }

  @Override
  public String getAsString() {
    return new Integer(digit).toString();
  }
  
}
