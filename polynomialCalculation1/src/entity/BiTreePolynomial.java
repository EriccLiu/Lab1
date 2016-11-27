package entity;

import java.util.ArrayList;
import entity.dataStructure.*;

public class BiTreePolynomial {
  private Information info;
  private Node root = null;
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

  public Node getRoot() {
    return root;
  }

  public ArrayList<String> getVariable() {
    return variable;
  }

  /**
   * 复制一个二叉树.
   * 
   * @param root
   * @return
   */
  private Node copy(Node root) {
    Node current = null;
    if (root != null) {
      if (root instanceof Operator) {
        char ope = ((Operator) root).getAsString().toCharArray()[0];
        current = new Operator(ope);
      } else if (root instanceof Digit) {
        int digit = ((Digit) root).getDigit();
        current = new Digit(digit);
      } else if (root instanceof Variable) {
        String variable = ((Variable) root).getAsString();
        current = new Variable(variable);
      }

      current.setLeft(copy(root.getLeft()));
      current.setRight(copy(root.getRight()));
    }
    return current;
  }

  /**
   * 获取变量名并保存到列表中的递归算法.
   * 
   * @param root
   */
  private void getVar(Node root) {
    if (root instanceof Operator) {
      getVar(root.getLeft());
      getVar(root.getRight());
    } else if (root instanceof Variable) {
      for (Object k : variable) {
        if (((Variable) root).getAsString().equals((String) k)) {
          return;
        }
      }
      variable.add(((Variable) root).getAsString());
    }
  }

  /**
   * 对输入字符数组进行预处理，去掉无用符号等.
   * 
   * @param line
   * @return
   */
  private char[] preprocess(char[] line) {
    if(line.length==0){
      return line;
    }
    ArrayList process = new ArrayList();
    if (line[0] != ' ') {
      process.add(line[0]);
    }
    for (int i = 1; i < line.length - 1; i++) {
      if (line[i] == ' ') {
        if ((line[i - 1] >= '0' && line[i - 1] <= '9')
            && (line[i + 1] >= 'a' && line[i + 1] <= 'z')) {
          line[i] = '*';
        } else if ((line[i - 1] >= 'a' && line[i - 1] <= 'z')
            && (line[i + 1] >= '0' && line[i + 1] <= '9')) {
          line[i] = '*';
        } else if ((line[i - 1] >= 'a' && line[i - 1] <= 'z')
            && (line[i + 1] >= 'a' && line[i + 1] <= 'z')) {
          line[i] = '*';
        } else {
          continue;
        }
      } else if ((line[i] >= 'a' && line[i] <= 'z') && (line[i - 1] >= '0' && line[i - 1] <= '9')) {
        process.add('*');
      }
      process.add(line[i]);
    }
    if (line[line.length - 1] != ' ') {
      if ((line[line.length - 1] >= 'a' && line[line.length - 1] <= 'z')
          && (line[line.length - 2] >= '0' && line[line.length - 2] <= '9')) {
        process.add('*');
      }
      if (line.length != 1) {
        process.add(line[line.length - 1]);
      }
    }

    char[] changed = new char[process.size()];
    for (int i = 0; i < process.size(); i++) {
      changed[i] = (char) process.get(i);
    }
    return changed;
  }

  /**
   * 去字符串两端的匹配括号.
   * 
   * @param str
   * @return
   */
  private String debracket(String str) {
    int bracketCount = 1;
    if (str.length()>=1&&str.substring(0, 1).equals("(")) {
      for (int i = 1; i < str.length(); i++) {
        if (str.substring(i, i + 1).equals("(")) {
          bracketCount++;
        } else if (str.substring(i, i + 1).equals(")")) {
          bracketCount--;
        }

        if (bracketCount == 0) {
          if (i == str.length() - 1) {
            str = str.substring(1, str.length() - 1);
          } else {
            break;
          }
        }
      }
    }
    return str;
  }

  private Node contributeTree(char[] line) {

    Node current = null;
    int bracketCount = 0;
    int numCount = 0;
    int chCount = 0;
    int splitPos = 0;
    String temp = new String(line);

    for (int i = 0; i < line.length; i++) {
      if (line[i] == ' ') {
        continue;
      }
      if (line[i] == '(') {
        bracketCount++;
      } else if (line[i] == ')') {
        bracketCount--;
      } else if (line[i] == '+') {
        if (bracketCount == 0) {
          splitPos = i;
          break;
        }
      } else if (line[i] >= '0' && line[i] <= '9') {
        numCount++;
      } else if (line[i] >= 'a' && line[i] <= 'z') {
        chCount++;
      }
    }
    if (splitPos == 0 && line[0] != '+') {
      for (int i = 0; i < line.length; i++) {
        if (line[i] == ' ') {
          continue;
        }
        if (line[i] == '(') {
          bracketCount++;
        } else if (line[i] == ')') {
          bracketCount--;
        } else if (line[i] == '*') {
          if (bracketCount == 0) {
            splitPos = i;
            break;
          }
        } else {
          continue;
        }
      }
    }
    if (line.length > 0 && chCount == line.length) {
      current = new Variable(new String(line));
    }
    if (line.length > 0 && numCount == line.length) {
      int data = 0;
      for (int i = 0; i < line.length; i++) {
        data *= 10;
        data += line[i] - '0';
      }
      current = new Digit(data);
    }

    if (current instanceof Digit || current instanceof Variable) {
      current.setLeft(null);
      current.setRight(null);
    } else if (line.length != 0) {
      String left = temp.substring(0, splitPos);
      String right = temp.substring(splitPos + 1, line.length);
      current = new Operator(line[splitPos]);

      left = debracket(left);
      if (left.equals("")) {
        current.setLeft(null);
      } else {
        current.setLeft(contributeTree(left.toCharArray()));
      }

      right = debracket(right);
      if (right.equals("")) {
        current.setRight(null);
      } else {
        current.setRight(contributeTree(right.toCharArray()));
      }

      if ((current.getLeft() == null && current.getRight() != null)
          || (current.getLeft() != null && current.getRight() == null)) {
        info.setIllegal(true);
      }
    }

    return current;
  }

  /**
   * 对二叉树进行拆括号的递归算法.
   * 
   * @param root
   */
  private void unfold(Node root) {
    if (root instanceof Operator) {
      unfold(root.getLeft());
      unfold(root.getRight());

      if (((Operator) root).getAsString().equals("*")) {
        boolean leftIsPlus = false;
        boolean rightIsPlus = false;

        if (root.getLeft() instanceof Operator
            && ((Operator) (root.getLeft())).getAsString().equals("+")) {
          leftIsPlus = true;
        }
        if (root.getRight() instanceof Operator
            && ((Operator) (root.getRight())).getAsString().equals("+")) {
          rightIsPlus = true;
        }
        if (leftIsPlus && rightIsPlus) {
          ((Operator) root).setOperator('+');
          Node ll = new Operator('*');
          ll.setLeft(root.getLeft().getLeft());
          ll.setRight(root.getRight().getLeft());
          Node lr = new Operator('*');
          lr.setLeft(copy(root.getLeft().getLeft()));
          lr.setRight(root.getRight().getRight());
          Node rl = new Operator('*');
          rl.setLeft(root.getLeft().getRight());
          rl.setRight(copy(root.getRight().getLeft()));
          Node rr = new Operator('*');
          rr.setLeft(copy(root.getLeft().getRight()));
          rr.setRight(copy(root.getRight().getRight()));
          root.getLeft().setLeft(ll);
          root.getLeft().setRight(lr);
          root.getRight().setLeft(rl);
          root.getRight().setRight(rr);
        } else if (leftIsPlus && !rightIsPlus) {
          Node right = new Operator('*');
          ((Operator) root).setOperator('+');
          ((Operator) root.getLeft()).setOperator('*');
          right.setLeft(root.getLeft().getRight());
          root.getLeft().setRight(copy(root.getRight()));
          right.setRight(root.getRight());
          root.setRight(right);
        } else if (!leftIsPlus && rightIsPlus) {
          Node left = new Operator('*');
          ((Operator) root).setOperator('+');
          ((Operator) root.getRight()).setOperator('*');
          left.setRight(root.getRight().getLeft());
          root.getRight().setLeft(copy(root.getLeft()));
          left.setLeft(root.getLeft());
          root.setLeft(left);
        }
      }

      unfold(root.getLeft());
      unfold(root.getRight());
    }

  }

  /**
   * 获取变量名并保存到列表中. 用于外部调用.
   */
  public void getVariableList() {
    getVar(root);
  }

  /**
   * 将字符数组转化为二叉树. 先预处理. 然后处理括号. 然后用递归算法建树.
   * 
   * @param line
   */
  public void toTree(char[] line) {
    line = preprocess(line);
    if(line.length==0){
      info.setIllegal(true);
      info.addResult("无输入！");
      return;
    }
    
    root = null;
    int bracketCount = 0;
    for (int i = 0; i < line.length; i++) {
      if (line[i] == '(') {
        bracketCount++;
      } else if (line[i] == ')') {
        bracketCount--;
        if (bracketCount < 0) {
          info.addResult("括号不匹配！");
          info.setIllegal(true);
          return;
        }
      } else if (line[i] >= '0' && line[i] <= '9' || line[i] >= 'a' && line[i] <= 'z'
          || line[i] == '+' || line[i] == '*') {
        continue;
      } else {
        info.addResult("非法字符：" + line[i]);
        info.setIllegal(true);
      }
    }
    if (bracketCount != 0) {
      info.addResult("括号不匹配！");
      info.setIllegal(true);
    }

    if (!info.isIllegal()) {
      root = contributeTree(line);
    }

  }

  /**
   * 对二叉树进行拆括号的递归算法. 用于外部调用.
   */
  public void unfoldBiTree() {
    unfold(root);
  }



}
