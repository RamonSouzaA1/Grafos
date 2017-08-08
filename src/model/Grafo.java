package model;
/**
 *
 * Integrantes do grupo:
 * Victor Domingos Duque
 * Lucas Lagrimante Martinho
 * Ramon de Souza Almeida
 * Kaio Cesar B. Costa
 */
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@XStreamAlias("graph")
public class Grafo {

    @XStreamAlias("id")
    @XStreamAsAttribute
    private String id;
    @XStreamAlias("edgedefault")
    @XStreamAsAttribute
    private String tipo;
    @XStreamImplicit(itemFieldName = "node")
    private List<No> nos;
    @XStreamImplicit(itemFieldName = "edge")
    private List<Aresta> arestas;
    @XStreamOmitField
    private int[][] matriz;
    @XStreamOmitField
    private int[][] matrizI;
    @XStreamOmitField
    private int[][] matrizValue;
    Object parent;
    @XStreamOmitField
    List<List<No>> adjacencia = new ArrayList<List<No>>();

    public Grafo(String id, String tipo, List<No> nos, List<Aresta> arestas) {
        this.id = id;
        this.tipo = tipo;
        this.nos = nos;
        this.arestas = arestas;
        geraMatriz();
    }

    public Grafo() {
    }

    public List<No> getVertices() {
        return nos;
    }

    public int getQuantidadeAretas() {
        return arestas.size();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<No> getNos() {
        return nos;
    }

    public void setNos(List<No> nos) {
        this.nos = nos;
    }

    public List<Aresta> getArestas() {
        return arestas;
    }

    public void setArestas(List<Aresta> arestas) {
        this.arestas = arestas;
    }

    public int getOrdem() {
        return nos.size();
    }

    public void geraMatriz() {
        int Qtdenos = nos.size();
        matriz = new int[Qtdenos][Qtdenos];
        for (int i = 0; i < Qtdenos; i++) {
            for (int j = 0; j < Qtdenos; j++) {
                matriz[i][j] = 0;
            }
        }
        for (Aresta are : arestas) {
            int posO = nos.indexOf(new No(are.getOrigem()));
            int posD = nos.indexOf(new No(are.getDestino()));
            matriz[posO][posD] = 1;
            if (tipo.equals("undirected")) {
                matriz[posD][posO] = 1;
            }
        }
    }

    public No getNoById(String id) {
        No a = null;
        for (No no : nos) {
            if (no.getId().equals(id)) {
                a = no;
                break;
            }
        }
        return a;
    }

    public Grafo copiaGrafo(Grafo grafo, String nome) {
        List<No> listaNos2 = new ArrayList<No>();
        List<Aresta> listaArestas2 = new ArrayList<Aresta>();
        for (No n : grafo.getNos()) {
            listaNos2.add(n);
        }
        for (Aresta a : grafo.getArestas()) {
            listaArestas2.add(a);
        }
        Grafo g = new Grafo(nome, grafo.getTipo(), listaNos2, listaArestas2);
        return g;
    }

    public void salvaGrafo(Grafo grafo) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(Grafo.class);
        System.out.println(xstream.toXML(grafo));
        String xml = xstream.toXML(grafo);

        grafo = null;
        grafo = (Grafo) xstream.fromXML(xml);
        try {
            File xmlFile = new File(grafo.getId() + ".xml");
            xstream.toXML(grafo, new FileWriter(xmlFile));
        } catch (IOException ex) {
            System.out.println("Erro ao Gravar Arquivo");
        }
    }


    public int getGrauEmissao(No no) {
        int grau = 0;
        for (Aresta aresta : this.arestas) {
            if (aresta.getOrigem() == no.getId()) {
                grau++;
            }
        }
        return grau;
    }

    public int getGrauRecepcao(No no) {
        int grau = 0;
        for (Aresta aresta : this.arestas) {
            if (aresta.getDestino() == no.getId()) {
                grau++;
            }
        }
        return grau;
    }

    public int getGrauVertice(No no) {
        int grau = 0;
        for (Aresta aresta : this.arestas) {
            if (aresta.getDestino() == no.getId()) {
                grau++;
            }
            if (aresta.getOrigem() == no.getId()) {
                grau++;
            }
        }
        return grau;
    }

    public int[][] getMatriz() {
        return matriz;
    }

    public int[][] matrizAdjacencia(Grafo grafo) {

        int[][] ma = new int[grafo.getVertices().size()][grafo.getVertices().size()];

        for (int i = 0; i < grafo.getVertices().size(); i++) {
            for (int j = 0; i < grafo.getVertices().size(); i++) {
                ma[i][j] = 0;
            }

        }

        if (grafo.getTipo().equals("unidirected")) {

            for (int i = 0; i < grafo.getVertices().size(); i++) {
                String v1 = grafo.getVertices().get(i).getId();

                for (int j = 0; j < grafo.getArestas().size(); j++) {
                    if (v1.equals(grafo.getArestas().get(j).getOrigem())) {

                        for (int k = 0; k < grafo.getVertices().size(); k++) {
                            String v2 = grafo.getVertices().get(k).getId();

                            if (v1.equals(grafo.getArestas().get(j).getOrigem()) && v2.equals(grafo.getArestas().get(j).getDestino())) {
                                ma[i][k] = 1;
                                ma[k][i] = 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (grafo.getTipo().equals("directed")) {
            for (int i = 0; i < grafo.getVertices().size(); i++) {
                String vertice1 = grafo.getVertices().get(i).getId();

                for (int j = 0; j < grafo.getArestas().size(); j++) {
                    if (vertice1.equals(grafo.getArestas().get(j).getOrigem())) {

                        for (int k = 0; k < grafo.getVertices().size(); k++) {
                            String vertice2 = grafo.getVertices().get(k).getId();

                            if (vertice1.equals(grafo.getArestas().get(j).getOrigem()) && vertice2.equals(grafo.getArestas().get(j).getDestino())) {
                                ma[i][k] = 1;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return ma;
    }

    public int[][] matrizIncidencia(Grafo grafo) {

        int[][] mi = new int[grafo.getVertices().size()][grafo.getArestas().size()];

        for (int i = 0; i < grafo.getVertices().size(); i++) {
            for (int j = 0; i < grafo.getArestas().size(); i++) {
                mi[i][j] = 0;
            }

        }
        if (grafo.getTipo().equals("unidirected")) {
            for (int i = 0; i < grafo.getArestas().size(); i++) {
                String vertice1 = grafo.getArestas().get(i).getOrigem();
                String vertice2 = grafo.getArestas().get(i).getDestino();
                for (int k = 0; k < grafo.getVertices().size(); k++) {
                    if (vertice1.equals(grafo.getVertices().get(k).getId())) {
                        for (int j = 0; j < grafo.getVertices().size(); j++) {
                            if (vertice2.equals(grafo.getVertices().get(j).getId())) {
                                mi[k][i] = 1;
                                mi[j][i] = 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (grafo.getTipo().equals("directed")) {
            for (int i = 0; i < grafo.getArestas().size(); i++) {
                String vertice1 = grafo.getArestas().get(i).getOrigem();
                String vertice2 = grafo.getArestas().get(i).getDestino();
                for (int k = 0; k < grafo.getVertices().size(); k++) {
                    if (vertice1.equals(grafo.getVertices().get(k).getId())) {
                        for (int j = 0; j < grafo.getVertices().size(); j++) {
                            if (vertice2.equals(grafo.getVertices().get(j).getId())) {
                                mi[k][i] = 1;
                                mi[j][i] = -1;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return mi;
    }

    public ArrayList<ArrayList> listaAdjacencia(Grafo grafo) {
        ArrayList<ArrayList> listaIncidencia = new ArrayList<ArrayList>();

        if (grafo.getTipo().equals("directed")) {
            for (int i = 0; i < grafo.getVertices().size(); i++) {
                ArrayList<String> lista = new ArrayList<String>();
                String v1 = grafo.getVertices().get(i).getId();
                lista.add(v1);
                for (int j = 0; j < grafo.getVertices().size(); j++) {
                    if (v1.equals(grafo.getArestas().get(j).getOrigem())) {
                        lista.add(grafo.getArestas().get(j).getDestino());
                    }
                }
                listaIncidencia.add(lista);
            }
        }

        if (grafo.getTipo().equals("unidirected")) {
            for (int i = 0; i < grafo.getVertices().size(); i++) {
                ArrayList<String> lista = new ArrayList<String>();
                String v1 = grafo.getVertices().get(i).getId();
                lista.add(v1);
                for (int j = 0; j < grafo.getArestas().size(); j++) {
                    if (v1.equals(grafo.getArestas().get(j).getDestino()) || v1.equals(grafo.getArestas().get(j).getOrigem())) {
                        if (v1.equals(grafo.getArestas().get(j).getOrigem())) {
                            lista.add(grafo.getArestas().get(j).getDestino());
                        } else {
                            lista.add(grafo.getArestas().get(j).getOrigem());
                        }
                    }
                }
                listaIncidencia.add(lista);
            }
        }
        return listaIncidencia;
    }
}
