package entity;

import java.util.ArrayList;
import java.util.HashMap;

import entity.dataStructure.Digit;
import entity.dataStructure.Node;
import entity.dataStructure.Operator;
import entity.dataStructure.Variable;

public class TreePolynomial {
  private Information info;
  private Operator root = new Operator('+');
  /**
   * 用于保存变量名与顺序.
   */
  private transient ArrayList<String> variable = new ArrayList<String>();

  public Information getInfo() {
    return info;
  }

  public void setInfo(Information info) {
    this.info = info;
  }

  public void setVariable(ArrayList<String> variable) {
    this.variable = variable;
  }

  /**
   * 化简单项式.
   * 将数字乘到一起，将同名变量以指数形式存储.
   * @param root
   * @param target
   */
  private void travelLeaf(Node root, Operator target) {
    if (root.getLeft() != null && root.getRight() != null) {
      travelLeaf(root.getLeft(), target);
      travelLeaf(root.getRight(), target);
    } else if (root.getLeft() == null && root.getRight() == null) {
      if (root instanceof Digit) {
        int result;
        result = ((Digit) (target.getSon().get(0))).getDigit() * ((Digit) root).getDigit();
        ((Digit) (target.getSon().get(0))).setDigit(result);
      } else if (root instanceof Variable) {
        for (Node k : ((Operator) target).getSon()) {
          if (k instanceof Variable) {
            if (((Variable) k).getAsString().equals(((Variable) root).getAsString())) {
              ((Variable) k).setIndex(((Variable) k).getIndex() + 1);
            }
          } else if (k instanceof Digit) {
            continue;
          }

        }
      }
    }
  }

  /**
   * 建立单项式结点的框架，并调用travelLeaf函数添加内容.
   * @param root
   * @return
   */
  private Node settle(Node root) {
    Operator sub = new Operator('*');
    sub.addSon(new Digit(1));

    for (Object k : variable) {
      Node leaf = new Variable((String) k);
      ((Variable) leaf).setIndex(0);
      sub.addSon(leaf);
    }

    travelLeaf(root, sub);
    return sub;
  }

  /**
   * 比较两结点单项式是否为同类型.
   * 
   * @param first
   * @param second
   * @return
   */
  private boolean compareMono(Operator first, Operator second) {
    boolean isSame = true;
    for (int i = 1; i < first.getSon().size(); i++) {
      if (((Variable) (first.getSon().get(i))).getIndex() == ((Variable) (second.getSon().get(i)))
          .getIndex()) {
        continue;
      } else {
        isSame = false;
        break;
      }
    }
    return isSame;
  }

  /**
   * 以字符串格式表示多项式的递归算法.
   * 
   * @param root
   * @return
   */
  private String print(Operator root) {
    StringBuffer sb = new StringBuffer();

    if (root.getOperator() == '+') {
      int count = 0;
      if (root.getSon().size() == 0) {
        sb.append("0");
      } else {
        for (Node k : root.getSon()) {
          if (count != 0) {
            sb.append("+");
          }
          sb.append(print((Operator) k));
          count++;
        }
      }
    } else if (root.getOperator() == '*') {
      int count = 0;
      int factor = ((Digit) (root.getSon().get(0))).getDigit();
      for (Node k : root.getSon()) {
        if (k instanceof Digit) {
          if (factor == 1) {
            continue;
          } else if (factor == -1) {
            sb.append("-");
          } else {
            sb.append(factor);
          }
        } else if (k instanceof Variable && ((Variable) k).getIndex() > 0) {
          if (count != 0) {
            sb.append("*");
          } else if (factor != 1 && factor != -1) {
            sb.append("*");
          }

          sb.append(((Variable) k).getAsString());
          if (((Variable) k).getIndex() > 1) {
            sb.append("^" + ((Variable) k).getIndex());
          }
          count++;
        }
      }
    }
    return sb.toString();
  }

  /**
   * 向表达式树根结点中添加化简后的单项式.
   * @param root
   */
  public void clearUp(Node root) {
    if (root instanceof Operator && ((Operator) root).getOperator() == '+') {
      clearUp(root.getLeft());
      clearUp(root.getRight());
    } else if ((root instanceof Digit || root instanceof Variable)
        || (root instanceof Operator && ((Operator) root).getOperator() == '*')) {
      Node subNode = settle(root);
      ((Operator) this.root).addSon(subNode);
    }
  }

  /**
   * 合并同类项.
   */
  public void combine() {
    for (int i = 0; i < root.getSon().size(); i++) {
      for (int j = i + 1; j < root.getSon().size(); j++) {
        if (compareMono((Operator) root.getSon().get(i), (Operator) root.getSon().get(j))) {
          int factor = ((Digit) ((Operator) root.getSon().get(i)).getSon().get(0)).getDigit();
          factor += ((Digit) ((Operator) root.getSon().get(j)).getSon().get(0)).getDigit();
          ((Digit) ((Operator) root.getSon().get(i)).getSon().get(0)).setDigit(factor);
          root.getSon().remove(j);
          j--;
        }
      }
    }
  }

  public String printAsString() {
    return print(root);
  }

  /**
   * 赋值化简多项式.
   * 
   * @param cmd
   */
  public void simplify(String cmd) {
    ArrayList<String> input = new ArrayList<String>();
    HashMap<Integer, Integer> pair = new HashMap<Integer, Integer>();
    String[] cut = cmd.split(" ");
    boolean exist = false;

    for (int i = 0; i < cut.length; i++) {
      exist = false;
      if (!cut[i].equals("")) {
        String[] divide = cut[i].split("=");
        if (divide.length != 2) {
          info.addResult("赋值输入格式有误。");
          info.setIllegal(true);
          return;
        } else {
          int value = 0;
          char[] data = divide[1].toCharArray();
          for (int j = 0; j < data.length; j++) {
            if (data[j] >= '0' && data[j] <= '9') {
              value *= 10;
              value += data[j] - '0';
            } else {
              info.addResult("输入非纯数字。");
              info.setIllegal(true);;
              return;
            }
          }

          for (String k : variable) {
            if (k.equals(divide[0])) {
              exist = true;
              input.add(k);
              pair.put(variable.indexOf(k) + 1, value);
              break;
            }
          }
          if (!exist) {
            info.addResult("变量" + divide[0] + "不存在");
            info.setIllegal(true);
            return;
          }
        }
      }
    }

    for (Node n : this.root.getSon()) {
      int factor = ((Digit) (((Operator) n).getSon().get(0))).getDigit();
      for (Integer k : pair.keySet()) {
        Variable point = (Variable) ((Operator) n).getSon().get(k);
        for (int i = 0; i < point.getIndex(); i++) {
          factor *= (int) pair.get(k);
        }
        ((Digit) ((Operator) n).getSon().get(0)).setDigit(factor);
      }
      if (factor == 0) {
        this.root.getSon().remove(n);
      } else {
        for (String k : input) {
          for (Node q : ((Operator) n).getSon()) {
            if (q instanceof Variable && k.equals(((Variable) q).getAsString())) {
              ((Operator) n).getSon().remove(q);
              break;
            }
          }
        }
      }
    }

    for (String k : input) {
      for (String p : variable) {
        if (k.equals(p)) {
          variable.remove(p);
          break;
        }
      }
    }
  }

  /**
   * 对多项式树进行求导.
   * 
   * @param cmd
   */
  public void derivative(String cmd) {
    int varPos = 0;
    int pos = 0;
    char[] var = cmd.toCharArray();
    String result = "";
    for (int i = 0; i < var.length; i++, varPos++) {
      if (varPos > i && var[i] != ' ') {
        info.addResult("只能对一个变量求导.");
        info.setIllegal(true);
        break;
      } else if (var[i] == ' ') {
        continue;
      } else {
        varPos = i;
        for (varPos = i; varPos < var.length; varPos++) {
          if (var[varPos] == ' ') {
            break;
          }
        }
        String cut = new String(var).substring(i, varPos);
        info.setIllegal(true);
        for (String k : variable) {
          if (!(k.equals(cut))) {
            continue;
          } else {
            pos = variable.indexOf(k);
            pos++;
            info.setIllegal(false);
            break;
          }
        }
        if (info.isIllegal()) {
          info.addResult("变量" + cut + "不存在");
        }

      }
    }

    if (!info.isIllegal()) {
      for (int i = 0; i < this.root.getSon().size(); i++) {
        Node root = this.root.getSon().get(i);
        int factor = ((Digit) (((Operator) root).getSon().get(0))).getDigit();
        int index = ((Variable) (((Operator) root).getSon().get(pos))).getIndex();
        factor *= index;
        ((Variable) (((Operator) root).getSon().get(pos))).setIndex(index - 1);
        ((Digit) (((Operator) root).getSon().get(0))).setDigit(factor);

        if (factor == 0) {
          this.root.getSon().remove(root);
          i--;
        }

      }
    }
  }

}
