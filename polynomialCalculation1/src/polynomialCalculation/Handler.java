package polynomialCalculation;

public class Handler {

  protected Calculator c;

  protected Handler(final Calculator c) {
    this.c = c;
  }

  public String doCmd(final String cmd) {
    return null;
  }

  public boolean isExit() {
    return false;
  }
  
}
