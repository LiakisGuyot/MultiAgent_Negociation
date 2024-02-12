package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.items.Billet;
import org.multiagent.strategies.Strategie;

import java.util.Iterator;

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
                this.negociations.remove(nego);
                if(this.negociations.isEmpty()) {
                    this.bal.clear();
                    this.env.print_result(nego, false);
                }
            }
            else{
                nego.getNegociateur().depositMessage(new Message("accept", this, nego));
                this.removeNegociationByBillet(nego.getBillet());
                this.env.print_result(nego, true);
            }
            this.negociations.remove(nego);
            this.objective_prices.remove(nego);
        }
        else{
            nego.getNegociateur().depositMessage(new Message("keep", this, nego));
        }
    }

    @Override
    public void addNegociation(Negociation negociation, double obj_red){
        super.addNegociation(negociation, obj_red);
        double objective_reduction = obj_red;
        objective_prices.put(negociation, negociation.getBillet().getPrix() * objective_reduction);
        System.out.println(ANSI_PURPLE +
                "Negociation with " + negociation.getNegociateur().getName() + " created with maximal reduction : -" +
                (1-objective_reduction)+ "%" + ANSI_RESET);
    }

    @Override
    public void run() {
        while (true) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                if (this.negociations.contains(firstMessage.getNegociation())) {
                    System.out.println(ANSI_YELLOW + this.name + " received a message from " + firstMessage.getSender().getName() + " : "
                            + firstMessage.getAction() + " (proposed price : " +
                            firstMessage.getNegociation().getPrice(firstMessage.getNegociation().getHistoprix().size() - 1)
                            + ")" + ANSI_RESET);
                    if (firstMessage.getAction().equals("abort")) {
                        this.negociations.remove(firstMessage.getNegociation());
                        if(this.negociations.isEmpty()) {
                            this.bal.clear();
                            this.env.print_result(firstMessage.getNegociation(), false);
                        }
                    }
                    else if (firstMessage.getAction().equals("keep") && this.negociations.contains(firstMessage.getNegociation())) {
                        appliquerStrategie(firstMessage.getNegociation());
                    }
                    else if (firstMessage.getAction().equals("accept")) {
                        this.removeNegociationByBillet(firstMessage.getNegociation().getBillet());
                        this.env.print_result(firstMessage.getNegociation(), true);
                    }


                }
                else{
                    System.out.println( ANSI_YELLOW +
                            "Fournisseur " + this.name + " received an unknown message or a closed negociation"
                            + ANSI_RESET);
                }
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
                            nego.getNegociateur().removeNegociation(nego);
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
