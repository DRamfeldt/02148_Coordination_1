package main;

import org.jspace.*;

import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws InterruptedException {
        /*
        Space arlaProducts = new SequentialSpace();

        arlaProducts.put("milk");
        arlaProducts.put("yogurt");

        Space centralInv = new SequentialSpace();
        centralInv.put("milk",0);
        centralInv.put("yogurt",0);

        Central Netto = new Central(centralInv);
        Supplier Arla = new Supplier(arlaProducts);

        Netto.addSupplier(Arla, "milk");
        Netto.addSupplier(Arla, "yogurt");

        Space storeInv = new SequentialSpace();
        storeInv.put("milk", 20);
        storeInv.put("yogurt", 15);
        Space storeStdInv = new SequentialSpace();
        storeStdInv.put("milk", 15);
        storeStdInv.put("yogurt", 10);

        Store Søborg = new Store(storeInv, storeStdInv, Netto);


        Søborg.sale("yogurt", 12);
        new Thread(Netto).start();

        */
        Space arla = new SequentialSpace();
        arla.put("milk");
        arla.put("yogurt");
        Space egelykke = new SequentialSpace();
        egelykke.put("milk");
        egelykke.put("icecream");
        egelykke.put("cheese");
        Space stryhns = new SequentialSpace();
        stryhns.put("leverpostej");
        Space karenvolf = new SequentialSpace();
        karenvolf.put("snoefler");
        Space marabou = new SequentialSpace();
        marabou.put("chocolate");
        Space rittersport = new SequentialSpace();
        rittersport.put("chocolate");
        Space hansens = new SequentialSpace();
        hansens.put("icecream");
        Space smirnoff = new SequentialSpace();
        smirnoff.put("vodka");
        Space heinz = new SequentialSpace();
        heinz.put("ketchup");
        Space kellogs = new SequentialSpace();
        kellogs.put("corn flakes");

        Supplier Arla = new Supplier(arla,"Arla");
        Supplier Egelykke = new Supplier(egelykke,"Egelykke");
        Supplier Stryhns = new Supplier(stryhns,"Stryhns");
        Supplier KarenVolf = new Supplier(karenvolf,"Karen Volf");
        Supplier Marabou = new Supplier(marabou, "Marabou");
        Supplier RitterSport = new Supplier(rittersport,"Ritter Sport");
        Supplier Hansens = new Supplier(hansens,"Hansens flødeis");
        Supplier Smirnoff = new Supplier(smirnoff,"Smirnoff");
        Supplier Heinz = new Supplier(heinz,"Heinz");
        Supplier Kellogs = new Supplier(kellogs,"Kellogs");

        Stryhns.addConnection(Kellogs,5);
        Stryhns.addConnection(Heinz,2);
        Heinz.addConnection(Stryhns,2);
        //Heinz.addConnection(RitterSport,2);
        Heinz.addConnection(Kellogs,2);
        Kellogs.addConnection(Stryhns,5);
        Kellogs.addConnection(Heinz,2);
        Kellogs.addConnection(Smirnoff,1);
        //RitterSport.addConnection(Heinz,2);
        //RitterSport.addConnection(Arla,2);
        Smirnoff.addConnection(Kellogs,1);
        Smirnoff.addConnection(Hansens,2);
        Smirnoff.addConnection(Marabou,5);
        //Hansens.addConnection(Egelykke,7);
        Hansens.addConnection(Smirnoff,2);
        //Egelykke.addConnection(KarenVolf,1);
        //Egelykke.addConnection(Hansens,7);
        //Egelykke.addConnection(Arla,5);
        //Arla.addConnection(Egelykke,5);
        //Arla.addConnection(RitterSport,2);
        //KarenVolf.addConnection(Egelykke,1);
        Marabou.addConnection(Smirnoff,5);

        Tuple centralNeighbors = new Tuple(Stryhns);//,Hansens);
        Central c = new Central(centralNeighbors);

        Arla.setCentral(c);
        Egelykke.setCentral(c);
        Hansens.setCentral(c);
        Smirnoff.setCentral(c);
        RitterSport.setCentral(c);
        Kellogs.setCentral(c);
        Heinz.setCentral(c);
        Stryhns.setCentral(c);
        Marabou.setCentral(c);
        KarenVolf.setCentral(c);

        Space inventory = new SequentialSpace();

        Store s = new Store(inventory,inventory,c);
        //(new Thread(Arla)).start();
        //(new Thread(RitterSport)).start();
        (new Thread(Heinz)).start();
        (new Thread(Stryhns)).start();
        (new Thread(Kellogs)).start();
        (new Thread(Smirnoff)).start();
        (new Thread(Marabou)).start();
        (new Thread(Hansens)).start();
        //(new Thread(Egelykke)).start();
        //(new Thread(KarenVolf)).start();


        (new Thread(c)).start();
        c.request("ketchup",5,s);
        /*Thread.sleep(2000);
        System.out.println("==========================");
        c.request("chocolate",1,s);
        Thread.sleep(2000);
        System.out.println("==========================");
        c.request("vodka",3,s);
        Thread.sleep(2000);
        System.out.println("==========================");*/

    }
}