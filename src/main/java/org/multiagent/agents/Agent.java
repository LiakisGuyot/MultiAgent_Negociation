package org.multiagent.agents;

import org.multiagent.communication.Message;

import java.util.EventListener;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class Agent implements Runnable{
    private static int idCounter = 0;
    private Queue<Message> bal;
    private final int id;
    protected final String name;

    public Agent(String name){
        this.id = idCounter++;
        this.name = name;
        this.bal = new PriorityQueue<Message>();

    }

    public void depositMessage(Message msg){
        this.bal.add(msg);
    }


}
