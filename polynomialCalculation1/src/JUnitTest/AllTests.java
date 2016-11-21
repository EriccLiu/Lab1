package JUnitTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import JUnitTest.WhiteTest.TestCaseMulti_Unexist;
import JUnitTest.WhiteTest.TestCaseRight;

@RunWith(Suite.class)
@SuiteClasses({
  TestCaseMulti_Unexist.class,
  TestCaseRight.class
  })
 
public class AllTests {
  //the class remain empty
  //used only as a holder for the above annotations
}
