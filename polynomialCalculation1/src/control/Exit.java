package control;

import entity.Information;

public class Exit {
  private Information info;

  public Information getInfo() {
    return info;
  }

  public void setInfo(Information info) {
    this.info = info;
  }
  
  public void execute(){
    info.addResult("Thanks for your using!");
  }


}
