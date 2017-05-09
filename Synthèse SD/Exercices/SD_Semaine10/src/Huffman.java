import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;


public class Huffman {

  private static class Node implements Comparable<Node> {
    private char ch;
    private int freq;
    private final Node left, right;

    public Node(char ch, int freq, Node left, Node right) {
      this.ch = ch;
      this.freq = freq;
      this.left = left;
      this.right = right;
    }

    public boolean isLeaf() {
      return left == null && right == null;
    }

    @Override
    public int compareTo(Node that) {
      return this.freq - that.freq;
    }
  }

  // renvoie une map qui a comme cl� les lettres de la chaine de
  // caract�re donn�e en param�tre et comme valeur la fr�quence de
  // ces lettres
  public static Map<Character, Integer> computeFreq(String s) {
    Map<Character, Integer> map = new HashMap<Character, Integer>();
    for (int i = 0; i < s.length(); i++) {
      Character carac = s.charAt(i);
      if (!map.containsKey(carac)) {
        map.put(carac, 1);
      } else {
        map.put(carac, map.get(carac) + 1);
      }
    }
    return map;
  }

  // renvoie l'arbre de Huffman obtenu � partir de la map des fr�quences des lettres
  public static Node buildTree(Map<Character, Integer> freq) {
    PriorityQueue<Node> list = new PriorityQueue<Node>(new Comparator<Node>() {

      @Override
      public int compare(Node node1, Node node2) {
        return node1.freq - node2.freq;
      }

    });
    for (Entry<Character, Integer> entry : freq.entrySet()) {
      list.add(new Node(entry.getKey(), entry.getValue(), null, null));
    }
    Node parent = list.peek();
    while (list.size() != 1) {
      Node filsGauche = list.remove();
      Node filsDroit = list.remove();
      parent = new Node('\u0000', filsGauche.freq + filsDroit.freq, filsGauche, filsDroit);
      list.add(parent);
    }
    return parent;
  }

  // renvoie une map qui associe chaque lettre � son code (suite de 0 et de 1).
  // Ce code est obtenu en parcourant l'arbre de Huffman donn� en param�tre
  public static Map<Character, String> buildCode(Node root) {
    Map<Character, String> map = new HashMap<Character, String>();
    buidCodeAuxi(root, "", map);
    return map;
  }

  private static void buidCodeAuxi(Node root, String path, Map<Character, String> map) {
    if (root.isLeaf()) {
      map.put(root.ch, path);
    } else {
      buidCodeAuxi(root.left, path + "0", map);
      buidCodeAuxi(root.right, path + "1", map);
    }
  }


  // encode la chaine de caract�re prise en param�tre en une chaine de
  // bit (0 et 1) en utilisant la map des codes codeMap
  public static String compress(String s, Map<Character, String> codeMap) {
    String coding = "";
    for (int i = 0; i < s.length(); i++) {
      coding += codeMap.get(s.charAt(i));
    }
    return coding;
  }

  // Cette m�thode d�code une chaine de 0 et de 1 cod� � l'aide de l'algorithme de Huffman
  // En param�tre, en plus de la chaine � d�coder, est sp�cifi� la racine de l'arbre de
  // Huffman
  public static String expand(Node root, String t) {
    String decoded = "";
    Node current = root;
    int i = 0;
    while (i < t.length() || current.isLeaf()) {
      if (current.isLeaf()) {
        decoded += current.ch;
        current = root;
      } else if (t.charAt(i) == '0') {
        current = current.left;
        i++;
      } else {
        i++;
        current = current.right;
      }
    }
    return decoded;
  }

  public static void main(String[] args) throws IOException {
    String s = HuffmanReadFile.loadFile(new File("files/11-0.txt"));
    System.out.println("Readed");
    Map<Character, Integer> freq = computeFreq(s);
    System.out.println("Computed");
    Node root = buildTree(freq);
    System.out.println("Builded (Tree)");
    Map<Character, String> code = buildCode(root);
    System.out.println("Builded (Code)");
    String compress = compress(s, code);
    System.out.println("Compressed");
    HuffmanWriteFile.write("fichier_compresse", compress);
    System.out.println("Writed");
    String texteOriginal = expand(root, HuffmanReadFile.read("files/fichier_compresse"));
    System.out.println(texteOriginal);
  }



}
