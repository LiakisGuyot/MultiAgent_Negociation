package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.items.Billet;
import org.multiagent.strategies.Milieu;

import java.util.Random;


public class Negociateur extends Agent{
    public Negociateur(String name, Environment env) {
        super(name, env);
        this.strat = new Milieu();
    }

    @Override
    public boolean appliquerStrategie(Negociation nego){
        boolean result = this.strat.appliquer(nego, this.objective_prices.get(nego));
        if(result){
            if(nego.getPrice(nego.getHistoprix().size()-1) > this.objective_prices.get(nego)){
                nego.getFournisseur().depositMessage(new Message("abort", this, nego));
                this.env.print_result(nego, true);
            }
            else{
                nego.getFournisseur().depositMessage(new Message("accept", this, nego));
                this.env.print_result(nego, false);
            }
            this.negociations.remove(nego);
            this.objective_prices.remove(nego);
        }
        else{
            nego.getFournisseur().depositMessage(new Message("keep", this, nego));
        }
        return result;
    }

    @Override
    public void addNegociation(Negociation negociation){
        super.addNegociation(negociation);
        Random random = new Random();
        objective_prices.put(negociation, negociation.getBillet().getPrix() * random.nextFloat(0.5f, 0.7f));
    }

    @Override
    public void run() {
        boolean keep = true;
        while(keep) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                System.out.println("Negociateur " + this.name + " received a message from " + firstMessage.getSender().getName());
                keep = !appliquerStrategie(firstMessage.getNegociation());
            } else {
                System.out.println("Negociateur " + this.name + " has no message");
                keep = !appliquerStrategie(this.negociations.get(0));
            }
        }
    }
}
