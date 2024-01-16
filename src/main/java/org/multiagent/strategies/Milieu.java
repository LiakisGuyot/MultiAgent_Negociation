package org.multiagent.strategies;

import org.multiagent.communication.Negociation;

public class Milieu implements Strategie{


    @Override
    public boolean appliquer(Negociation negociation, Float objective){
        float last = negociation.getPrice(negociation.getHistoprix().size()-1);
        if(last < objective){
            return true;
        }
        else {
            if (negociation.getHistoprix().size() < 2) {
                negociation.addPrice(negociation.getBillet().getPrix() / 2);
                return false;
            } else {
                float antelast = negociation.getPrice(negociation.getHistoprix().size()-2);
                negociation.addPrice((antelast + last) / 2);
                if(negociation.getPrice(negociation.getHistoprix().size()-1) > objective){
                    return true;
                }
                return false;
            }
        }
    }
}
