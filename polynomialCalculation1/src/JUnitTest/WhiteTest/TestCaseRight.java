package JUnitTest.WhiteTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import JUnitTest.TestCase;
import polynomialCalculation.Calculator;

public class TestCaseRight extends TestCase {
  @Test
  public void testAdd() {
    Calculator calculator = new Calculator();
    String result ;
    
    calculator.doCalculation("x*y+y");
    calculator.info.clear();
    calculator.doCalculation("!d/dx");
    result = calculator.info.getMessage();
    assertEquals("3", "y", result);
  }
}
