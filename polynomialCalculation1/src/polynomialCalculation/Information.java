package polynomialCalculation;

public class Information {
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  Information() {
    this.message = "";
  }

  public void addMessage(final String message) {
    StringBuffer sb = new StringBuffer(this.message);
    sb.append(message);
    this.message = sb.toString();
  }

  public void say() {
    System.out.println(this.message);
  }
  
  public void clear(){
    this.message = new String();
  }

}
