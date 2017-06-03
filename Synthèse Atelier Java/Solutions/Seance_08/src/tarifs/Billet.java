package tarifs;

import java.time.LocalDate;

import exceptions.TarifNonDisponibleException;
import tarifs.Tarif.TypeReduction;
import util.Util;

public class Billet {
	private Trajet trajet;
	private double prix;
	private TypeReduction typeReduction;
	private LocalDate dateVoyage;

	public Billet(Trajet trajet, TypeReduction typeReduction, LocalDate dateVoyage) throws TarifNonDisponibleException {
		Util.checkObject(trajet);
		Util.checkObject(typeReduction);
		Util.checkObject(dateVoyage);
		this.trajet = trajet;
		this.typeReduction = typeReduction;
		this.dateVoyage = dateVoyage;
		this.prix = Assembly.getListeTarifs().getPrix(trajet, typeReduction, dateVoyage);
	}

	public Billet(Trajet trajet, TypeReduction typeReduction) throws TarifNonDisponibleException {
		this(trajet, typeReduction, LocalDate.now());
	}

	public Trajet getTrajet() {
		return trajet;
	}

	public double getPrix() {
		return prix;
	}

	public TypeReduction getTypeReduction() {
		return typeReduction;
	}

	public LocalDate getDateVoyage() {
		return dateVoyage;
	}
	@Override
	public String toString() {
		String reduc;
		if (typeReduction == TypeReduction.TARIF_PLEIN)
			reduc = " - TARIF_PLEIN";
		else
			reduc = " - R�duction : " + typeReduction;
		return "Voyage de " + trajet.getGareDepart() + " à "
				+ trajet.getGareArrivee() + "\nDistance : "
				+ trajet.getDistance() + reduc + "\nPrix : " + prix;
	}
}
