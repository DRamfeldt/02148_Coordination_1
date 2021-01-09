package main;
import org.jspace.*;

public class Supplier implements Runnable {

    Space catalogue;

    public Supplier(SequentialSpace catalogue){
        this.catalogue = catalogue;

    }

    public void request( String item, Integer amount) throws InterruptedException {
        Object[]  shipment = catalogue.query(new ActualField(item), new FormalField(Integer.class));

    }
    public void run(){
        while (true){
            
        }
    }
}