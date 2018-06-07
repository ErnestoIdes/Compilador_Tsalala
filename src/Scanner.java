import java.util.*;

public class Scanner {
  private String source;
  private String[] lines;
  private int numberOfLines;
  private int currentLine;
  private int currentColumn;
  private final String NOVA_LINHA = "@";
  private final String FIM_DO_FICHEIRO = "#";
  private final String SEPARADOR = "(=+|>+|<+|>+|&+|\\^+|\\s+|\\(+|\\)+)";

  public Scanner(String s) {
    this.source = s;
    this.lines = s.split("\n");
    this.numberOfLines = this.lines.length;
    this.currentLine = 0;
    this.currentColumn = 0;


    for (int i = 0; i < lines.length; i++) {
      String x = lines[i].replaceAll("(\\s+|\\t+)", " ");
      x = x.replaceAll("(\\()", "$1;");
      x = x.replaceAll("(\\))", ";$1");
      System.out.println(x);
      x = x.replaceAll(SEPARADOR, ";$1;");
      x = x.replaceAll("\\s", "");
      x = x.replaceAll(";+", ";");

      x = x + ";$";
      x = x.replaceAll(";+", ";");

      this.lines[i] = x.trim();
    }
  }

  public Map<String, String> seeChar() {
    Map<String, String> m = new HashMap<String, String>();

    if (!endOfLine()) {
      String caracter = String.valueOf(this.getLine().charAt(currentColumn));
      m.put("caracter", caracter);
      m.put("linha", lineNumber());
      m.put("coluna", columnNumber());

      return m;
    } else {
      m.put("caracter", NOVA_LINHA);
      m.put("linha", lineNumber());
      m.put("coluna", columnNumber());

      return m;
    }
  }

  public Map<String, String> getChar() {
    Map<String, String> m = new HashMap<String, String>();
    if (endOfFile()) {
      m.put("caracter", FIM_DO_FICHEIRO);
      m.put("linha", lineNumber());
      m.put("coluna", columnNumber());

      return m;
    } else {
      m = seeChar();

      if (m.get("caracter").equals(NOVA_LINHA)) {
        this.currentLine = this.currentLine + 1;
        this.currentColumn = 0;
      } else {
        this.currentColumn = this.currentColumn + 1;
      }

      return m;
    }
  }

  public String getLine() {
    return lines[this.currentLine];
  }

  private boolean endOfLine() {
    return getLine().length() == currentColumn;
  }

  public boolean endOfFile() {
    return (numberOfLines == currentLine);
  }

  private String lineNumber() {
    return String.valueOf(currentLine + 1);
  }

  private String columnNumber() {
    return String.valueOf(currentColumn);
  }
}
