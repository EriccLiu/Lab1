package polynomialCalculation;

public class HandlerDerivation extends Handler {

  public static final int FOUR = 4;

  public HandlerDerivation(final Calculator c) {
    super(c);
  }

  @Override
  public String doCmd(final String cmd) {
    return c.derivation(cmd.substring(FOUR));
  }

}
