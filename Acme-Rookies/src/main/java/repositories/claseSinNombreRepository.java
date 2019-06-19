
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.claseSinNombre;

@Repository
public interface claseSinNombreRepository extends JpaRepository<claseSinNombre, Integer> {
	@Query("select a from claseSinNombre a join a.audit aa where a.isDraft = 0 AND aa.auditor.id = ?1")
	Collection<claseSinNombre> findAllByAuditorId(int auditorId);

	@Query("select a from claseSinNombre a join a.audit aa join aa.position aap where aap.company.id = ?1")
	Collection<claseSinNombre> findAllByCompanyId(int companyId);

}
