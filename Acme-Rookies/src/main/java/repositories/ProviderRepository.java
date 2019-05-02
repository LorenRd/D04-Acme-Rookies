
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Provider;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

	@Query("select p from Provider p where p.userAccount.id = ?1")
	Provider findByUserAccountId(int userAccountId);

	@Query("select i.provider from Item i group by i.provider order by count(i.provider) desc")
	List<Provider> topFiveProvidersInItems();
}
