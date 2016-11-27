package JUnitTest.BlackTest;

import static org.junit.Assert.*;
import org.junit.Test;

import JUnitTest.TestCase;
import boundary.Interface;

public class TestCaseExpression extends TestCase{
  
  @Test
  public void testAdd() {
    Interface inf = new Interface();
    String result;

    inf.executeOrder("(a+(a+b)*(c+d))*c+(a+c)*(d+b)");
    result = inf.getInfo().getResult();
    assertEquals("1", "a*c+a*c^2+a*c*d+b*c^2+b*c*d+a*d+a*b+c*d+b*c\n", result);
    inf.getInfo().clear();
    
    inf.executeOrder("(a+(a+b)*(c+d))*c+((a+c)*(d+b)&");
    result = inf.getInfo().getResult();
    assertEquals("2", "非法字符：&\n括号不匹配！\n不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("(a+(a+b)*(c+d)))*c+(a+c)*(d+b)");
    result = inf.getInfo().getResult();
    assertEquals("3", "括号不匹配！\n不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("(a+(a+b)*(c+d))*c+(a+c)(d+b)");
    result = inf.getInfo().getResult();
    assertEquals("4", "不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("(a+(a+b)*(c+d))*c+(a+c)a(d+b)");
    result = inf.getInfo().getResult();    
    assertEquals("5", "不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("(a+(a+b)*(c+d))*c+(a+c)*()");
    result = inf.getInfo().getResult();
    assertEquals("6", "不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("+(a+(a+b)*(c+d))*c+(a+c)*(d+b)");
    result = inf.getInfo().getResult();
    assertEquals("7", "不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("(a+(a+b)*(c+d))*c+(a+c)*(d+b)+");
    result = inf.getInfo().getResult();
    assertEquals("8", "不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("(a+(a+b)*(c+d))*c+(a+c)*+(d+b)");
    result = inf.getInfo().getResult();
    assertEquals("9", "不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("   ");
    result = inf.getInfo().getResult();
    assertEquals("10", "无输入！\n不合法输入！\n", result);
    inf.getInfo().clear();

    inf.executeOrder("");
    result = inf.getInfo().getResult();
    assertEquals("11", "无输入！\n不合法输入！\n", result);
    inf.getInfo().clear();
  }

}
