package boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import control.*;
import control.Error;
import entity.Information;

public class Interface {
  /**
   * 与后台传递的信息.
   */
  private Information info = new Information();

  public Information getInfo() {
    return info;
  }
  public void setInfo(Information info) {
    this.info = info;
  }

  /**
   * 显示提示信息.
   */
  private void showPrompt() {
    System.out.println();
    System.out.println("请输入指令或新的表达式：");
    System.out.println("指令由: !simplify !d/d !exit");
    System.out.println();
  }
  
  /**
   * 获取控制台的指令.
   * 传给相应的控制类.
   * @param order.
   */
  public void executeOrder(String order) {
    order = order.toLowerCase();
    if(order.length()>=1&&order.charAt(0)=='!'){
      if(order.length()>=4&&order.substring(0, 4).equals("!d/d")){
        Derivative derivative = new Derivative();
        info.setCmdType("derivative");
        info.setInput(order.substring(4));
        derivative.setInfo(info);
        derivative.execute();
        info = derivative.getInfo();
      }else if(order.length()>=5&&order.substring(0, 5).equals("!exit")){
        Exit exit = new Exit();
        info.setCmdType("exit");
        info.setInput("");
        exit.setInfo(info);
        exit.execute();
        info = exit.getInfo();
      }else if(order.length()>=9&&order.substring(0, 9).equals("!simplify")){
        Simplify simplify = new Simplify();
        info.setCmdType("simplify");
        info.setInput(order.substring(9));
        simplify.setInfo(info);
        simplify.execute();
        info = simplify.getInfo();
      }else{
        Error error = new Error();
        info.setCmdType("error");
        info.setInput(order);
        error.setInfo(info);
        error.execute();
        info = error.getInfo();
      }
    }else{
      Expression expression = new Expression();
      info.setCmdType("expression");
      info.setInput(order);
      expression.setInfo(info);
      expression.execute();
      info = expression.getInfo();
    }
  }
  
  public void execute(){
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String input = null;
    while(info.getCmdType()==null||!(info.getCmdType().equals("exit"))){
      try{
        showPrompt();
        input= br.readLine();
        executeOrder(input);
        info.sayResult();
        info.clear();
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String args[]){
    Interface inf = new Interface();
    inf.execute();
  }
}
