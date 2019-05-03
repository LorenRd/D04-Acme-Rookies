
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditRepository;
import domain.Audit;
import domain.Auditor;

@Service
@Transactional
public class AuditService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private AuditRepository	 auditRepository;

	@Autowired
	private Validator				validator;

	// Supporting services ----------------------------------------------------
	@Autowired
	private AuditorService			auditorService;

	// Simple CRUD Methods
	public void delete(final Audit audit) {

		Assert.notNull(audit);
		Assert.isTrue(audit.getId() != 0);
		Assert.isTrue(audit.getIsDraft());

		this.auditRepository.delete(audit);
	}

	public Audit findOne(final int auditId) {
		Audit result;

		result = this.auditRepository.findOne(auditId);
		Assert.notNull(result);
		return result;

	}

	public Audit save(final Audit audit, final Boolean draft) {
		Audit result;
		Auditor principal;

		principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(audit);
		Assert.isTrue(audit.getAuditor() == principal);
		audit.setIsDraft(draft);

		result = this.auditRepository.save(audit);
		Assert.notNull(result);
		return result;
	}


	// Additional functions

	public Collection<Audit> findAllByAuditor (final int auditorId){
		Collection<Audit> result;
		result = this.auditRepository.findAllByAuditorId(auditorId);
		
		return result;
	}
	
	
	public Collection<Audit> findAllByCompany (final int companyId){
		Collection<Audit> result;
		result = this.auditRepository.findAllByCompanyId(companyId);
		
		return result;
	}
	
	public Collection<Audit> findAllByPosition (final int positionId){
		Collection<Audit> result;
		result = this.auditRepository.findAllByPositionId(positionId);
		
		return result;
	}
	
	public Collection<Audit> findAll (){
		Collection<Audit> result;
		result = this.auditRepository.findAll();
		
		return result;
	}
 	

	public Audit reconstruct(final Audit audit, final BindingResult binding) {
		Audit result;
		if (audit.getId() == 0) {
			result = audit;
			result.setAuditor(this.auditorService.findByPrincipal());
			result.setIsDraft(true);
			result.setMoment(new Date(System.currentTimeMillis() - 1));
			result.setPosition(audit.getPosition());
			
			if (audit.getPosition()== null)
				binding.rejectValue("position", "audit.validation.position", "Can't be null");

		} else{
			result = this.auditRepository.findOne(audit.getId());
	
			result.setText(audit.getText());
			result.setScore(audit.getScore());

		
		}
		
		this.validator.validate(result, binding);

		return result;
	}

	public Audit create() {
		Audit result;
		final Auditor principal;

		principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal);

		result = new Audit();
		result.setIsDraft(true);
		result.setAuditor(principal);
		result.setScore(0.0);
		result.setMoment(new Date(System.currentTimeMillis() - 1));

		return result;
	}


	public void deleteInBatch(Collection<Audit> audits){
		this.auditRepository.deleteInBatch(audits);
	}
}
