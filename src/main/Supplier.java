package main;

import org.jspace.*;

public class Supplier implements Runnable {

    private Space catalogue;
    private Space requests = new SequentialSpace();

    public Supplier(Space catalogue) {
        this.catalogue = catalogue;
    }

    public void Request(String Item, Integer Amount) throws InterruptedException {
        requests.put(Item, Amount);
    }

    public void run() {
        while (true) {
            try{
            Object[] request = requests.get(new FormalField(String.class), new FormalField(Integer.class));
            Object itemInCatalogue = catalogue.queryp(new ActualField(request));
            String item    = (String)  request[0];
            Integer amount = (Integer) request[1];
            if (itemInCatalogue != null) {
                Central.AddToInventory(item, amount);
            } else {
                System.out.println("Supplier item not available");
            }  } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}