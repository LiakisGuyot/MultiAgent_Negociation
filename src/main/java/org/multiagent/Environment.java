package org.multiagent;

import org.multiagent.agents.Fournisseur;
import org.multiagent.agents.Negociateur;
import org.multiagent.communication.Negociation;
import org.multiagent.items.Billet;
import org.multiagent.strategies.Factor;
import org.multiagent.strategies.FactorFournisseur;
import org.multiagent.strategies.Milieu;
import org.multiagent.strategies.MilieuFournisseur;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Environment implements Runnable{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
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
            System.out.println(ANSI_GREEN +
                            "Ticket " + nego.getBillet().getVilleProvenance() + " to " + nego.getBillet().getVilleDestination()+
                    " sold to " + nego.getNegociateur().getName() + " by " + nego.getFournisseur().getName() + " for " +
                    nego.getPrice(nego.getHistoprix().size()-1) + ANSI_RESET);
        }
        else{
            System.out.println(ANSI_RED +
                    "Negociation for ticket "+ nego.getBillet().getVilleProvenance() + " to " + nego.getBillet().getVilleDestination()+
                    " between " + nego.getNegociateur().getName() + " and " + nego.getFournisseur().getName() + " aborted" + ANSI_RESET);
        }
    }

    @Override
    public void run(){
        Random rand = new Random();
        int nbNego = 5;
        for (int i = 0; i < nbNego; i++) {
            Negociateur nego = new Negociateur("Neg " + i, this, new Factor(rand.nextFloat(0.5f, 0.75f)));
            negociateurs.add(nego);
        }
        Fournisseur fournisseur = new Fournisseur("Four", this, new FactorFournisseur(0.85f));
        Billet mabite = new Billet(1800, "Lyon", "Paris", "MateB");
        fournisseurs.add(fournisseur);
        for(int i = 0; i < nbNego; i++){
            Negociation n = new Negociation(fournisseur, negociateurs.get(i), mabite);
            negociateurs.get(i).addNegociation(n);
            fournisseur.addNegociation(n);
        }

        for(int i = 0; i < nbNego; i++){
            new Thread(negociateurs.get(i)).start();
        }
        new Thread(fournisseur).start();
            try {
                sleep(100);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

}