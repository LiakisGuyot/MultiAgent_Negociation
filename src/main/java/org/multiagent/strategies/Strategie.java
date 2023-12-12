package org.multiagent.strategies;

import org.multiagent.communication.Negociation;

public interface Strategie {

    public boolean appliquer(Negociation negociation, Float objective_price);
}
