package control;

import entity.*;

public class Expression {
  private Information info;
  private TreePolynomial TPolynomial = new TreePolynomial();
  
  public Information getInfo() {
    return info;
  }

  public void setInfo(Information info) {
    this.info = info;
  }
  
  public TreePolynomial getTPolynomial() {
    return TPolynomial;
  }

  public void execute(){
//    System.out.println("cmd:"+info.getCmdType());
//    System.out.println("input:"+info.getInput());
    
    BiTreePolynomial BTPolynomial = new BiTreePolynomial();
    BTPolynomial.setInfo(info);
    TPolynomial.setInfo(info);
    
    BTPolynomial.toTree(info.getInput().toCharArray());
    if(!info.isIllegal()){
      BTPolynomial.getVariableList();
      BTPolynomial.unfoldBiTree();
      TPolynomial.setVariable(BTPolynomial.getVariable());
      TPolynomial.clearUp(BTPolynomial.getRoot());
      TPolynomial.combine();
      info.setTree(TPolynomial);
      info.addResult(TPolynomial.printAsString());
    }else{
      info.addResult("不合法输入！");
    }
  }
}
