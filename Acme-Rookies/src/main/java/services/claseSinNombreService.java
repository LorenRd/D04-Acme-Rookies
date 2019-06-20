package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

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
		if(!draft)
			claseSinNombre.setPublicationMoment(new Date(System.currentTimeMillis() - 1));
		
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
		result.setTicker(generateTicker());

		return result;
	}

	private String generateTicker() {
		String result;
		String numbers;

		final Random random = new Random();

		Date today = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(cal.get(Calendar.MONTH));
		String year = String.valueOf(cal.get(Calendar.YEAR));

		year.substring(2, 4);

		numbers = String.format("%04d", random.nextInt(10000));
		result = year + month + day + "-" + numbers;
		if (this.repeatedTicker(result))
			this.generateTicker();

		return result;
	}

	public boolean repeatedTicker(final String ticker) {
		Boolean isRepeated = false;
		int repeats;

		repeats = this.claseSinNombreRepository.findRepeatedTickers(ticker);

		if (repeats > 0)
			isRepeated = true;

		return isRepeated;
	}


	public void flush() {
		this.claseSinNombreRepository.flush();

	}

	public boolean exist(final int id) {
		return this.claseSinNombreRepository.exists(id);
	}

	public Collection<claseSinNombre> findByAudit(int auditId) {
		Collection<claseSinNombre> result;
		result = this.claseSinNombreRepository.findByAuditId(auditId);
		return result;
	}

	public Collection<claseSinNombre> findByAuditFinal(int auditId) {
		Collection<claseSinNombre> result;
		result = this.claseSinNombreRepository.findByAuditFinalId(auditId);
		return result;
	}


}
