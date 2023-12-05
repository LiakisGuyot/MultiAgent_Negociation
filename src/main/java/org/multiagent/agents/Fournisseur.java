package org.multiagent.agents;

public class Fournisseur extends Agent{
    public Fournisseur(String name) {
        super(name);
    }

    @Override
    public void run() {
        boolean keep = true;
        while (keep) {
            Message firstMessage = this.getFirstMessage();
            if (firstMessage != null) {
                System.out.println(this.name + " received a message from " + firstMessage.getSender().getName() + " : " + firstMessage.getAction() + " (price : " + firstMessage.getNegociation().getPrice(firstMessage.getNegociation().getHistoprix().size()-1) + ")");
                keep = !appliquerStrategie(firstMessage.getNegociation());
            } else {
                System.out.println("Fournisseur " + this.name + " has no message");
            }
        }
    }

}
