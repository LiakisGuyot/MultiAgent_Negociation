package org.multiagent.strategies;

import org.multiagent.communication.Negociation;

public class MilieuFournisseur implements Strategie{


    @Override
    public boolean appliquer(Negociation negociation, Float objective){
        float last = negociation.getPrice(negociation.getHistoprix().size()-1);
        if(last > objective){
            return true;
        }
        else {
            float antelast = negociation.getPrice(negociation.getHistoprix().size()-2);
            negociation.addPrice((antelast + last) / 2);
            if( negociation.getPrice(negociation.getHistoprix().size()-1) < objective){
                return true;
            }
            return false;
        }
    }
}
