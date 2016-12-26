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
                 * Simula una parte di elaborazione.
                 * Qui il punto sta se sia possibile prima suddividere il dominio
                 * di calcolo impostando un approccio Map/Reduce o Fork/Join
                 * 
                 * Qui la stream è usata per comodità per fare la somma, non per fare
                 * parallelismo che invece è indotto dal fatto di stare in una Callable
                 * 
                 * Le Callable dovranno essere fatte in modo da lavorare su parti
                 * del dominio da elaborare.
                 * Vedi esempio a call multiple su
                 * https://github.com/thimotyb/java8/blob/master/java8course/src/it/corso/concurrency/Example3.java
                 * 
                 */
                Integer mySum = elements.stream().reduce(0,  (a, b) -> a + b);
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
