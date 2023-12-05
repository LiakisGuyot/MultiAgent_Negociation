package org.multiagent.agents;

public class Negociateur extends Agent{
    public Negociateur(String name) {
        super(name);
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
