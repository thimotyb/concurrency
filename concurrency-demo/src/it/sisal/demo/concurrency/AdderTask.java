package it.sisal.demo.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;


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
public class AdderTask implements Callable<Integer> {

	private List<Integer> elements;
	Logger logger = Logger.getLogger("AdderTask");
	
	public AdderTask(List<Integer> elements) {
		this.elements = elements;
	}
	
	@Override
	public Integer call() throws Exception {
		Integer mySum = elements.stream().reduce(0,  (a, b) -> a + b);
		logger.info("Summed: "+mySum);
        return mySum;
	}

}
