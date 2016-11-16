package polynomialCalculation;

public class HandlerExit extends Handler {

  public HandlerExit(final Calculator c) {
    super(c);
  }

  @Override
  public boolean isExit() {
    return true;
  }

}
