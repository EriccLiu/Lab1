package polynomialCalculation;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCaseDoCalculation {
  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}
  
  @Test
  public void testAdd() {
    Calculator calculator = new Calculator();
    String result;

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+(a+c)*(d+b)");
    result = calculator.info.getMessage();
    assertEquals("1", "a*c+a*c^2+a*c*d+b*c^2+b*c*d+a*d+a*b+c*d+b*c", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+((a+c)*(d+b)&");
    result = calculator.info.getMessage();
    assertEquals("2", "非法字符：&\n括号不匹配！\n不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d)))*c+(a+c)*(d+b)");
    result = calculator.info.getMessage();
    assertEquals("3", "括号不匹配！\n不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+(a+c)(d+b)");
    result = calculator.info.getMessage();
    assertEquals("4", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+(a+c)a(d+b)");
    result = calculator.info.getMessage();
    assertEquals("5", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+(a+c)*()");
    result = calculator.info.getMessage();
    assertEquals("6", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("+(a+(a+b)*(c+d))*c+(a+c)*(d+b)");
    result = calculator.info.getMessage();
    assertEquals("7", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+(a+c)*(d+b)+");
    result = calculator.info.getMessage();
    assertEquals("8", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("(a+(a+b)*(c+d))*c+(a+c)*+(d+b)");
    result = calculator.info.getMessage();
    assertEquals("9", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("   ");
    result = calculator.info.getMessage();
    assertEquals("10", "不合法输入！", result);

    calculator.info.clear();
    calculator.doCalculation("");
    result = calculator.info.getMessage();
    assertEquals("11", "无输入。\n不合法输入！", result);

  }

}
