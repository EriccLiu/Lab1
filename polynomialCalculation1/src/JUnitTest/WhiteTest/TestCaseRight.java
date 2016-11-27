package JUnitTest.WhiteTest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import JUnitTest.TestCase;

import boundary.Interface;

public class TestCaseRight extends TestCase {
  @Test
  public void testAdd() {
    Interface inf = new Interface();
    String result ;
    
    inf.executeOrder("x*y+y");
    inf.getInfo().clear();
    inf.executeOrder("!d/dx");
    result = inf.getInfo().getResult();
    assertEquals("3", "y\n", result);
  }
}
