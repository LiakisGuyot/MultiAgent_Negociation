package org.multiagent.strategies;

import org.multiagent.communication.Negociation;
public class Factor implements Strategie{
    private float factor;

    public Factor(float factor){
        this.factor = factor;
    }

    @Override
    public boolean appliquer(Negociation negociation, Float objective){
        float last = negociation.getPrice(negociation.getHistoprix().size()-1);
        if(last < objective){
            return true;
        }
        else {
            if (negociation.getHistoprix().size() < 2) {
                negociation.addPrice(objective * factor);
                return false;
            } else {
                float antelast = negociation.getPrice(negociation.getHistoprix().size()-2);
                if((last-antelast) * factor > objective){
                    negociation.addPrice(antelast);
                    return false;
                }
                negociation.addPrice((last-antelast) * factor + antelast);
                return false;
            }
        }
    }
}
