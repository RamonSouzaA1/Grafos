/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import model.Aresta;
import model.Grafo;
import Storage.GraphSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.No;

/**
 *
 * @author KAIO, VITIN, LUCAS, RAMON
 */
public class ExibirGrafo extends javax.swing.JFrame {

    Grafo grafo = new Grafo();

    public ExibirGrafo() {
        initComponents();
        this.grafo = GraphSession.getGrafo();
        montaGrafo();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(452, 452, 452))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExibirGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExibirGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExibirGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExibirGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExibirGrafo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    public void montaGrafo() {
        try {
            Process p;
            File arquivo = new File("src\\fileDot\\fileS.dot");
            List<Aresta> listaAresta = grafo.getArestas();
            List<No> listaNos = grafo.getNos();

            String digraph_text = "";
            if (grafo.getTipo().equals("undirected")) {
                digraph_text = "graph G {";
            } else {
                digraph_text = "digraph G {";
            }
            for (int i = 0; i < listaNos.size(); i++) {
                digraph_text += listaNos.get(i).getId();
                digraph_text += "\n";
            }
            for (int i = 0; i < listaAresta.size(); i++) {
                digraph_text += "\n";
                if (grafo.getTipo().equals("undirected")) {
                    digraph_text += listaAresta.get(i).getOrigem() + "--" + listaAresta.get(i).getDestino() + "[label=" + listaAresta.get(i).getValorAresta() + "]" + ";";

                } else {
                    digraph_text += listaAresta.get(i).getOrigem() + "->" + listaAresta.get(i).getDestino() + "[label=" + listaAresta.get(i).getValorAresta() + "]" + ";";

                }
            }
            digraph_text += "\n}";
            System.out.println(digraph_text);
            try (FileWriter fw = new FileWriter(arquivo)) {
                fw.write(digraph_text);
                fw.flush();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Erro ao Criar Arquivo: " + ex.getMessage());
                System.out.println("Erro ao Criar Arquivo: " + ex.getMessage());
            }
            String commandLine = "dot -Tpng " + arquivo.getCanonicalPath() + " -o src\\image\\fileS.png";
            System.out.println(commandLine);

            p = Runtime.getRuntime().exec(commandLine);
            while (p.isAlive()) {
            }

            File arqImg = new File("src\\image\\fileS.png");
            ImageIcon image = new ImageIcon(arqImg.getCanonicalPath());
            image.getImage().flush();
            jLabel1.setIcon(image);
            jLabel1.setText("");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao Criar Arquivo: " + ex.getMessage());
            System.out.println("Erro do Executar Comando: " + ex.getMessage());
        }
    }
}
