package org.multiagent.communication;

import org.multiagent.agents.Agent;

public class Message  {

    private Negociation negociation;
    private String action;
    private Agent sender;

    public Message(String content, Agent sender, Negociation negociation) {
        this.action = content;
        this.sender = sender;
        this.negociation = negociation;
    }

    public String getAction() {
        return this.action;
    }

    public Agent getSender() {
        return sender;
    }

    public void setAction(String action) {
        this.action = action;
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
        return "Message{" + "action=" + this.action + ", sender=" + this.sender.getId() + '}';
    }

}
