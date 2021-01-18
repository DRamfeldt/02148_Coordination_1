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
        Space egelykkeProducts = new SequentialSpace();
        Space plasticProducts = new SequentialSpace();
        Space oilProducts = new SequentialSpace();
        Space stickers = new SequentialSpace();
        Space woodProducts = new SequentialSpace();
        Space ikea = new SequentialSpace();


        o = new ArrayList<String>();
        o.add("plastic");
        o.add("sticker");
        plasticProducts.put("container",o);

        woodProducts.put("legs");

        o = new ArrayList<String>();
        o.add("legs");
        ikea.put("table",o);

        oilProducts.put("plastic");

        stickers.put("sticker");

        egelykkeProducts.put("milk");

        o = new ArrayList<String>();
        o.add("container");
        arlaProducts.put("yogurt", o);
        arlaProducts.put("milk");

        Supplier StickerFactory = new Supplier(stickers, "Sticker Factory");
        Supplier PlasticFactory = new Supplier(plasticProducts, "Plastic Factory");
        Supplier Arla = new Supplier(arlaProducts, "Arla");
        Supplier Egelykke = new Supplier(egelykkeProducts, "Egelykke");
        Supplier Ikea = new Supplier(ikea,"Ikea");
        Supplier Refinery1 = new Supplier(oilProducts, "Refinery 1");
        Supplier Refinery2 = new Supplier(oilProducts, "Refinery 2");
        Supplier Woodshop1 = new Supplier(woodProducts, "Woodshop 1");
        Supplier Woodshop2 = new Supplier(woodProducts, "Woodshop 2");

        Arla.addConnection(StickerFactory,5);
        Refinery1.addConnection(StickerFactory, 9);
        Refinery1.addConnection(Egelykke,1);
        Egelykke.addConnection(Ikea,4);
        Egelykke.addConnection(Refinery1,1);
        Egelykke.addConnection(Woodshop1,15);
        Woodshop1.addConnection(Egelykke,15);
        Ikea.addConnection(Egelykke,4);
        StickerFactory.addConnection(PlasticFactory,4);
        StickerFactory.addConnection(Refinery2,2);
        StickerFactory.addConnection(Woodshop2,1);
        StickerFactory.addConnection(Arla,5);
        StickerFactory.addConnection(Refinery1,9);
        Woodshop2.addConnection(StickerFactory,1);
        Refinery2.addConnection(StickerFactory,2);
        PlasticFactory.addConnection(StickerFactory,4);




        Tuple centralNeighbors = new Tuple(Arla,Refinery1,Ikea);
        Central c = new Central(centralNeighbors);

        System.out.println("==========================");
        c.supplierRequest("sticker",5);
        System.out.println("==========================");
        c.supplierRequest("container",1);
        System.out.println("==========================");
        c.supplierRequest("milk",3);
        System.out.println("==========================");
        c.supplierRequest("plastic",1000);

    }
}