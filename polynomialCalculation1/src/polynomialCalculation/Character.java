package polynomialCalculation;

public class Character extends Data {

  private String word;
  private int index;

  public Character(final String word) {
    this.word = new String(word);
    index = 1;
  }

  public Character(final char[] word) {
    this.word = new String(word);
    index = 1;
  }

  public String getContent() {
    return word;
  }

  public int getIndex() {
    return index;
  }

  public void set(final String word) {
    this.word = new String(word);
  }

  public void setIndex(final int index) {
    this.index = index;
  }

  @Override
  public char[] get() {
    return word.toCharArray();
  }

}
