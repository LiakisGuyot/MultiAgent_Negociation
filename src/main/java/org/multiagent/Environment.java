package org.multiagent;

import org.multiagent.agents.Agent;
import org.multiagent.agents.Coalition;
import org.multiagent.agents.Fournisseur;
import org.multiagent.agents.Negociateur;
import org.multiagent.communication.Negociation;
import org.multiagent.items.Billet;
import org.multiagent.strategies.Factor;
import org.multiagent.strategies.FactorFournisseur;


import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class Environment implements Runnable{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static final List<String> listeVilles = Arrays.asList("Paris","Versailles","Lyon","Marseille","Toulouse","Bordeaux","Nantes","Strasbourg","Lille","Nice","Montpellier");
    public static int nbNego =4;
    public static int refreshRate = 1;
    public static double epsilon = 0.7;
    public static double decay = 0.9;
    public AtomicBoolean nego_ended = new AtomicBoolean(false);
    public static List<Negociateur> negociateurs = new ArrayList<Negociateur>();
    public static List<Fournisseur> fournisseurs = new ArrayList<Fournisseur>();
    public static List<Double> factors = new ArrayList<>();
    public static List<Double> obj_reds = new ArrayList<>();
    public static HashMap<Negociateur, Double> coal_factors = new HashMap<>();
    public static HashMap<Negociateur, Double> coal_obj_reds = new HashMap<>();

    public static Semaphore use_bal = new Semaphore(1, true);

    public  Boolean isCoalition;

    public Environment(Boolean isCoalition){
        super();
        this.isCoalition = isCoalition;
        factors.add(0.9);
        factors.add(0.85);
        factors.add(0.8);
        factors.add(0.75);
        obj_reds.add(0.8);
        obj_reds.add(0.8);
        obj_reds.add(0.8);
        obj_reds.add(0.8);
        for (int i = 0; i < nbNego; i++) {
            Negociateur nego = new Negociateur("Negotiateur " + i, this, new Factor(factors.get(i)));
            negociateurs.add(nego);
        }
        Fournisseur fournisseur = new Fournisseur("Fournisseur ", this, new FactorFournisseur(0.8));
        fournisseurs.add(fournisseur);
        createAllCoalitions();
        coal_factors.put(negociateurs.get(4), 0.6);
        coal_factors.put(negociateurs.get(5), 0.3);
        coal_factors.put(negociateurs.get(6), 0.5);
        coal_factors.put(negociateurs.get(7), 0.99);
        coal_factors.put(negociateurs.get(8), 0.65);
        coal_factors.put(negociateurs.get(9), 0.7);
        coal_factors.put(negociateurs.get(10), 0.1);
        coal_factors.put(negociateurs.get(11), 0.7);
        coal_factors.put(negociateurs.get(12), 0.35);
        coal_factors.put(negociateurs.get(13), 0.2);
        coal_factors.put(negociateurs.get(14), 0.8);
        for(int i = nbNego; i < 14; i++){
            coal_obj_reds.put(negociateurs.get(i), 0.8);
        }
        coal_factors.put(negociateurs.get(14), 0.7);
        setAllCoalitionsStrat();
    }



    /**
     * prints the outcome of a negociation
     * @param nego the negociation
     * @param outcome true if tickeet sold, false if not
     */
    public void print_result(Negociation nego, boolean outcome){
        if(outcome) {
            System.out.println(ANSI_GREEN +
                            "Ticket " + nego.getBillet().getVilleProvenance() + " to " + nego.getBillet().getVilleDestination()+
                    " sold to " + nego.getNegociateur().getName() + " by " + nego.getFournisseur().getName() + " for " +
                    nego.getPrice(nego.getHistoprix().size()-1) + ANSI_RESET);
            List<Negociateur> negociators = this.getActiveNegociateurs();
            for(Negociateur n : negociators){
                if(n == nego.getNegociateur()){
                    n.addResult(1);
                }
                else{
                    n.addResult(0);
                }
            }
        }
        else{
            System.out.println(ANSI_RED +
                    "Negociation for ticket "+ nego.getBillet().getVilleProvenance() + " to " + nego.getBillet().getVilleDestination()+ " aborted" + ANSI_RESET);
            List<Negociateur> negociators = this.getActiveNegociateurs();
            for(Negociateur n : negociators){
                n.addResult(0);
            }
        }
        try{
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.nego_ended.set(true);
    }

    public void newBillet(Billet billet){
        List<Negociateur> active_negociateurs = getActiveNegociateurs();
        for(Negociateur nego : active_negociateurs){
            Negociation n = new Negociation(fournisseurs.get(0), nego, billet);
            if(nego instanceof Coalition){
                nego.addNegociation(n, coal_obj_reds.get(nego));
            }
            else {
                nego.addNegociation(n, obj_reds.get(negociateurs.indexOf(nego)));
            }
            fournisseurs.get(0).addNegociation(n, 0.8);
        }
        for(Negociateur nego : active_negociateurs){
            nego.setFirst(true);
        }
    }

    /**
     * creates all possible coalitions between negociators from 2 to  n-1 members (n would have a 100%
     */
    public void createAllCoalitions(){
        for(int i = 0; i< nbNego; i++){
            for(int j = i+1; j < nbNego; j++){
                List<Negociateur> members = new ArrayList<>();
                List<Integer> indexes = new ArrayList<>();
                members.add(negociateurs.get(i));
                indexes.add(i);
                members.add(negociateurs.get(j));
                indexes.add(j);
                Coalition c = new Coalition("C " + indexes, members, this);
                negociateurs.add(c);
            }
        }
        for(int j = 0; j < nbNego; j++){
            List<Negociateur> members = new ArrayList<>();
            List<Integer> indexes = new ArrayList<>();
            for(int k = 0; k < 3; k++){
                members.add(negociateurs.get((j+k)%nbNego));
                indexes.add((j+k)%nbNego);
            }
            Coalition c = new Coalition("C " + indexes, members, this);
            negociateurs.add(c);
        }
        List<Negociateur> members = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        for(int k = 0; k < nbNego; k++){
            members.add(negociateurs.get(k));
            indexes.add(k);
        }
        Coalition c = new Coalition("C " + indexes, members, this);
        negociateurs.add(c);
    }


    public List<Negociateur> getActiveNegociateurs(){
        List<Negociateur> active = new ArrayList<>();
        for(Negociateur n : negociateurs){
            if(n.isActive()){
                active.add(n);
            }
        }
        return active;
    }

    public void setAllCoalitionsStrat(){
        for(int i = 4; i < 15; i++){
            ((Coalition)negociateurs.get(i)).setStrategie(new Factor(coal_factors.get(negociateurs.get(i))));
        }
    }

    @Override
    public void run(){
        Billet bil = new Billet(1800, "Lyon", "Paris", "MateB");
        System.out.println(ANSI_GREEN +
                "Ticket to be negotiated from : "+ bil.getVilleProvenance() + " to : " +bil.getVilleDestination()+ ", starting price : " + bil.getPrix() + ANSI_RESET);
        newBillet(bil);
        for(Negociateur n : negociateurs){
            new Thread(n).start();
        }
        new Thread(fournisseurs.get(0)).start();
        int refresh = 0;
        while(true){
            if(this.nego_ended.get()){
                refresh++;
                if(this.isCoalition == Boolean.TRUE) {
                    if (refresh == refreshRate) {
                        List<Negociateur> singletons = new ArrayList<>();
                        for (Negociateur n : negociateurs) {
                            if (!(n instanceof Coalition)) {
                                singletons.add(n);
                            }
                        }
                        List<Negociateur> proposedCoalitions = new ArrayList<>();
                        for (Negociateur n : this.getActiveNegociateurs()) {
                            System.out.println(n.getName() + " : " + n.evaluatePerformances(n));
                        }
                        Collections.shuffle(singletons);
                        while (!singletons.isEmpty()) {
                            Negociateur single = singletons.get(0);
                            List<Negociateur> explore = findNotExploredCoalitions(single);
                            Negociateur newNego;
                            boolean isOk;
                            do {
                                isOk = true;
                                newNego = single.chooseCoalition(explore, epsilon);
                                if (newNego instanceof Coalition) {
                                    if (!singletons.containsAll(((Coalition) newNego).getMembers())) {
                                        isOk = false;
                                    } else {
                                        List<Negociateur> members = new ArrayList<>(((Coalition) newNego).getMembers());
                                        members.remove(single);
                                        isOk = checkIfMembersOK(members, newNego);
                                    }
                                }
                            } while (!isOk);
                            if (newNego instanceof Coalition) {
                                singletons.removeAll(((Coalition) newNego).getMembers());
                            } else {
                                singletons.remove(single);
                            }
                            proposedCoalitions.add(newNego);
                        }
                        for (Negociateur n : this.getActiveNegociateurs()) {
                            n.setActive(false);
                        }
                        for (Negociateur n : proposedCoalitions) {
                            if (n instanceof Coalition) {
                                System.out.println(n.getName() + " proposed coalition " + n.getName());
                            } else {
                                System.out.println(n.getName() + " proposed to stay alone");
                            }
                            n.setActive(true);
                        }
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        epsilon *= decay;
                        refresh = 0;
                    }
                }
                this.nego_ended.set(false);
                Random rand = new Random();
                int prix = rand.nextInt(1000,2500) + 1000;
                Billet bil2 = generateBillet();
                newBillet(bil2);
                System.out.println(ANSI_GREEN +
                        "Ticket to be negotiated  : from : "+ bil2.getVilleProvenance() + " to : " +bil2.getVilleDestination()+ ", starting price : " + bil2.getPrix() + ANSI_RESET);
            }
        }
    }

    public Billet generateBillet() {
        Random rand = new Random();
        int prix = rand.nextInt(1000,2500) + 1000;
        int ville1 = rand.nextInt(0, listeVilles.size());
        int ville2 = rand.nextInt(0, listeVilles.size());
        while(ville1 == ville2){
            ville2 = rand.nextInt(0, listeVilles.size());
        }
        Billet bil = new Billet(prix, listeVilles.get(ville1), listeVilles.get(ville2), "MateB");
        return bil;

    }
    public List<Negociateur> findNotExploredCoalitions(Negociateur n){
        List<Negociateur> notExplored = new ArrayList<>();
        List<Negociateur> coalitions = new ArrayList<>();
        for(Negociateur nego : negociateurs){
            if(nego instanceof Coalition){
                coalitions.add(nego);
            }
        }
        for(Negociateur nego : coalitions){
            if(nego instanceof Coalition){
                if(!n.getResults_history().containsKey(nego)){
                    notExplored.add(nego);
                }
            }
        }
        return notExplored;
    }

    public boolean checkIfMembersOK(List<Negociateur> members, Negociateur coalition){
        boolean result = true;
        for(Negociateur n : members){
            result = result && n.isOK(coalition, epsilon);
        }
        return true;
    }

}