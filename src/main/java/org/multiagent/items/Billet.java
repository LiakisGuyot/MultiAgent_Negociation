package org.multiagent.items;

public class Billet {
    private float prix;
    private float prixOrigine;
    private String villeProvenance;
    private String villeDestination;
    private String compagnie;

    public Billet(float prixOrigine, String villeProvenance, String villeDestination, String compagnie) {
        this.prix = prixOrigine;
        this.prixOrigine = prixOrigine;
        this.villeProvenance = villeProvenance;
        this.villeDestination = villeDestination;
        this.compagnie = compagnie;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public float getPrixOrigine() {
        return prixOrigine;
    }

    public void setPrixOrigine(float prixOrigine) {
        this.prixOrigine = prixOrigine;
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
