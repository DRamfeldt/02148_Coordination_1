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
        ArrayList<String> o = new ArrayList<String>();
        Space arlaProducts = new SequentialSpace();
        Space plasticProducts = new SequentialSpace();
        Space oilProducts = new SequentialSpace();
        Space stickers = new SequentialSpace();

        o = new ArrayList<String>();
        o.add("plastic");
        o.add("sticker");
        plasticProducts.put("container",o);


        oilProducts.put("plastic");

        stickers.put("sticker");

        o = new ArrayList<String>();
        o.add("container");
        arlaProducts.put("yogurt", o);
        arlaProducts.put("milk");

        Supplier Refinery = new Supplier(oilProducts, "Refinery");
        Supplier StickerFactory = new Supplier(stickers, "Sticker Factory");
        Supplier PlasticFactory = new Supplier(plasticProducts, "Plastic Factory");
        Supplier Arla = new Supplier(arlaProducts, "Arla");

        Arla.addConnection(StickerFactory,5);
        Refinery.addConnection(StickerFactory, 9);
        StickerFactory.addConnection(PlasticFactory,4);
        StickerFactory.addConnection(Arla,5);
        StickerFactory.addConnection(Refinery,9);



        Tuple centralNeighbors = new Tuple(Arla,Refinery);
        Central c = new Central(centralNeighbors);

        System.out.println("==========================");
        c.supplierRequest("yogurt",5);
        System.out.println("==========================");
        c.supplierRequest("container",1);
        System.out.println("==========================");
        c.supplierRequest("milk",3);
        System.out.println("==========================");
        c.supplierRequest("plastic",1000);

    }
}