import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Editor {
  private javax.swing.JFrame janela;

  public Editor() {
      this.janela = new javax.swing.JFrame("Tsala Language");
      janela.setTitle("IDE Tsalala");
  }

  public void prepararJanela() {
    this.janela.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    this.janela.pack();
    this.janela.setSize(800, 600);


    javax.swing.JPanel painel = new javax.swing.JPanel(new java.awt.BorderLayout());
    javax.swing.JButton b = new javax.swing.JButton("RUN");
    b.setBackground(Color.DARK_GRAY);
    b.setIcon(new ImageIcon(Editor.class.getResource("/img/run.png")));
    final javax.swing.JTextArea t = new javax.swing.JTextArea(20, 10);
    t.setTabSize(2);
    t.setLineWrap(true);
    t.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 40, 10, 40));
    java.awt.Font font = new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 16);
    t.setFont(font);
    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(t);
    //scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    final javax.swing.JTextArea t2 = new javax.swing.JTextArea(10, 10);
    t2.setTabSize(2);
    t2.setLineWrap(true);
    t2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 40, 10, 40));
    t2.setFont(font);
    javax.swing.JScrollPane scroll2 = new javax.swing.JScrollPane(t2);
    //scroll2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String s = t.getText();

        Parser p = new Parser(s);
        p.parse();

        int nErros = p.getErros().size();
        if (nErros == 0) {
          t2.setText("Compilado com sucesso\n");
        } else {
          t2.setText("==== Compilação completa ===\n");
          String contadorDeErros;
          if (nErros == 1) {
            contadorDeErros = "%s erro encontrado\n";
          } else {
            contadorDeErros = "%s erros encontrados\n";
          }

          t2.setText(String.format(contadorDeErros, nErros));
          for (String t: p.getErros()) {
            String erros = t2.getText() + String.format("%s\n", t);
            t2.setText(erros);
          }
        }
      }
    });
    painel.add(b, java.awt.BorderLayout.SOUTH);
    painel.add(scroll, java.awt.BorderLayout.NORTH);
    painel.add(scroll2, java.awt.BorderLayout.CENTER);

    this.janela.getContentPane().add(painel);

    this.janela.setVisible(true);
  }

  public static void main(String[] args) {
// 	  try {
//           for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//               if ("Windows".equals(info.getName())) {
//                   javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                   break;
//               }
//           }
//       } catch (ClassNotFoundException|IllegalAccessException |javax.swing.UnsupportedLookAndFeelException
//     		  | InstantiationException ex) {
//           System.out.println("Houve um grande BUG big brother");
//       }
    Editor e = new Editor();
    e.prepararJanela();
  }
}
