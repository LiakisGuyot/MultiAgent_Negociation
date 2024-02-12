package org.multiagent;

public class main {
    public static void main(String[] args) {
        Boolean allowCoalition = Boolean.TRUE;
        Environment env = new Environment(allowCoalition);
        env.run();
    }
}
