package org.multiagent.strategies;

import org.multiagent.communication.Negociation;

public class FactorFournisseur implements Strategie{
    private double factor;

    public FactorFournisseur(double factor){
        this.factor = factor;
    }

    @Override
    public boolean appliquer(Negociation negociation, Double objective){
        int lastid = negociation.getHistoprix().size()-1;
        double last = negociation.getPrice(lastid);
        if(last >= objective){
            return true;
        }
        else {
            if (lastid >= 3){
                if(last == negociation.getPrice(lastid-2) && negociation.getPrice(lastid-1) == negociation.getPrice(lastid-3)){
                    return true;
                }
            }
            double antelast = negociation.getPrice(lastid-1);
            if(antelast - (antelast-last) * (1-factor) < objective){
                negociation.addPrice(antelast);
                return false;
            }
            negociation.addPrice((int)(antelast - (antelast-last) * (1-factor)));
            return false;
        }
    }
}
