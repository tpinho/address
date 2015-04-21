package br.com.thiagopinho.address.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import br.com.thiagopinho.address.dao.AddressDAO;
import br.com.thiagopinho.address.model.Address;
import br.com.thiagopinho.address.service.ZipcodeService;
import br.com.thiagopinho.address.util.Pagination;

@Stateless
@ApplicationPath("/resources")
@Path("addresses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AddressResource extends Application {

	@EJB
	private AddressDAO addressDAO;
	
	@EJB
	private ZipcodeService zipcodeService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Pagination listAddresses(
			@DefaultValue("1") @QueryParam("page") Integer page,
			@DefaultValue("id") @QueryParam("sortFields") String sortFields,
			@DefaultValue("asc") @QueryParam("sortDirections") String sortDirections) {
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page);
		pagination.setSortFields(sortFields);
		pagination.setSortDirections(sortDirections);
		pagination.setPageSize(10);
		return addressDAO.findAddresses(pagination);
	}

	@GET
	@Path("{id}")
	public Address getAddress(@PathParam("id") Long id) {
		return addressDAO.find(id);
	}

	@GET
	@Path("/searchAddress")
	@Produces(MediaType.APPLICATION_JSON)
	public Address searchAddress(@QueryParam("zipcode") String zipcode) throws Exception {
		Address address = zipcodeService.findByZipcode(zipcode);
		
		if (address == null)
			throw new Exception("Zipcode Uknown");
		
		return address;
	}
	
	@POST
    public Address saveAddress(Address address) {
		addressDAO.save(address);

        return address;
    }
	
	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") Long id) {
		addressDAO.remove(id);
	}

}