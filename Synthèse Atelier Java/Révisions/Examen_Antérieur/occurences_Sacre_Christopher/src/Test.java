

import java.nio.file.FileSystems;
import java.util.Scanner;

public class Test {
  public static void main(String[] args) {
    Integer retour = null;
    String motClef = null;
    String repertoire = null;

    Scanner in = new Scanner(System.in);
    System.out.print("Indiquez le r�pertoire de d�part: ");
    repertoire = in.nextLine();

    System.out.print("Indiquez un mot � chercher � partir de ce r�pertoire : ");
    motClef = in.nextLine();

    ScruterRepertoire scRepertoire =
        new ScruterRepertoire(FileSystems.getDefault().getPath(repertoire), motClef);

    // lancer l'ex�cution d'une tache dans un nouveau Thread afin de faire le travail de scruter le
    // r�pertoire
    // et d'en r�cup�rer le r�sultat
    Thread thread = new Thread(new ThreadResult(scRepertoire));
    thread.start();

    System.out.println("Le mot " + motClef + " se trouve dans " + retour + " fichiers.");

    in.close();

  }

  private static class ThreadResult implements Runnable {

    ScruterRepertoire rep;

    public ThreadResult(ScruterRepertoire rep) {
      this.rep = rep;
    }

    @Override
    public void run() {
      rep.call();
    }

  }
}
