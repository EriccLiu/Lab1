package JUnitTest.WhiteTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import JUnitTest.TestCase;
import polynomialCalculation.Calculator;

public class TestCaseMulti_Unexist extends TestCase {

  @Test
  public void testAdd() {
    Calculator calculator = new Calculator();
    String result ;
    
    calculator.doCalculation("x*y");
    calculator.info.clear();
    calculator.doCalculation("!d/d a b");
    result = calculator.info.getMessage();
    assertEquals("1.2", "变量a不存在\n只能对一个变量求导.\n", result);
  }

}
