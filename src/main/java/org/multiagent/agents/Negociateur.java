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
        boolean first = true;
        while(keep) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                System.out.println(this.name + " received a message from " + firstMessage.getSender().getName()
                + " : " + firstMessage.getAction() + " (price : " + firstMessage.getNegociation().getPrice(firstMessage.getNegociation().getHistoprix().size()-1) + ")");
                if(firstMessage.getAction().equals("accept")){
                    keep = false;
                }
                else if(firstMessage.getAction().equals("abort")){
                    keep = false;
                }
                else if(firstMessage.getAction().equals("keep")){
                    keep = !appliquerStrategie(firstMessage.getNegociation());
                }
                else{
                    System.out.println("Error : unknown action");
                }
            } else {
                System.out.println(this.name + " has no message");
                if(first){
                    keep = !appliquerStrategie(this.negociations.get(0));
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
