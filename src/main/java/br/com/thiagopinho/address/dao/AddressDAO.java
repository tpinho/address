package br.com.thiagopinho.address.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.thiagopinho.address.model.Address;
import br.com.thiagopinho.address.util.Pagination;

@Stateless
public class AddressDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public Address find(Long id) {
		return entityManager.find(Address.class, id);
	}

	public void remove(Long id) {
		entityManager.remove(find(id));
	}

	public Address save(Address address) {
		if (address.getId() == null)
			entityManager.persist(address);
		else
			entityManager.merge(address);
		
		return address;
	}
	
	private Integer countAddresses() {
        Query query = entityManager.createQuery("SELECT COUNT(a.id) FROM Address a");
        return ((Long) query.getSingleResult()).intValue();
    }
	
	public Pagination findAddresses(Pagination pagination) {
		pagination.setTotalResults(countAddresses());
		int start = (pagination.getCurrentPage() - 1)
				* pagination.getPageSize();
		pagination.setList(findAddresses(start, pagination.getPageSize(),
				pagination.getSortFields(), pagination.getSortDirections()));
		return pagination;
	}

	@SuppressWarnings("unchecked")
	private List<Address> findAddresses(int startPosition, int maxResults,
			String sortFields, String sortDirections) {
		Query query = entityManager
				.createQuery("SELECT a FROM Address a ORDER BY a." + sortFields
						+ " " + sortDirections);
		query.setFirstResult(startPosition);
		query.setMaxResults(maxResults);
		return query.getResultList();
	}
	
}
