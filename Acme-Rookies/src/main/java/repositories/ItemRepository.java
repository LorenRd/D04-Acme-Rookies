
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	@Query("select i from Item i where i.provider.id = ?1")
	Collection<Item> findByProviderId(int providerId);

	@Query("select avg(1.0*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Double avgItemsPerProvider();

	@Query("select min(1.0*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Double minItemsPerProvider();

	@Query("select max(1.0*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Double maxItemsPerProvider();

	@Query("select stddev(1.0*(select count(i) from Item i where i.provider.id = p.id)) from Provider p")
	Double stddevItemsPerProvider();
}
