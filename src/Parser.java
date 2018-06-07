import java.util.*;

public class Parser {
  private Lexer lexer;
  private Token token;
  private List<String> erros;
  private String ERRO_ID = "Erro esperava encontrar %s, encontrou %s";

  public Parser(String source) {
    this.lexer = new Lexer(source);
    this.erros = new ArrayList<String>();
  }

  public List<String> getErros() {
    return this.erros;
  }

  private void programa() {
    next();

    if (is("T_PROGRAMA")) {
      next();

      if (is("T_ID")) {
        next();
        if (is("T_NOVALINHA")) {
          this.corpo();
          this.fim();
        } else {
          this.error(ErroSintatico.NOVA_LINHA);
          this.sync();
          this.corpo();
          this.fim();
        }
      } else {
        this.error(ErroSintatico.ID);
        this.sync();

        this.corpo();
        this.fim();
      }
    } else {
      this.error(ErroSintatico.PROGRAMA);
      this.sync();

      this.corpo();
      this.fim();
    }
  }

  private void next() {
    token = lexer.getToken();
  }

  private boolean is(String s) {
    return token.is(s);
  }

  private void error(String expected) {
    String s;
    if (expected == ErroSintatico.EXPRESSAO && token.getClassificacao() == "T_NOVALINHA") {
      expected = "Esperava encontrar uma expressão válida, chegou ao final da linha [L: %s, C: %s]";
      s = String.format("ERRO: " + expected, token.getLinha(), token.getColuna());
    } else if (expected == ErroSintatico.ID && token.getClassificacao() == "T_TIPO") {
      s = String.format("ERRO: \"%s\" É tipo de dado e por isso não pode ser identificador [L: %s, C: %s]", token.getCadeia(), token.getLinha(), token.getColuna());
    } else if (token.getClassificacao() == "T_NOVALINHA") {
      String cadeia = "\\n";
      s = String.format("ERRO: " + expected, cadeia, token.getLinha(), token.getColuna());
    } else {
      s = String.format("ERRO: " + expected, token.getCadeia(), token.getLinha(), token.getColuna());
    }

    System.out.println(s);
    this.erros.add(s);
  }

  private void sync() {
    while(!is("T_NOVALINHA")) {
      next();
    }
  }

  private void corpo() {
    next();

    if (is("T_TIPO")) {
      next();
      if (is("T_ID")) {
        declaracao();
      } else {
        this.error(ErroSintatico.ID);
        this.sync();
        this.corpo();
      }
    } else if (is("T_ID") || is("T_NUMERO_INTEIRO") || is("T_NUMERO_REAL") || is("T_TEXTO")) {
      this.next();
      if(is("T_OPERADOR")) {
        this.next();
        this.xp();
        this.next();
      } else {
        this.error(ErroSintatico.OPERADOR);
        this.sync();
        this.corpo();
        this.next();
      }
    } else if(is("T_ENQUANTO")) {
    	next();
      this.enquanto();
      this.next();
     }else if(is("T_ENTAO")) {
       this.entao();
          //sthis.next();
     //} else {if (is("T_SENAO")) {
    	// this.senao();
    	 //this.next();
   // } else if (is("T_FIM")) {
     // this.fim();
   // } else if(is("T_SE")) {
     // this.se();
     // this.next();
     //}else if(is("T_ENTAO")) {
       //   this.entao();
         // this.next();
    
    } /*else*/ else if (is("T_FIM")) {
      this.fim();
    } 
    else if (is("T_ERRO")) {
      this.error(ErroSintatico.ID_OU_TIPO);
      this.sync();
      this.corpo();
    } else {
      return;
    }
  }

  public void parse() {
    this.programa();
  }

  public void fim() {
    if (is("T_FIM")) {
      return;
    } else {
      this.error(ErroSintatico.FIM);
    }
  }

  public void declaracao() {
    if (is("T_ID")) {
      next();
      if (is("T_NOVALINHA")) {
        System.out.println("DECLARACAO");
        this.corpo();
      } else if (is("T_OPERADOR")) {
        System.out.println("DECLARACAO + INICIALIZACAO");
        next();
        this.xp();
        this.corpo();
      } else {
        this.error(ErroSintatico.NOVA_LINHA);
        this.sync();
        this.corpo();
      }
    } else {
      this.error(ErroSintatico.ID);
      this.sync();
      this.corpo();
    }
  }

  public void expressao() {
    if (is("T_PARENTESES_ESQUERDA")) {
      next();
      this.expressao();
      next();
      if (is("T_PARENTESES_DIREITA")) {
        this.xp();
      } else {
        this.error(ErroSintatico.PARENTESES_DA_DIREITA);
        this.sync();
        return;
      }
    } if (is("T_ID") || is("T_NUMERO_INTEIRO") || is("T_NUMERO_REAL") || is("T_TEXTO")) {
      next();

      if (is("T_OPERADOR")) {
        next();
        this.expressao();
      } else {
        this.error(ErroSintatico.OPERADOR_OU_NOVALINHA);
        this.sync();
        return;
      }
    } else if (is("T_NOVALINHA")) {
      return;
    } else {
      this.error(ErroSintatico.ESTRANHO);
      this.sync();
      return;
    }
  }

  public void xp() {
    if (is("T_PARENTESES_ESQUERDA")) {
      next();
      this.xp();
      //next();
      if (is("T_PARENTESES_DIREITA")) {
        next();
        if (is("T_ENTAO")) {
          return;
        } else if (is("T_OPERADOR")) {
          next();
          this.xp();
        } else {
          this.error(ErroSintatico.ESTRANHO);
          this.sync();
        }
      } else {
        this.error(ErroSintatico.PARENTESES_DA_DIREITA);
        this.sync();
      }
    } else if (is("T_ID") || is("T_TEXTO") || is("T_NUMERO_REAL") || is("T_NUMERO_INTEIRO")) {
      next();

      if (is("T_OPERADOR")) {
        next();
        this.xp();
      } else if (is("T_NOVALINHA") || is("T_PARENTESES_DIREITA")) {
        return;
      } else {
        this.error(ErroSintatico.OPERADOR_OU_NOVALINHA);
        this.sync();
      }
    } else {
      this.error(ErroSintatico.EXPRESSAO);
      this.sync();
    }
  }

  public void enquanto() {
  //  next();
    this.xp();
    this.corpo();
    //this.next();
  }
  
  public void senao (){
	  //next();
	  this.corpo();
	  //this.error(ErroSintatico.OPERADOR_OU_NOVALINHA);
  }
  public void entao(){
	  //next();
	  this.corpo();
	 // this.error(ErroSintatico.OPERADOR_OU_NOVALINHA);
	  
  }
  public void corpose() {
    this.corpo();
  }

}
