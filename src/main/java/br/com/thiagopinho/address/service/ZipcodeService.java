package br.com.thiagopinho.address.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

import br.com.thiagopinho.address.model.Address;

@Stateless
public class ZipcodeService {

	private static Map<String, Address> zipcodes;
	
	static {
		zipcodes = new HashMap<String, Address>();
		
		Address address = new Address();
		address.setStreet("Ladeira da constituição");
		address.setNeighborhood("Centro");
		address.setCity("São Paulo");
		address.setState("SP");
		address.setZipcode("01030010");
		zipcodes.put(address.getZipcode(), address);
		
		Address address2 = new Address();
		address2.setStreet("AC Central de Brasília");
		address2.setCity("Brasília");
		address2.setState("DF");
		address2.setZipcode("70040976");
		zipcodes.put(address2.getZipcode(), address2);
	}
	
	public Address findByZipcode(String zipcode) {
		return zipcodes.get(zipcode);
	}
	
}
