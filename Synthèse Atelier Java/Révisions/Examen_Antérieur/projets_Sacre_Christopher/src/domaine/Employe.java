package domaine;

import static util.Util.checkObject;
import static util.Util.checkString;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Util;

public class Employe {

  public static final int MAX_PROJETS = 4;

  private String matricule;
  private String nom;
  private String prenom;
  private Set<Projet> projets = new HashSet<Projet>();
  private Map<Domaine, NiveauDeCompetence> domaines = new HashMap<Domaine, NiveauDeCompetence>();

  public Employe(String matricule, String nom, String prenom) {
    checkString(matricule);
    checkString(nom);
    checkString(prenom);
    this.matricule = matricule;
    this.nom = nom;
    this.prenom = prenom;
  }

  public String getMatricule() {
    return matricule;
  }

  public String getNom() {
    return nom;
  }

  public String getPrenom() {
    return prenom;
  }

  // Gestion des domaines dans lesquels l'employ� a des comp�tences

  // ajoute le domaine dans les comp�tences de l'employ� avec le niveau
  // sp�cifi�
  // renvoie false si le domaine est d�j� repris dans les comp�tences de
  // l'employ�
  public boolean ajouterCompetence(Domaine domaine, NiveauDeCompetence niveau) {
    checkObject(domaine);
    checkObject(niveau);
    if (domaines.containsKey(domaine))
      return false;
    domaines.put(domaine, niveau);
    return true;
  }

  // modifie le niveau de comp�tence de l'employ� dans le domaine donn�
  // renvoie false si le domaine n'est pas repris dans les comp�tences de
  // l'employ�
  public boolean modifierNiveau(Domaine domaine, NiveauDeCompetence niveau) {
    checkObject(domaine);
    checkObject(niveau);
    if (!domaines.containsKey(domaine))
      return false;
    domaines.put(domaine, niveau);
    return true;
  }

  // renvoie le niveau de comp�tence dans le dom�ine pass� en param�tre
  // renvoie null si le domaine n'est pas repris dans les comp�tences de
  // l'employ�
  public NiveauDeCompetence trouverNiveau(Domaine domaine) {
    checkObject(domaine);
    return domaines.get(domaine);
  }

  // renvoie true si un des domaines de l'ensemble pass� en param�tre est
  // repris dans les comp�tences de l'employ�
  // renvoie false sinon
  // VOUS DEVEZ UTILISER LES STREAMS POUR IMPL�MENTER CETTE M�THODE !!
  public boolean possedeUneCompetence(Set<Domaine> domaines) {
    return domaines.stream().anyMatch((d) -> this.domaines.containsKey(d));
  }

  // Gestion de l'association avec projet

  public boolean ajouterProjet(Projet projet) {
    Util.checkObject(projet);
    if (maximumProjetAtteint())
      return false;
    if (!possedeUneCompetence(projet.domaines()))
      return false;
    if (contientProjet(projet))
      return false;
    this.projets.add(projet);
    projet.ajouterEmploye(this);
    return true;
  }

  public boolean supprimerProjet(Projet projet) {
    if (!contientProjet(projet))
      return false;
    projets.remove(projet);
    projet.supprimerEmploye(this);
    return true;
  }

  public boolean contientProjet(Projet projet) {
    checkObject(projet);
    return projets.contains(projet);
  }

  public boolean maximumProjetAtteint() {
    return projets.size() >= MAX_PROJETS;
  }

  public Iterator<Projet> projets() {
    return Collections.unmodifiableSet(projets).iterator();
  }

  public int nombreDeProjets() {
    return projets.size();
  }

  enum NiveauDeCompetence {
    DEBUTANT("D�butant"), MOYEN("Moyen"), MAITRISE("Maitrise"), AVANCE("Avance"), EXPERT("Expert");

    String niveau;

    private NiveauDeCompetence(String niveau) {
      this.niveau = niveau;
    }

    @Override
    public String toString() {
      return (this.ordinal() + 1) + ". " + niveau;
    }
  }

  public static void main(String[] args) {
    System.out.println(NiveauDeCompetence.DEBUTANT.toString());
  }

}
