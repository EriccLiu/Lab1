package control;

import entity.Information;

public class Simplify {
  private Information info;

  public Information getInfo() {
    return info;
  }

  public void setInfo(Information info) {
    this.info = info;
  }

  public void execute() {
//    System.out.println(info.getCmdType());
//    System.out.println(info.getInput());

    info.getTree().simplify(info.getInput());
    if (!info.isIllegal()) {
      info.getTree().combine();
      info.addResult(info.getTree().printAsString());
    }
  }
}
