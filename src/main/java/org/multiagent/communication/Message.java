package org.multiagent.communication;

import org.multiagent.agents.Agent;

public class Message {

    private Negociation negociation;
    private Integer newprice;
    private Agent sender;

    public Message(Integer content, Agent sender) {
        this.newprice = content;
        this.sender = sender;
        this.negociation = negociation;
    }

    public Integer getNewprice() {
        return newprice;
    }

    public Agent getSender() {
        return sender;
    }

    public void setNewprice(Integer newprice) {
        this.newprice = newprice;
    }

    public void setSender(Agent sender) {
        this.sender = sender;
    }

    public Negociation getNegociation() {
        return negociation;
    }

    public void setNegociation(Negociation negociation) {
        this.negociation = negociation;
    }

    @Override
    public String toString() {
        return "Message{" + "proposed_price=" + newprice + ", sender=" + sender + '}';
    }

}
