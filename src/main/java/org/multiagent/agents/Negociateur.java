package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.strategies.Strategie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Negociateur extends Agent{

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private static boolean first = true;
    protected HashMap<Agent, Double> results_history;
    protected boolean active = true;
    protected List<Double> results;

    public Negociateur(String name, Environment env, Strategie strat) {
        super(name, env);
        this.strat = strat;
        this.results = new ArrayList<>();
        this.results_history = new HashMap<>();
    }

    @Override
    public void appliquerStrategie(Negociation nego){
        boolean result = this.strat.appliquer(nego, this.objective_prices.get(nego));
        if(result){
            if(nego.getPrice(nego.getHistoprix().size()-1) > this.objective_prices.get(nego)){
                nego.getFournisseur().depositMessage(new Message("abort", this, nego));
            }
            else{
                nego.getFournisseur().depositMessage(new Message("accept", this, nego));
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
        System.out.println(ANSI_PURPLE + "Negociateur " + this.name + " created with objective reduction : -" + (1-obj_red) + "%" + ANSI_RESET);
    }

    @Override
    public void run() {
        while(this.active) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                System.out.println(ANSI_BLUE + this.name + " received a message from " + firstMessage.getSender().getName()
                + " : " + firstMessage.getAction() + " (proposed price : " +
                firstMessage.getNegociation().getPrice(firstMessage.getNegociation().getHistoprix().size()-1) + ")"
                + ANSI_RESET);
                if(firstMessage.getAction().equals("accept") || (firstMessage.getAction().equals("abort"))){
                    this.bal.clear();
                    this.removeNegociation(firstMessage.getNegociation());
                }
                else if(firstMessage.getAction().equals("keep") && this.negociations.contains(firstMessage.getNegociation())){
                    appliquerStrategie(firstMessage.getNegociation());
                }
                else{
                    System.out.println("Error : unknown action");
                }
            }
            else {
                //System.out.println(first + " " + this.name);
                try{
                    if(first && !this.negociations.isEmpty()){
                        appliquerStrategie(this.negociations.get(0));
                        first = false;
                    }
                }catch (IndexOutOfBoundsException e){
                    System.out.println("No negociation for " + this.name + " yet");
                }
            }
        }
    }

    public void removeNegociation(Negociation nego){
        this.bal.clear();
        this.negociations.remove(nego);
    }

    public void setFirst(boolean first){
        this.first = first;
    }

    public void setActive(boolean active){
        this.active = active;
        this.results.clear();
    }

    public void addResult(double result){
        this.results.add(result);
    }

    public double evaluatePerformances(Agent agent){
        System.out.println(this.results);
        double nbSuccess = 0;
        for(double result : this.results){
            nbSuccess+=result;
        }
        double resultat = nbSuccess/this.results.size();
        this.results_history.put(agent, resultat);
        this.results.clear();
        return resultat;
    }

    public boolean isActive(){
        return this.active;
    }

}
