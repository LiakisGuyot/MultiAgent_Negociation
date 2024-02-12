package org.multiagent.items;

public class Billet {
    private double prix;
    private String villeProvenance;
    private String villeDestination;
    private String compagnie;

    public Billet(double prixOrigine, String villeProvenance, String villeDestination, String compagnie) {
        this.prix = prixOrigine;
        this.villeProvenance = villeProvenance;
        this.villeDestination = villeDestination;
        this.compagnie = compagnie;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getVilleProvenance() {
        return villeProvenance;
    }

    public void setVilleProvenance(String villeProvenance) {
        this.villeProvenance = villeProvenance;
    }

    public String getVilleDestination() {
        return villeDestination;
    }

    public void setVilleDestination(String villeDestination) {
        this.villeDestination = villeDestination;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }
}
