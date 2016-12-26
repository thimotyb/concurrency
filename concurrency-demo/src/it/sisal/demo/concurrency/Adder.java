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
	 * Il metodo è sincrono ma utilizza un executor framework per il parallelismo
	 * @param elements
	 * @return
	 */
	public Integer addAll(List<Integer> elements) {
		
		// Divide in due il dominio da sommare per farlo in parallelo
		// MAPPER: qui occorrerà trovare l'algoritmo di separazione del dominio
		AdderTask task1 = new AdderTask(elements.subList(0, elements.size()/2));
		AdderTask task2 = new AdderTask(elements.subList((elements.size()/2)+1, elements.size()-1));
		
        /**
         * Queste chiamate sono asincrone
         */
        Future<Integer> half1 = mes.submit(task1);
        Future<Integer> half2 = mes.submit(task2);
        
        try {
        	/**
        	 * Qui si inserisce il punto di attesa che chiude la catena asincrona
        	 * e sincronizza la riposta, prendendo i risultato delle due metà
        	 * 
        	 * REDUCER: Qui occorrerà trovare il criterio di ricomposizione dei risultati
        	 */
            Integer result = half1.get() + half2.get();
            return result;
        } catch (InterruptedException | ExecutionException ex) {
            throw new IllegalStateException("Cannot get the answer", ex);
        }
    }	

}
