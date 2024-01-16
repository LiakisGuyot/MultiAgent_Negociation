package org.multiagent.agents;

import org.multiagent.Environment;
import org.multiagent.communication.Message;
import org.multiagent.communication.Negociation;
import org.multiagent.strategies.Strategie;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Agent implements Runnable{
    protected static int idCounter = 0;
    protected ConcurrentLinkedQueue<Message> bal;
    protected final int id;
    protected final String name;
    protected List<Negociation> negociations;
    protected HashMap<Negociation, Float> objective_prices;
    protected Strategie strat;

    protected Environment env;


    /**
     * Creates a new agent with the given name.
     *
     * @param name the name of the agent
     */
    public Agent(String name, Environment env){
        this.id = idCounter++;
        this.name = name;
        this.bal = new ConcurrentLinkedQueue<>();
        this.negociations = new ArrayList<>();
        this.objective_prices = new HashMap<>();
        this.env = env;
    }

    /**
     * Adds a message to the back of the queue.
     * @param msg the message to add
     */
    public void depositMessage(Message msg) {
        this.bal.add(msg);
    }

    /**
     * Retrieves the first message in the queue, removing it from the queue.
     * @return the first message in the queue, or null if the queue is empty
     */
    public Message getFirstMessage(){
        if(this.bal.isEmpty()){
            return null;
        }
        return this.bal.poll();
    }

    public abstract void appliquerStrategie(Negociation nego);

    public String getName() {
        return name;
    }

    public void addNegociation(Negociation negociation){

        this.negociations.add(negociation);
    }

    public Negociation find_nego(int id_nego){
        for(Negociation nego : this.negociations){
            if(nego.getId() == id_nego){
                return nego;
            }
        }
        return null;
    }

    public void readMessage(Message msg) {
        System.out.println("Agent " + this.name + " received a message from " + msg.getSender().getName() );
        System.out.println("Action du message : " +msg.getAction());
        System.out.println("Sender du message : " +msg.getSender().getName());
    }

    public int getId(){return this.id;}
}
