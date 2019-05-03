
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PositionRepository;
import security.Authority;
import domain.Actor;
import domain.Application;
import domain.Audit;
import domain.Company;
import domain.Position;
import domain.Rookie;

@Service
@Transactional
public class PositionService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private PositionRepository	positionRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private RookieService		rookieService;

	@Autowired
	private Validator validator;
	
	@Autowired
	private AuditService auditService;

	


	// Simple CRUD Methods

	public boolean exist(final Integer positionId) {
		return this.positionRepository.exists(positionId);
	}

	public Position findOne(final int positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> result;

		result = this.positionRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Position create() {
		Position result;
		final Company principal;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		result = new Position();
		result.setTicker(this.generateTicker(principal));
		result.setStatus("DRAFT");
		result.setCompany(principal);
		return result;
	}

	public Position save(final Position position, final String saveMode) {
		Company principal;
		Position result;
		int numProblems = 0;

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(position);
		Assert.isTrue(position.getCompany() == principal);

		if (position.getProblems() != null)
			numProblems = position.getProblems().size();

		if (saveMode.equals("CANCELLED"))
			Assert.isTrue(position.getStatus().equals("FINAL"));
		if (saveMode.equals("FINAL"))
			Assert.isTrue(numProblems >= 2);

		position.setStatus(saveMode);

		result = this.positionRepository.save(position);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Position position) {
		Company principal;
		Collection<Application> applications;

		Assert.notNull(position);

		principal = this.companyService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(position.getCompany().getId() == principal.getId());

		applications = this.applicationService.findAllByPositionId(position.getId());

		for (final Application a : applications)
			this.applicationService.delete(a);

		this.positionRepository.delete(position);
	}

	// Business Methods

	public Collection<Position> findByCompany(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findByCompanyId(companyId);
		return result;
	}

	public Collection<Position> findAllFinal() {
		Collection<Position> result;

		result = this.positionRepository.findAllFinal();
		return result;
	}

	public Collection<Position> findAllFinalNotApplication() {
		Collection<Position> result;
		Collection<Application> applications;
		Collection<Position> positions;

		Rookie principal;
		principal = this.rookieService.findByPrincipal();

		applications = this.applicationService.findAllApplicationsByRookieId(principal.getId());
		result = this.positionRepository.findAllFinal();
		positions = this.positionRepository.findAllFinal();

		for (Position p : positions) {
			for (Application a : applications) {
				if(a.getPosition().getId() == p.getId())
					result.remove(p);
		return result;
	}
	
	public Collection<Position> findAllFinalNotAudit() {
		Collection<Position> positions;
		Collection<Audit> audits;
		Collection<Position> result;
		
		audits = this.auditService.findAll();
		positions = this.positionRepository.findAllFinal();
		result = this.positionRepository.findAllFinal();
		
		for (Position p : positions) {
			for (Audit a : audits) {
				if(a.getPosition().getId() == p.getId())
					result.remove(p);
			}
		}
		return result;
	}

	public Collection<Position> findAvailableByCompanyId(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findAvailableByCompanyId(companyId);
		return result;
	}

	public Collection<Position> findByKeywordAll(final String keyword, final int companyId) {
		final Collection<Position> result = this.positionRepository.findByKeyword(keyword, companyId);

		return result;
	}
	public Collection<Position> findByKeywordFinal(final String keyword) {
		final Collection<Position> result = this.positionRepository.findByKeywordFinal(keyword);

		return result;
	}
	public Collection<Position> findByKeywordFinalCompany(final String keyword, final int companyId) {
		final Collection<Position> result = this.positionRepository.findByKeywordFinalCompany(keyword, companyId);

		return result;
	}

	public Collection<Position> findAllFinalCompany(final int companyId) {
		Collection<Position> result;

		result = this.positionRepository.findAllFinalCompany(companyId);
		return result;
	}

	public Collection<Position> findByProblemId(final int problemId) {
		Collection<Position> result;

		result = this.positionRepository.findByProblemId(problemId);
		return result;
	}

	public Collection<Position> findFinalByProblemId(final int problemId) {
		Collection<Position> result;
		result = this.positionRepository.findFinalByProbemId(problemId);
		return result;
	}

	private String generateTicker(final Company company) {
		String result;
		String text;
		String numbers;

		text = company.getCommercialName().toUpperCase();
		final Random random = new Random();

		if (text.length() < 4)
			while (text.length() < 4)
				text.concat("X");
		else if (text.length() > 4)
			text = text.substring(0, 4);

		numbers = String.format("%04d", random.nextInt(10000));
		result = text + "-" + numbers;
		if (this.repeatedTicker(company, result))
			this.generateTicker(company);

		return result;
	}

	public boolean repeatedTicker(final Company company, final String ticker) {
		Boolean isRepeated = false;
		int repeats;

		repeats = this.positionRepository.findRepeatedTickers(company.getId(), ticker);

		if (repeats > 0)
			isRepeated = true;

		return isRepeated;
	}

	public Position reconstruct(final Position position,
			final BindingResult binding) {
		Position original;
		if (position.getId() == 0) {
			original = position;
			original.setCompany(this.companyService.findByPrincipal());
			original.setTicker(this.generateTicker(original.getCompany()));
			original.setStatus("DRAFT");
		} else{
			original = this.positionRepository.findOne(position.getId());
			position.setTicker(original.getTicker());
			position.setCompany(this.companyService.findByPrincipal());
			position.setStatus("DRAFT");

		
			if(position.getDeadline().before(Calendar.getInstance().getTime()))
				binding.rejectValue("deadline", "application.validation.deadline", "Deadline must be future");
			if (position.getTechnologiesRequired().isEmpty())
				binding.rejectValue("technologiesRequired", "application.validation.technologiesRequired", "Must not be blank");
			if (position.getSkillsRequired().isEmpty())
				binding.rejectValue("skillsRequired", "application.validation.skillsRequired", "Must not be blank");

		
		}

		
		this.validator.validate(position, binding);

		return position;
	}

	public void flush() {
		this.positionRepository.flush();
	}

	public Double avgPositionsPerCompany() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.avgPositionsPerCompany();

		return result;
	}

	public Double minPositionsPerCompany() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.minPositionsPerCompany();

		return result;
	}

	public Double maxPositionsPerCompany() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.maxPositionsPerCompany();

		return result;
	}

	public Double stddevPositionsPerCompany() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.stddevPositionsPerCompany();

		return result;
	}

	public Double avgSalariesOffered() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.avgSalariesOffered();

		return result;
	}

	public Double minSalariesOffered() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.minSalariesOffered();

		return result;
	}

	public Double maxSalariesOffered() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.maxSalariesOffered();

		return result;
	}

	public Double stddevSalariesOffered() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.positionRepository.stddevSalariesOffered();

		return result;
	}

	public Position bestSalaryPosition() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Position result = null;

		if(this.positionRepository.bestSalaryPosition().size()>0)
			result = this.positionRepository.bestSalaryPosition().iterator().next();

		return result;
	}

	public Position worstSalaryPosition() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Position result = null;
		
		if(this.positionRepository.worstSalaryPosition().size()>0)
			result = this.positionRepository.worstSalaryPosition().iterator().next();

		return result;
	}
	public void deleteInBatch(final Collection<Position> positions) {
		this.positionRepository.deleteInBatch(positions);
	}
}
