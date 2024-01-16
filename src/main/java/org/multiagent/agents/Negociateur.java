package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.strategies.Milieu;
import org.multiagent.strategies.Strategie;

import java.util.Random;


public class Negociateur extends Agent{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private float objective_reduction;

    public Negociateur(String name, Environment env, Strategie strat) {
        super(name, env);
        this.strat = strat;
        Random random = new Random();
        this.objective_reduction = ((float) random.nextGaussian(0.5, 0.25));
        if(this.objective_reduction < 0.25){
            this.objective_reduction = 0.25f;
        }
        if(this.objective_reduction > 1){
            this.objective_reduction = 0.99f;
        }
        if(strat.getClass() == Milieu.class) {
            this.objective_reduction = 0.80f;
        }
        System.out.println(ANSI_PURPLE + "Negociateur " + this.name + " created with objective reduction : -" + (1-this.objective_reduction) + "%" + ANSI_RESET);
    }

    @Override
    public void appliquerStrategie(Negociation nego){
        boolean result = this.strat.appliquer(nego, this.objective_prices.get(nego));
        if(result){
            if(nego.getPrice(nego.getHistoprix().size()-1) > this.objective_prices.get(nego)){
                nego.getFournisseur().depositMessage(new Message("abort", this, nego));
                this.env.print_result(nego, false);
            }
            else{
                nego.getFournisseur().depositMessage(new Message("accept", this, nego));
                this.env.print_result(nego, true);
            }
            this.negociations.remove(nego);
            this.objective_prices.remove(nego);
        }
        else{
            nego.getFournisseur().depositMessage(new Message("keep", this, nego));
        }
    }

    @Override
    public void addNegociation(Negociation negociation){
        super.addNegociation(negociation);
        objective_prices.put(negociation, negociation.getBillet().getPrix() * objective_reduction);
    }

    @Override
    public void run() {
        boolean first = true;
        while(true) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                System.out.println(ANSI_BLUE + this.name + " received a message from " + firstMessage.getSender().getName()
                + " : " + firstMessage.getAction() + " (proposed price : " +
                firstMessage.getNegociation().getPrice(firstMessage.getNegociation().getHistoprix().size()-1) + ")"
                + ANSI_RESET);
                if(firstMessage.getAction().equals("accept") || (firstMessage.getAction().equals("abort"))){
                    this.negociations.remove(firstMessage.getNegociation());
                }
                else if(firstMessage.getAction().equals("keep") && this.negociations.contains(firstMessage.getNegociation())){
                    appliquerStrategie(firstMessage.getNegociation());
                }
                else{
                    System.out.println("Error : unknown action");
                }
            } else {
                if(first){
                    appliquerStrategie(this.negociations.get(0));
                    first = false;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
