package polynomialCalculation;

public class HandlerSimplify extends Handler {

  public static final int NIAN = 9;

  public HandlerSimplify(final Calculator c) {
    super(c);
  }

  @Override
  public String doCmd(final String cmd) {
    return c.simplify(cmd.substring(NIAN));
  }

}
