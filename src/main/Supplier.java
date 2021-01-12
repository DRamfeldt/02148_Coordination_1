package main;

import org.jspace.*;

public class Supplier implements Runnable {
    private Space catalogue;
    private Space requests = new SequentialSpace();

    public Supplier(Space catalogue) {
        this.catalogue = catalogue;
    }

    public void request(String Item, Integer Amount, Central central) throws InterruptedException {
        requests.put(Item, Amount, central);
    }

    public void run() {
        while (true) {
            try{
            Object[] request = requests.get(new FormalField(String.class), new FormalField(Integer.class));
            Object itemInCatalogue = catalogue.queryp(new ActualField(request));
            String item    = (String)  request[0];
            Integer amount = (Integer) request[1];
            if (itemInCatalogue != null) {
                System.out.println("This item is not in the catalogue.");
            } else {
                System.out.println("Supplier item not available");
            }  } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}