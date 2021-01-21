package main;

import org.jspace.*;

import java.util.ArrayList;

import java.util.List;

public class Supplier implements Runnable {
    public static final String Reset = "\u001B[0m";
    public static final String Red = "\u001B[31m";
    public static final String Green = "\u001B[32m";
    public static final String Blue = "\u001B[34m";
    public static final String Purple = "\u001B[35m";
    public static final String Cyan = "\u001B[36m";
    public static final String White = "\u001B[37m";

    private Space catalogue;                        // (String item) || (String item, ArrayList<String>)
    private Space suppliers = new QueueSpace();                        // Queue space : (Supplier s, int distance)
    public String name;
    private Space requests = new QueueSpace();
    private Central c;


    public Supplier(Space catalogue, String name) {
        this.catalogue = catalogue;
        this.name = name;
    }

    public void sendRequest(String item, List<Supplier> alreadyVisited, int weight, List<Supplier> route) throws InterruptedException {
        requests.put(item, alreadyVisited, weight, route);
    }

    public void addConnection(Supplier s, int i) throws InterruptedException {
        suppliers.put(s, i);
    }

    public void setCentral(Central c) {
        this.c = c;
    }


    public void search(String item, List<Supplier> aV, int weight, List<Supplier> rT) throws InterruptedException {
        List<Object[]> connections = suppliers.queryAll(new FormalField(Supplier.class), new FormalField(Integer.class));
        List<Supplier> alreadyVisited = new ArrayList<Supplier>(aV);
        List<Supplier> route = new ArrayList<Supplier>(rT);
        alreadyVisited.add(this);
        if (catalogue.queryp(new ActualField(item)) != null) {
            route.add(this);
            System.out.println(Green + name + Purple + " can supply " + Cyan + item + Reset);
            c.routeFound(item, route, weight);
        } else {
            if (connections.isEmpty()) {
                System.out.println(Green + name + Reset + " cannot supply " + Cyan + item + Reset + " and doesn't have any other suppliers");
            } else {
                route.add(this);
                List<Supplier> sent = new ArrayList<Supplier>();
                for (Object[] p : connections) {
                    Supplier i = (Supplier) p[0];
                    Integer newWeight = (Integer) p[1];
                    if (!alreadyVisited.contains(i)) {
                        sent.add(i);
                        i.sendRequest(item, alreadyVisited, weight + newWeight, route);
                    }
                }
                if (sent.size()>0) {
                    System.out.println(Green + name + Reset + " cannot supply " + Cyan + item + Reset + " but requested from " + Green + c.stringify(sent) + Reset);

                }else{
                    System.out.println(Green + name + Reset + " cannot supply " + Cyan + item + Reset + " and doesn't have any other suppliers");
                }
            }
        }
    }


    public void run() {
        while (true) {
            try {
                Object req[];
                req = requests.getp(new FormalField(String.class),
                        new FormalField(List.class),
                        new FormalField(Integer.class),
                        new FormalField(List.class));
                if (req != null) {
                    String item = (String) req[0];
                    List<Supplier> alreadyVisited = (List<Supplier>) req[1];
                    int weight = (int) req[2];
                    List<Supplier> route = (List<Supplier>) req[3];
                    //System.out.println(Green + name + Reset + " has received request for " + Cyan + item + Reset);
                    search(item, alreadyVisited, weight, route);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

