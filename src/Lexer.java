import java.util.*;

public class Lexer {
  private Scanner scanner;
  private List<String> errors;
  private String[][] table = {
    {"T_TIPO", "(int|inteiro|real|caracter|texto)"},
    {"T_PROGRAMA", "programa"},
    {"T_SE", "se"},
    {"T_SENAO", "senao"},
    {"T_FIM", "fim"},
    {"T_ENQUANTO", "enquanto"},
    {"T_ENTAO", ":"},
    {"T_ID", "[_a-zA-Z][_a-zA-Z0-9]*"},
    {"T_PARENTESES_ESQUERDA", "\\("},
    {"T_PARENTESES_DIREITA", "\\)"},
    {"T_ESPACO", "\\s"},
    {"T_OPERADOR", "(=|==|>|<|<=|>=|\\|\\||&&|\\^|!=|\\+|\\-|\\*|\\/|%)"},
    {"T_NOVALINHA", "\\$"},
    {"T_NUMERO_INTEIRO", "[0-9]+"},
    {"T_NUMERO_REAL", "([0-9]+)?\\.([0-9]+)"},
    {"T_NUMERO_REAL", "([0-9]+)\\.([0-9]+)?"},
    {"T_TEXTO", "\".*\""},
    {"T_CARACTER", "\'.*\'"},
    {"T_ERRO", ".+"},
    
    
  };

  private final String SEPARADOR = "(;|@)";

  public Lexer(String source) {
    this.scanner = new Scanner(source);
    this.errors = new ArrayList<String>();
  }

  public Token getToken() {
    String token = "";
    boolean found = true;
    String coluna = "0";

    while(!scanner.endOfFile()) {
      Map<String, String> m = scanner.getChar();

      if (found) {
         coluna = m.get("coluna");
        found = false;
      }

      if (m.get("caracter").matches(SEPARADOR)) {

        for (String[] t: table) {
          if (token.matches(t[1])) {
            if (t[0].equals("T_ERRO")) {
              errors.add(String.format("ERRO \"%s\" na linha %s, coluna %s:%s", token, m.get("linha"), coluna, m.get("coluna")));
            } else {
              errors.add(String.format("ACERTO \"%s\" na linha %s, coluna %s:%s", token, m.get("linha"), coluna, m.get("coluna")));
            }

            Token tok = new Token(token, t[0], m.get("linha"), m.get("coluna"));

            return tok;
          }
        }

        found = true;
      } else {
        token = token + m.get("caracter");
      }
    }

    Token tok = new Token("#", "T_FIM_DO_FICHEIRO", "FIM", "FIM");

    return tok;
  }

  public boolean hasToken() {
    return !scanner.endOfFile();
  }

  public List<String> getErrors() {
    return this.errors;
  }

  public boolean end() {
    return scanner.endOfFile();
  }

}
