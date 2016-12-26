package it.sisal.demo.concurrency;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService		
public class AdderService {

	@Inject Adder adder;
	
	/**
	 * Il web service simula una chiamata sincrona (ad es. RMI)
	 * @param elements
	 * @return
	 */
	@WebMethod
	public String adder(List<Integer> elements) {
		
		/**
		 * Il bean Adder offre una chiamata sincrona, ma al suo
		 * interno elabora in task paralleli la somma richiesta
		 */
		Integer result = adder.addAll(elements);
		return "Il risultato è "+result;
		
	}
	
}
