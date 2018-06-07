public class Token {
  private String classificacao;
  private String cadeia;
  private String linha;
  private String coluna;

  public enum Type {
      PROGRAMA,
      FIM,
      TIPO,
      LITERAL,
      SE,
      SENAO,
      ATRIBUICAO,
      OPERADOR_BINARIO,
      OPERADOR_UNARIO,
      NOVA_LINA
  }

  public Token(String cadeia, String classificacao, String linha, String coluna) {
    this.cadeia = cadeia;
    this.classificacao = classificacao;
    this.linha = linha;
    this.coluna = coluna;
  }

  public String getCadeia() {
    return this.cadeia;
  }

  public String getLinha() {
    return this.linha;
  }

  public String getColuna() {
    return this.coluna;
  }

  public boolean equals(Object t) {
    if (t instanceof Token) {
      Token tok = (Token)t;
      return this.classificacao.equals(tok.toString());
    }

    return false;
  }

  public String getClassificacao() {
    return this.classificacao;
  }

  public String toString() {
    return String.format("%s --> %s, L:%s, C:%s", this.classificacao, this.cadeia, this.linha, this.coluna);
  }

  public boolean is(String s) {
    return classificacao == s;
  }
}
