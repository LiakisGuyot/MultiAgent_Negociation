package org.multiagent.communication;

import org.multiagent.agents.Fournisseur;
import org.multiagent.agents.Negociateur;
import org.multiagent.items.Billet;

import java.util.ArrayList;
import java.util.List;

public class Negociation {

    private Fournisseur fournisseur;
    private Negociateur negociateur;
    private Billet billet;
    private List<Double> Histoprix;
    private static int idCounter;
    private final int id;

    public Negociation(Fournisseur fournisseur, Negociateur negociateur, Billet billet) {
        this.id = idCounter ++;
        this.fournisseur = fournisseur;
        this.negociateur = negociateur;
        this.billet = billet;
        this.Histoprix = new ArrayList<>();
        this.Histoprix.add(billet.getPrix());
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Negociateur getNegociateur() {
        return negociateur;
    }

    public void setNegociateur(Negociateur negociateur) {
        this.negociateur = negociateur;
    }

    public Billet getBillet() {
        return billet;
    }

    public void setBillet(Billet billet) {
        this.billet = billet;
    }

    public void addPrice(double price){
        this.Histoprix.add(price);
    }

    public List<Double> getHistoprix(){
        return this.Histoprix;
    }

    public double getPrice(Integer i){
        return this.Histoprix.get(i);
    }

    public int getId(){return this.id;}
}
