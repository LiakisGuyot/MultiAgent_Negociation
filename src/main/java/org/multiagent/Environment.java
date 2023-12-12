package org.multiagent;

import org.multiagent.agents.Fournisseur;
import org.multiagent.agents.Negociateur;
import org.multiagent.communication.Negociation;
import org.multiagent.items.Billet;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Environment implements Runnable{
    public static List<Negociateur> negociateurs = new ArrayList<Negociateur>();
    public static List<Fournisseur> fournisseurs = new ArrayList<Fournisseur>();
    public List<Negociation> negociations = new ArrayList<>();

    /**
     * prints the outcome of a negociation
     * @param nego the negociation
     * @param outcome true if tickeet sold, false if not
     */
    public void print_result(Negociation nego, boolean outcome){
        try{
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(outcome) {
            System.out.println("Ticket " + nego.getBillet().getVilleProvenance() + " to " + nego.getBillet().getVilleDestination()+
                    " sold to " + nego.getNegociateur().getName() + " by " + nego.getFournisseur().getName() + " for " + nego.getPrice(nego.getHistoprix().size()-1));
        }
        else{
            System.out.println("Negociation for ticket "+ nego.getBillet().getVilleProvenance() + " to " + nego.getBillet().getVilleDestination()+
                    " between " + nego.getNegociateur().getName() + " and " + nego.getFournisseur().getName() + " aborted");
        }
    }

    @Override
    public void run(){
        Negociateur negociateur = new Negociateur("Negociateur", this);
        Fournisseur fournisseur = new Fournisseur("Fournisseur", this);
        negociateurs.add(negociateur);
        fournisseurs.add(fournisseur);
        Negociation n1 = new Negociation(fournisseur, negociateur, new Billet(1800, "Lyon", "Paris", "Mabite"));
        negociateur.addNegociation(n1);
        fournisseur.addNegociation(n1);

        new Thread(negociateur).start();
        new Thread(fournisseur).start();
            try {
                sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

}