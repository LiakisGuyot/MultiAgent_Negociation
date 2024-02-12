package org.multiagent.strategies;

import org.multiagent.communication.Negociation;

import java.util.List;

public class Factor implements Strategie{
    private double factor;

    public Factor(double factor){
        this.factor = factor;
    }

    @Override
    public boolean appliquer(Negociation negociation, Double objective){
        int lastid = negociation.getHistoprix().size()-1;
        double last = negociation.getPrice(lastid);
        if(last <= objective){
            return true;
        }
        else {
            if (lastid == 0) {
                negociation.addPrice(objective * factor);
                return false;
            }
            else if (lastid >= 3){
                if(last == negociation.getPrice(lastid-2) && negociation.getPrice(lastid-1) == negociation.getPrice(lastid-3)){
                    return true;
                }
            }
            double antelast = negociation.getPrice(lastid-1);
            if((last-antelast) * (1-factor)+ antelast > objective){
                negociation.addPrice(antelast);
                return false;
            }
            negociation.addPrice(((last-antelast) * (1-factor) + antelast));
            return false;
        }
    }
}
