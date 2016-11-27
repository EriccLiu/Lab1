package control;

import entity.Information;

public class Error {
  private Information info;

  public Information getInfo() {
    return info;
  }

  public void setInfo(Information info) {
    this.info = info;
  }
  
  public void execute(){
//    System.out.println(info.getCmdType());
//    System.out.println(info.getInput());
    info.addResult("wrong order,please input again.");
  }
}
