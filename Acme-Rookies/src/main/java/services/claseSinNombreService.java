package services;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.claseSinNombreRepository;
import domain.Company;
import domain.claseSinNombre;

@Service
@Transactional
public class claseSinNombreService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private claseSinNombreRepository claseSinNombreRepository;
	

	// Supporting services ----------------------------------------------------

	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private Validator validator;
	

	// Simple CRUD Methods
	public void delete(final claseSinNombre claseSinNombre) {
		Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(principal.getId() == claseSinNombre.getAudit().getPosition().getCompany().getId());
		Assert.notNull(claseSinNombre);
		Assert.isTrue(claseSinNombre.getId() != 0);
		Assert.isTrue(claseSinNombre.getIsDraft());

		this.claseSinNombreRepository.delete(claseSinNombre);
	}

	public void delete2(final claseSinNombre claseSinNombre) {

		this.claseSinNombreRepository.delete(claseSinNombre);
	}

	public claseSinNombre findOne(final int claseSinNombreId) {
		claseSinNombre result;

		result = this.claseSinNombreRepository.findOne(claseSinNombreId);
		Assert.notNull(result);
		return result;

	}

	public claseSinNombre save(final claseSinNombre claseSinNombre, final Boolean draft) {
		claseSinNombre result;
		Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(claseSinNombre);
		Assert.isTrue(claseSinNombre.getAudit().getPosition().getCompany().getId() == principal.getId());
		claseSinNombre.setIsDraft(draft);

		result = this.claseSinNombreRepository.save(claseSinNombre);
		Assert.notNull(result);
		return result;
	}

	// Additional functions

	public Collection<claseSinNombre> findAllByAuditor(final int auditorId) {
		Collection<claseSinNombre> result;
		result = this.claseSinNombreRepository.findAllByAuditorId(auditorId);

		return result;
	}

	public Collection<claseSinNombre> findAllByCompany(final int companyId) {
		Collection<claseSinNombre> result;
		result = this.claseSinNombreRepository.findAllByCompanyId(companyId);

		return result;
	}

	public Collection<claseSinNombre> findAll() {
		Collection<claseSinNombre> result;
		result = this.claseSinNombreRepository.findAll();

		return result;
	}

	public claseSinNombre reconstruct(final claseSinNombre claseSinNombre, final BindingResult binding) {
		claseSinNombre result;
		if (claseSinNombre.getId() == 0) {
			result = claseSinNombre;
			result.setBody(claseSinNombre.getBody());
			result.setPicture(claseSinNombre.getPicture());
			result.setTicker(claseSinNombre.getTicker());
			result.setAudit(claseSinNombre.getAudit());
			result.setIsDraft(true);

			if (claseSinNombre.getAudit() == null)
				binding.rejectValue("audit", "claseSinNombre.validation.audit", "Can't be null");

		} else {
			result = this.claseSinNombreRepository.findOne(claseSinNombre.getId());

			result.setBody(claseSinNombre.getBody());
			result.setPicture(claseSinNombre.getPicture());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public claseSinNombre create() {
		claseSinNombre result;
		final Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		result = new claseSinNombre();
		result.setIsDraft(true);

		return result;
	}



	public void flush() {
		this.claseSinNombreRepository.flush();

	}

	public boolean exist(final int id) {
		return this.claseSinNombreRepository.exists(id);
	}

}
