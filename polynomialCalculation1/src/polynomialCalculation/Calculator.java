package polynomialCalculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Calculator {

  private transient Node root;
  transient Operator savedPoly;
  static Information info = new Information();

  private transient HashMap<String, Handler> handlers = new HashMap<String, Handler>();
  private transient ArrayList<String> variable = new ArrayList<String>();
  private transient boolean illegal;
  private static final int ONE = 1;
  private static final int TWO = 2;
  private static final char SPACE = ' ';
  private static final char LEFTBRACKET = '(';
  private static final char RIGHTBRACKET = ')';
  private static final char PLUS = '+';
  private static final char MULTIPLY = '*';
  private static final char EXCLAMATION = '!';

  public Calculator() {
    handlers.put("!simplify", new HandlerSimplify(this));
    handlers.put("!d/d", new HandlerDerivation(this));
    handlers.put("!exit", new HandlerExit(this));
    root = null;
    savedPoly = new Operator('+');
  }

  private Node copy(Node root) {
    Node current = null;
    if (root != null) {
      if (root instanceof Operator) {
        current = new Operator(((Operator) root).getContent());
      } else if (root instanceof Digit) {
        current = new Digit(((Digit) root).getContent());
      } else if (root instanceof Character) {
        current = new Character(((Character) root).getContent());
      }

      current.left = copy(root.left);
      current.right = copy(root.right);
    }
    return current;
  }

  private void showPrompt() {
    System.out.println();
    System.out.println("请输入指令或新的表达式：");
    System.out.println("指令由: !simplify !d/d !exit");
    System.out.println();
  }

  private StringBuffer midTravel(Node root) {
    StringBuffer sb = new StringBuffer();
    if (root != null) {
      if ((root.left == null && root.right != null) || (root.left != null && root.right == null)) {
        illegal = true;
      }

      sb.append(midTravel(root.left));
      sb.append(root.get());
      sb.append(midTravel(root.right));

    }
    return sb;

  }

  private String preTravel(Node root) {
    StringBuffer sb = new StringBuffer();
    if (root != null) {
      if ((root.left == null && root.right != null) || (root.left != null && root.right == null)) {
        illegal = true;
      }
      sb.append(root.get());
      sb.append(preTravel(root.left));
      sb.append(preTravel(root.right));
    }
    return sb.toString();

  }

  String print(Operator root) {
    StringBuffer sb = new StringBuffer();

    if (root.getContent() == PLUS) {
      int count = 0; 
      if (root.son.size() == 0) {
        sb.append("0");
      } else {
        for (Node k : root.son) {
          if (count != 0) {
            sb.append("+");
          }
          sb.append(print((Operator) k));
          count++;
        }
      }
    } else if (root.getContent() == MULTIPLY) {
      int count = 0;
      int factor = ((Digit) (root.son.get(0))).getContent();
      for (Node k : root.son) {
        if (k instanceof Digit) {
          if (factor == ONE) {
            continue;
          } else if (factor == -1) {
            sb.append("-");
          } else {
            sb.append(factor);
          }
        } else if (k instanceof Character && ((Character) k).getIndex() > 0) {
          if (count != 0) {
            sb.append("*");
          } else if (factor != 1 && factor != -1) {
            sb.append("*");
          }

          sb.append(((Character) k).getContent());
          if (((Character) k).getIndex() > ONE) {
            sb.append("^" + ((Character) k).getIndex());
          }
          count++;
        }
      }
    }
    return sb.toString();
  }

  private String debracket(String str) {
    int bracketCount = 1;
    if (str.substring(0, 1).equals("(")) {
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
      if (line[i] == SPACE) {
        continue;
      }
      if (line[i] == LEFTBRACKET) {
        bracketCount++;
      } else if (line[i] == RIGHTBRACKET) {
        bracketCount--;
      } else if (line[i] == PLUS) {
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
        if (line[i] == SPACE) {
          continue;
        }
        if (line[i] == LEFTBRACKET) {
          bracketCount++;
        } else if (line[i] == RIGHTBRACKET) {
          bracketCount--;
        } else if (line[i] == MULTIPLY) {
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
      current = new Character(line);
    }
    if (line.length > 0 && numCount == line.length) {
      int data = 0;
      for (int i = 0; i < line.length; i++) {
        data *= 10;
        data += line[i] - '0';
      }
      current = new Digit(data);
    }

    if (current instanceof Data) {
      current.left = null;
      current.right = null;
    } else if (line.length != 0) {
      String left = temp.substring(0, splitPos);
      String right = temp.substring(splitPos + 1, line.length);
      current = new Operator(line[splitPos]);

      if (left.equals("")) {
        current.left = null;
      } else {
        current.left = contributeTree(debracket(left).toCharArray());
      }

      if (right.equals("")) {
        current.right = null;
      } else {
        current.right = contributeTree(debracket(right).toCharArray());
      }

      if ((current.left == null && current.right != null)
          || (current.left != null && current.right == null)) {
        illegal = true;
      }
    }

    return current;
  }

  public char[] preprocess(char[] line) {
    ArrayList process = new ArrayList();
    if (line[0] != SPACE) {
      process.add(line[0]);
    }
    for (int i = 1; i < line.length - 1; i++) {
      if (line[i] == SPACE) {
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
    if (line[line.length - 1] != SPACE) {
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

  public void toTree(char[] line) {
    line = preprocess(line);
    root = null;
    int bracketCount = 0;

    for (int i = 0; i < line.length; i++) {
      if (line[i] == LEFTBRACKET) {
        bracketCount++;
      } else if (line[i] == RIGHTBRACKET) {
        bracketCount--;
        if (bracketCount < 0) {
          info.addMessage("括号不匹配！\n");
          illegal = true;
          return;
        }
      } else if (line[i] >= '0' && line[i] <= '9' || line[i] >= 'a' && line[i] <= 'z'
          || line[i] == '+' || line[i] == '*') {
        continue;
      } else {
        info.addMessage("非法字符：" + line[i] + "\n");
        illegal = true;
      }
    }
    if (bracketCount != 0) {
      info.addMessage("括号不匹配！\n");
      illegal = true;
    }

    if (!illegal) {
      root = contributeTree(line);
    }

  }

  private void getVar(Node root) {
    if (root instanceof Operator) {
      getVar(root.left);
      getVar(root.right);
    } else if (root instanceof Character) {
      for (Object k : variable) {
        if (((Character) root).getContent().equals((String) k)) {
          return;
        }
      }
      variable.add(((Character) root).getContent());
    }
  }

  private void unfold(Node root) {
    if (root instanceof Operator) {
      unfold(root.getLeft());
      unfold(root.getRight());

      if (((Operator) root).getContent() == '*') {
        boolean leftIsPlus = false;
        boolean rightIsPlus = false;

        if (root.getLeft() instanceof Operator
            && ((Operator) (root.getLeft())).getContent() == '+') {
          leftIsPlus = true;
        }
        if (root.getRight() instanceof Operator
            && ((Operator) root.getRight()).getContent() == '+') {
          rightIsPlus = true;
        }
        if (leftIsPlus && rightIsPlus) {
          ((Operator) root).set('+');
          Node ll = new Operator('*');
          ll.manageLeft(root.getLeft().getLeft());
          ll.manageRight(root.getRight().getLeft());
          Node lr = new Operator('*');
          lr.manageLeft(copy(root.getLeft().getLeft()));
          lr.manageRight(root.getRight().getRight());
          Node rl = new Operator('*');
          rl.manageLeft(root.getLeft().getRight());
          rl.manageRight(copy(root.getRight().getLeft()));
          Node rr = new Operator('*');
          rr.manageLeft(copy(root.getLeft().getRight()));
          rr.manageRight(copy(root.getRight().getRight()));
          root.left.manageLeft(ll);
          root.left.manageRight(lr);
          root.right.manageLeft(rl);
          root.right.manageRight(rr);
        } else if (leftIsPlus && !rightIsPlus) {
          Node right = new Operator('*');
          ((Operator) root).set('+');
          ((Operator) root.getLeft()).set('*');
          right.manageLeft(root.getLeft().getRight());
          root.getLeft().manageRight(copy(root.getRight()));
          right.manageRight(root.getRight());
          root.manageRight(right);
        } else if (!leftIsPlus && rightIsPlus) {
          Node left = new Operator('*');
          ((Operator) root).set('+');
          ((Operator) root.getRight()).set('*');
          left.manageRight(root.getRight().getLeft());
          root.getRight().manageLeft(copy(root.getLeft()));
          left.manageLeft(root.getLeft());
          root.manageLeft(left);
        }
      }

      unfold(root.getLeft());
      unfold(root.getRight());
    }

  }

  private void travelLeaf(Node root, Operator target) {
    if (root.left != null && root.right != null) {
      travelLeaf(root.left, target);
      travelLeaf(root.right, target);
    } else if (root.left == null && root.right == null) {
      if (root instanceof Digit) {
        int result;
        result = ((Digit) (target.son.get(0))).getContent() * ((Digit) root).getContent();
        ((Digit) (target.son.get(0))).set(result);
      } else if (root instanceof Character) {
        for (Node k : ((Operator) target).son) {
          if (k instanceof Character) {
            if (((Character) k).getContent().equals(((Character) root).getContent())) {
              ((Character) k).setIndex(((Character) k).getIndex() + 1);
            }
          } else if (k instanceof Digit) {
            continue;
          }

        }
      }
    }
  }

  private Node settle(Node root) {
    Operator sub = new Operator('*');
    sub.addSon(new Digit(1));

    for (Object k : variable) {
      Node leaf = new Character((String) k);
      ((Character) leaf).setIndex(0);
      sub.addSon(leaf);
    }

    travelLeaf(root, sub);
    return sub;
  }

  private void clearUp(Node root) {
    if (root instanceof Operator && ((Operator) root).getContent() == '+') {
      clearUp(root.left);
      clearUp(root.right);
    } else if (root instanceof Data
        || (root instanceof Operator && ((Operator) root).getContent() == '*')) {
      Node subNode = settle(root);
      ((Operator) savedPoly).addSon(subNode);
    }
  }

  private boolean compareMono(Operator first, Operator second) {
    boolean isSame = true;
    for (int i = 1; i < first.son.size(); i++) {
      if (((Character) (first.son.get(i))).getIndex() == ((Character) (second.son.get(i)))
          .getIndex()) {
        continue;
      } else {
        isSame = false;
        break;
      }
    }
    return isSame;
  }

  private void combine() {
    for (int i = 0; i < savedPoly.son.size(); i++) {
      for (int j = i + 1; j < savedPoly.son.size(); j++) {
        if (compareMono((Operator) savedPoly.son.get(i), (Operator) savedPoly.son.get(j))) {
          int factor = ((Digit) ((Operator) savedPoly.son.get(i)).son.get(0)).getContent();
          factor += ((Digit) ((Operator) savedPoly.son.get(j)).son.get(0)).getContent();
          ((Digit) ((Operator) savedPoly.son.get(i)).son.get(0)).set(factor);;
          savedPoly.son.remove(j);
          j--;
        }
      }
    }
  }

  String save(char[] input) {

    root = null;
    savedPoly = new Operator('+');
    variable.clear();

    toTree(input);

    if (!illegal) {
      getVar(root);
      unfold(root);
      clearUp(root);
      combine();
      return print(savedPoly);
    }
    return "";
  }

  public String simplify(String cmd) {
    ArrayList<String> input = new ArrayList<String>();
    HashMap<Integer, Integer> pair = new HashMap<Integer, Integer>();
    String[] cut = cmd.split(" ");
    boolean exist = false;

    for (int i = 0; i < cut.length; i++) {
      exist = false;
      if (!cut[i].equals("")) {
        String[] divide = cut[i].split("=");
        if (divide.length != TWO) {
          info.addMessage("赋值输入格式有误。\n");
          illegal = true;
          return null;
        } else {
          int value = 0;
          char[] data = divide[1].toCharArray();
          for (int j = 0; j < data.length; j++) {
            if (data[j] >= '0' && data[j] <= '9') {
              value *= 10;
              value += data[j] - '0';
            } else {
              info.addMessage("输入非纯数字。\n");
              illegal = true;
              return null;
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
            return "变量" + divide[0] + "不存在";
          }
        }
      }
    }

    for (Node n : savedPoly.son) {
      int factor = ((Digit) (((Operator) n).son.get(0))).getContent();
      for (Integer k : pair.keySet()) {
        Character point = (Character) ((Operator) n).son.get(k);
        for (int i = 0; i < point.getIndex(); i++) {
          factor *= (int) pair.get(k);
        }
        ((Digit) ((Operator) n).son.get(0)).set(factor);
      }
      if (factor == 0) {
        savedPoly.son.remove(n);
      } else {
        for (String k : input) {
          for (Node q : ((Operator) n).son) {
            if (q instanceof Character && k.equals(((Character) q).getContent())) {
              ((Operator) n).son.remove(q);
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
    combine();
    return print(savedPoly);

  }

  public String derivation(String cmd) {
    int varPos = 0;
    int pos = 0;
    char[] var = cmd.toCharArray();
    for (int i = 0; i < var.length; i++, varPos++) {
      if (varPos > i && var[i] != ' ') {
        info.addMessage("只能对一个变量求导.\n");
        illegal = true;
        break;
      } else if (var[i] == SPACE) {
        continue;
      } else { 
        varPos = i;
        for (varPos = i; varPos < var.length; varPos++) {
          if (var[varPos] == SPACE) {
            break;
          }
        }
        String cut = new String(var).substring(i, varPos);
        illegal = true;
        for (String k : variable) {
          if (!(k.equals(cut))) {
            continue;
          } else {
            pos = variable.indexOf(k);
            pos++;
            illegal = false;
            break;
          }
        }
        if(illegal){
          info.addMessage("变量"+cut+"不存在");
        }
        break;
      }
    }

    if (!illegal) {
      for (int i = 0; i < savedPoly.son.size(); i++) {
        Node root = savedPoly.son.get(i);
        int factor = ((Digit) (((Operator) root).son.get(0))).getContent();
        int index = ((Character) (((Operator) root).son.get(pos))).getIndex();
        factor *= index;
        ((Character) (((Operator) root).son.get(pos))).setIndex(index - 1);
        ((Digit) (((Operator) root).son.get(0))).set(factor);

        if (factor == 0) {
          savedPoly.son.remove(root);
          i--;
        }

      }
      combine();
      return print(savedPoly);
    }
    illegal = false;
    return null;
  }

  public void doCalculation(String line) {
    line = line.toLowerCase();
    char[] input = line.toCharArray();

    if (input.length == 0) {
      info.addMessage("无输入。\n");
      illegal = true;
    } else if (input[0] != EXCLAMATION) {
      info.addMessage(save(input));
    } else {
      Handler handler = null;

      if (line.length() > 4 && line.subSequence(0, 4).equals("!d/d")) {
        handler = handlers.get("!d/d");
      } else if (line.length() > 4 && line.subSequence(0, 5).equals("!exit")) {
        handler = handlers.get("!exit");
      } else if (line.length() > 9 && line.subSequence(0, 9).equals("!simplify")) {
        handler = handlers.get("!simplify");
      } else {
        info.addMessage("输入指令有误。\n");
        illegal = true;
      }

      if (handler != null) {
        if (handler.isExit()) {
          System.exit(0);;
        } else if (root != null) {
          info.addMessage(handler.doCmd(line));
        } else {
          info.addMessage("格式错误！");
        }
      } else {
        info.addMessage("Error,illeagal input!");
      }
    }

    if (illegal) {
      info.addMessage("不合法输入！");
    }
    info.say();
  }

  public void calculate() {
    Scanner scan = new Scanner(System.in);
    while (true) {
      info.setMessage("");
      showPrompt();
      String line = scan.nextLine();
      doCalculation(line);
      info.say();
      illegal = false;
    }
  }

  public static void main(String[] args) {

    Calculator cal = new Calculator();
    cal.calculate();
    System.out.println("Bye");
  }

}
