package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.strategies.Strategie;

import java.util.List;

public class Coalition extends Negociateur{


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private List<Negociateur> members;

    public Coalition(String name, List<Negociateur> members, Environment env){
        super(name, env, null);
        this.members = members;
        this.active = false;
    }

    @Override
    public void setActive(boolean active){
        this.active = active;
        for(Negociateur n : this.members){
            n.setActive(!active);
        }
    }

    public void setStrategie(Strategie strat){
        this.strat = strat;
    }

    @Override
    public void addResult(double result){
        for(Negociateur n : this.members){
            n.addResult(result/this.members.size());
        }
    }

    @Override
    public double evaluatePerformances(Agent agent){
        double result = 0;
        for(Negociateur n : this.members){
            result = n.evaluatePerformances(this);
        }
        return result;
    }


    @Override
    public void appliquerStrategie(Negociation nego) {
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
    public void addNegociation(Negociation negociation, double obj_red){
        super.addNegociation(negociation, obj_red);
        objective_prices.put(negociation, negociation.getBillet().getPrix() * obj_red);
        System.out.println(ANSI_PURPLE + "Coalition " + this.name + " created with objective reduction : -" + (1-obj_red) + "%" + ANSI_RESET);
    }

    @Override
    public void run() {
        boolean first = true;
        while(this.active) {
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
