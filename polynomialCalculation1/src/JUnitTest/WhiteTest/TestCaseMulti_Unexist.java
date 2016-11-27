package JUnitTest.WhiteTest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import JUnitTest.TestCase;

import boundary.Interface;

public class TestCaseMulti_Unexist extends TestCase {

  @Test
  public void testAdd() {
    Interface inf = new Interface();
    String result ;
    
    inf.executeOrder("x*y");
    inf.getInfo().clear();
    inf.executeOrder("!d/d a b");
    result = inf.getInfo().getResult();
    assertEquals("1.2", "变量a不存在\n只能对一个变量求导.\n", result);
  }

}
