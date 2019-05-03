package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Audit;
import domain.Company;


@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {
	
	@Query("select a from Audit a where a.auditor.id = ?1")
	Collection<Audit> findAllByAuditorId(int auditorId);

	@Query("select a from Audit a join a.position ap where a.isDraft = 0 AND ap.company.id = ?1")
	Collection<Audit> findAllByCompanyId(int companyId);

	@Query("select a from Audit a where a.isDraft = 0 AND a.position.id = ?1")
	Collection<Audit> findAllByPositionId(int positionId);
	
	@Query("select avg(1.0*(select count(a) from Audit a where a.position.id = p.id)) from Position p")
	Double avgAuditScorePosition();
	
	@Query("select min(1.0*(select count(a) from Audit a where a.position.id = p.id)) from Position p")
	Double minAuditScorePosition();
	
	@Query("select max(1.0*(select count(a) from Audit a where a.position.id = p.id)) from Position p")
	Double maxAuditScorePosition();
	
	@Query("select stddev(1.0*(select count(a) from Audit a where a.position.id = p.id)) from Position p")
	Double stddevAuditScorePosition();
	
	@Query("select avg(1.0*(select count(a) from Audit a join a.position ap where ap.company.id = c.id)) from Company c")
	Double avgAuditScoreCompany();
	
	@Query("select min(1.0*(select count(a) from Audit a join a.position ap where ap.company.id = c.id)) from Company c")
	Double minAuditScoreCompany();
	
	@Query("select max(1.0*(select count(a) from Audit a join a.position ap where ap.company.id = c.id)) from Company c")
	Double maxAuditScoreCompany();
	
	@Query("select stddev(1.0*(select count(a) from Audit a join a.position ap where ap.company.id = c.id)) from Company c")
	Double stddevAuditScoreCompany();
	
	@Query("select c from Company c where c.score = (select max(c.score) from Company c)")
	Collection<Company> bestScoreCompanies();
	
	@Query("select avg(p.salaryOffered) from Position p where p.id = (select a.position from Audit a where a.score = (select max(a.score) from Audit a))")
	Double avgSalaryPositionsHighestAvgScore();
	
	

}
