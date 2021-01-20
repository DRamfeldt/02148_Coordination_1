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
    private Space centralRequests = new QueueSpace();
    private Space awaitingRequests = new QueueSpace();
    private Central c;


    public Supplier(Space catalogue, String name) {
        this.catalogue = catalogue;
        this.name = name;
    }

    public void sendRequest(String item, List<Supplier> alreadyVisited, int weight, List<Supplier> route, int amount) throws InterruptedException {
        requests.put(item, alreadyVisited, weight, route, amount);
    }

    public void sendCentralRequest(String item, List<Supplier> wayBack, int weight, List<Supplier> route, int amount) throws InterruptedException {
        centralRequests.put(item, wayBack, weight, route, amount);
    }

    public void addConnection(Supplier s, int i) throws InterruptedException {
        suppliers.put(s, i);
    }

    public void setCentral(Central c) {
        this.c = c;
    }


    public void search(String item, List<Supplier> alreadyVisited, int weight, int amount) throws InterruptedException {
        List<Object[]> connectiOri = suppliers.queryAll(new FormalField(Supplier.class), new FormalField(Integer.class));
        List<Object[]> connections = new ArrayList<>(connectiOri);
        alreadyVisited.add(this);
        for (Object[] i : connectiOri) {
            Supplier r = (Supplier) i[0];
            if (alreadyVisited.contains(r)) {
                connections.remove(i);
            }
        }
        if (catalogue.queryp(new ActualField(item)) != null) {
            List<Supplier> wayBack = new ArrayList<Supplier>(alreadyVisited);
            wayBack.remove(wayBack.size() - 1);
            wayBack.get(wayBack.size()-1).sendCentralRequest(item, wayBack, weight, alreadyVisited, amount);
            System.out.println(Green + name + Purple + " can and is supplying " + Cyan + amount + " " + item + Reset +" to "+Green + wayBack.get(wayBack.size()-1).name + Reset);
        } else {
            if (connections.isEmpty()) {
                List<Supplier> wayBack = new ArrayList<Supplier>(alreadyVisited);
                wayBack.remove(wayBack.size() - 1);
                wayBack.get(wayBack.size()-1).sendCentralRequest(item, wayBack, 10005000, alreadyVisited, amount);
                System.out.println(Green + name + Reset + " cannot supply " + Cyan + item + Reset + " and doesn't have any other suppliers, sending back to "+Green + wayBack.get(wayBack.size()-1).name + Reset);
            } else {
                int requestsSent = 0;
                System.out.println(Green + name + Reset + " cannot supply " + Cyan + item + Reset);
                for (Object[] p : connections) {
                    Supplier i = (Supplier) p[0];
                    Integer newWeight = (Integer) p[1];
                    if (!alreadyVisited.contains(i)) {
                        requestsSent++;
                        System.out.println(Green + name + Reset + " requested " + Cyan + amount + " " + item + Reset + " from " + Green + i.name + Reset);
                        i.sendRequest(item, alreadyVisited, weight + newWeight, alreadyVisited, amount);
                    }
                }
                if (requestsSent>0) {
                    System.out.println(Green + name + Purple + " return requested self for " + Cyan + item + Reset);
                    awaitingRequests.put(item, amount, requestsSent);
                }
            }
        }
    }


    public void run() {
        String item ="";
        int amount =0;
        Object[] req;
        int amountOfRequests=10000;
        int weight = 10000000;
        List<Supplier> route = new ArrayList<Supplier>();
        List<Supplier> wayBack = new ArrayList<Supplier>();
        boolean wayBackInit=false;
        while (true) {
            try {


                req = requests.getp(new FormalField(String.class),
                        new FormalField(List.class),
                        new FormalField(Integer.class),
                        new FormalField(List.class),
                        new FormalField(Integer.class));
                if (req != null) {
                    item = (String) req[0];
                    List<Supplier> alreadyVisited = (List<Supplier>) req[1];
                    weight = (int) req[2];
                    route = (List<Supplier>) req[3];
                    amount = (int) req[4];
                    System.out.println(Green + name + Reset + " has received request for " + Cyan + item + Reset);
                    search(item, alreadyVisited, weight, amount);
                }


                req = awaitingRequests.getp(new FormalField(String.class),
                        new FormalField(Integer.class),
                        new FormalField(Integer.class));
                if (req != null) {
                    item = (String) req[0];
                    amount = (int) req[1];
                    amountOfRequests = (int) req[2];
                    System.out.println(Green + name + Reset + " is wiring " + amountOfRequests + " requests for " + Cyan + item + Reset);
                }


                req = centralRequests.getp(new ActualField(item),
                        new FormalField(List.class),
                        new FormalField(Integer.class),
                        new FormalField(List.class),
                        new ActualField(amount));
                if (req!=null) {
                    System.out.println(name+" \nroute:"+c.stringify((List<Supplier>) req[3])+" \nwayBack"+c.stringify((List<Supplier>) req[1])+" \nweight : "+req[2]+" remaining reqs:"+amountOfRequests+" "+item);
                    amountOfRequests--;
                    if ((int) req[2] < weight) {
                        weight = (int) req[2];
                        wayBack = (List<Supplier>) req[1];
                        route = (List<Supplier>) req[3];
                        wayBackInit=true;
                    }
                }


                if (amountOfRequests<1 && wayBackInit) {
                    if (wayBack.size() <2) {
                        System.out.println("EARLY FIRE"+wayBack.size());
                        c.routeFound(item, route, amount, weight);
                    } else {
                        Supplier wb = wayBack.get(wayBack.size() - 1);
                        System.out.println(Green + name + Reset + " has returned request to " + Green + wb.name + Reset);
                        wb.sendCentralRequest(item, wayBack, weight, route, amount);
                    }
                    item ="";
                    amount =0;
                    amountOfRequests=10000;
                    weight = 10000000;
                    route = new ArrayList<Supplier>();
                    wayBack = new ArrayList<Supplier>();
                    wayBackInit = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

