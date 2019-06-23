
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.claseSinNombre;

@Repository
public interface claseSinNombreRepository extends JpaRepository<claseSinNombre, Integer> {

	@Query("select c from claseSinNombre c where c.isDraft = 0 AND c.position.id = ?1")
	Collection<claseSinNombre> findByPositionId(int positionId);

	@Query("select c from claseSinNombre c where c.position.id = ?1")
	Collection<claseSinNombre> findAllByPositionId(int positionId);

	@Query("select c from claseSinNombre c where c.rookie.id = ?1")
	Collection<claseSinNombre> findByRookieId(int rookieId);

	@Query("select count(c) from claseSinNombre c where c.ticker = ?1")
	Integer findRepeatedTickers(String ticker);
}
