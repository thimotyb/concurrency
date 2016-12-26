package it.sisal.demo.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

/**
 * Un normale EJB3.x stateless che richiama un executor come risorsa
 * per l'esecuzione di thread paralleli
 * @author thimo
 *
 */
@Stateless
public class Adder {

	@Resource
    private ManagedExecutorService mes;
	
	/**
	 * Il metodo è sincrono ma utilizza un parallel framework
	 * @param elements
	 * @return
	 */
	public Integer addAll(List<Integer> elements) {
		
		Callable theAnswer = new Callable() {

            @Override
            public Integer call() throws Exception {
                /**
                 * Simula una elaborazione parallela usando la stream api
                 * Qui il punto sta se sia possibile suddividere il dominio
                 * di calcolo impostando un approccio Map/Reduce o Fork/Join
                 * 
                 * Qui uso una lambda di riduzione molto semplice, ma usando
                 * una method reference si possono creare lambda più articolate
                 */
                Integer mySum = elements.stream().parallel().reduce(0,  (a, b) -> a + b);
                return mySum;
            }
        };
        /**
         * Questa chiamata è asincrona
         */
        Future futureResult = mes.submit(theAnswer);
        try {
        	/**
        	 * Qui si inserisce il punto di attesa che chiude la catena asincrona
        	 * e sincronizza la riposta
        	 */
            return (Integer) futureResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new IllegalStateException("Cannot get the answer", ex);
        }
    }	

}
