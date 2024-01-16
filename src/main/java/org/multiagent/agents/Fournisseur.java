package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.items.Billet;
import org.multiagent.strategies.MilieuFournisseur;
import org.multiagent.strategies.Strategie;

import java.util.Iterator;
import java.util.Random;

public class Fournisseur extends Agent{


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    public Fournisseur(String name, Environment env, Strategie strat) {
        super(name, env);
        this.strat = strat;
    }

    @Override
    public void appliquerStrategie(Negociation nego){
        boolean result = this.strat.appliquer(nego, this.objective_prices.get(nego));
        if(result){
            if(nego.getPrice(nego.getHistoprix().size()-1) < this.objective_prices.get(nego)){
                nego.getNegociateur().depositMessage(new Message("abort", this, nego));
                this.env.print_result(nego, false);
            }
            else{
                nego.getNegociateur().depositMessage(new Message("accept", this, nego));
                this.env.print_result(nego, true);
                this.removeNegociationByBillet(nego.getBillet());
            }
            this.negociations.remove(nego);
            this.objective_prices.remove(nego);
        }
        else{
            nego.getNegociateur().depositMessage(new Message("keep", this, nego));
        }
    }

    @Override
    public void addNegociation(Negociation negociation){
        super.addNegociation(negociation);
        Random random = new Random();
        float objective_reduction = ((float) random.nextGaussian(0.75, 0.25));
        if(objective_reduction < 0.25){
            objective_reduction = 0.25f;
        }
        else if(objective_reduction > 1){
            objective_reduction = 0.99f;
        }
        objective_reduction = 0.6f;
        objective_prices.put(negociation, negociation.getBillet().getPrix() * objective_reduction);
        System.out.println(ANSI_PURPLE +
                "Negociation with " + negociation.getNegociateur().getName() + " created with maximal reduction : -" +
                (1-objective_reduction)+ "%" + ANSI_RESET);
    }

    @Override
    public void run() {
        boolean keep = true;
        while (keep) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                System.out.println(ANSI_YELLOW + this.name + " received a message from " + firstMessage.getSender().getName() + " : "
                        + firstMessage.getAction() + " (proposed price : " +
                        firstMessage.getNegociation().getPrice(firstMessage.getNegociation().getHistoprix().size()-1)
                        + ")" + ANSI_RESET);
                if(firstMessage.getAction().equals("accept")){
                    this.removeNegociationByBillet(firstMessage.getNegociation().getBillet());
                }
                else if(firstMessage.getAction().equals("abort")){
                    this.negociations.remove(firstMessage.getNegociation());
                }
                else if(firstMessage.getAction().equals("keep") && this.negociations.contains(firstMessage.getNegociation())){
                        appliquerStrategie(firstMessage.getNegociation());
                }
                else{
                    System.out.println( ANSI_YELLOW +
                            "Fournisseur " + this.name + " received an unknown message or a closed negociation"
                            + ANSI_RESET);
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeNegociationByBillet(Billet billet){
        boolean continuer = true;
        synchronized (this.negociations) {
            while (continuer) {
                try {
                    continuer = false;
                    Iterator<Negociation> iterator= this.negociations.iterator();
                    while(iterator.hasNext()){
                        Negociation nego = iterator.next();
                        if(nego.getBillet().equals(billet)){
                            nego.getNegociateur().depositMessage(new Message("abort", this, nego));
                            iterator.remove();
                            this.objective_prices.remove(nego);
                        }
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    continuer = true;
                }
            }
        }
    }

}
