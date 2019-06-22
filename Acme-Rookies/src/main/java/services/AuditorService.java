package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AuditorRepository;
import repositories.CustomisationRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Administrator;
import domain.Audit;
import domain.Auditor;
import domain.CreditCard;
import domain.Message;
import domain.claseSinNombre;
import forms.AuditorForm;

@Service
@Transactional
public class AuditorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private AuditorRepository auditorRepository;
	
	@Autowired
	private UserAccountRepository useraccountRepository;
	
	@Autowired
	private CustomisationRepository customisationRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;

	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private MessageService messageService;
	 
	@Autowired
	private AuditService auditService;
	 
	@Autowired
	private claseSinNombreService claseSinNombreService;	
	
	@Autowired
	private Validator validator;
	
	 // Simple CRUD Methods

	 public boolean exists(final Integer arg0) {
		 return this.auditorRepository.exists(arg0);
	 }

	public Auditor create() {
		Auditor result;
		UserAccount userAccount;
		Authority authority;
		CreditCard creditCard;
		Administrator principal;
		
		principal = this.administratorService.findByPrincipal();
		Assert.isTrue(this.actorService.getAuthorityAsString(principal).equals("ADMIN"));

		result = new Auditor();
		userAccount = new UserAccount();
		authority = new Authority();
		creditCard = new CreditCard();

		authority.setAuthority("AUDITOR");
		userAccount.addAuthority(authority);

		Assert.notNull(userAccount);
		Assert.notNull(creditCard);

		result.setUserAccount(userAccount);
		result.setCreditCard(creditCard);

		return result;
	}

	public Auditor save(final Auditor auditor) {
		Auditor saved;
		UserAccount logedUserAccount;
		Md5PasswordEncoder encoder;

		encoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService.createUserAccount(Authority.AUDITOR);
		Assert.notNull(auditor, "auditor.not.null");

		if (this.exists(auditor.getId())) {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "auditor.notLogged");
			Assert.isTrue(logedUserAccount.equals(auditor.getUserAccount()),"company.notEqual.userAccount");
			saved = this.auditorRepository.findOne(auditor.getId());
			Assert.notNull(saved, "auditor.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(auditor.getUserAccount().getUsername()),"auditor.notEqual.username");
			Assert.isTrue(saved.getUserAccount().getPassword().equals(auditor.getUserAccount().getPassword()),"auditor.notEqual.password");

			saved = this.auditorRepository.save(auditor);

		} else {
			CreditCard creditCard;
			auditor.getUserAccount().setPassword(encoder.encodePassword(auditor.getUserAccount().getPassword(), null));
			creditCard = this.creditCardService.saveNew(auditor.getCreditCard());
			auditor.setCreditCard(creditCard);
			saved = this.auditorRepository.saveAndFlush(auditor);
		}
		return saved;
	}

	 public void delete() {

		 Auditor principal;
		 Collection<Audit> audits;
		 Collection<Message> messages;
		 Collection<claseSinNombre> claseSinNombres;
		 
		 principal = this.findByPrincipal();
		 Assert.notNull(principal);		 		
		 
		 audits = this.auditService.findAllByAuditor(principal.getId());
		 for (Audit a : audits) {
			 claseSinNombres = new ArrayList<claseSinNombre>();
			 claseSinNombres = this.claseSinNombreService.findByAudit(a.getId());
			 for (claseSinNombre cSN : claseSinNombres) {
				this.claseSinNombreService.delete2(cSN);
			}
		 }
		 this.auditService.deleteInBatch(audits);
		 
		 messages = this.messageService.findBySenderId(principal.getId());
		 this.messageService.deleteInBach(messages);

		 
		 this.auditorRepository.delete(principal);
		 
		 this.creditCardService.delete(principal.getCreditCard());

	 }

	public Auditor findOne(final int auditorId) {
		Auditor result;

		result = this.auditorRepository.findOne(auditorId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Auditor> findAll() {
		Collection<Auditor> result;

		result = this.auditorRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Auditor findByPrincipal() {
		Auditor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.auditorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;

	}

	public AuditorForm construct(final Auditor auditor) {
		final AuditorForm auditorForm = new AuditorForm();
		auditorForm.setAddress(auditor.getAddress());
		auditorForm.setEmail(auditor.getEmail());
		auditorForm.setIdAuditor(auditor.getId());
		auditorForm.setName(auditor.getName());
		auditorForm.setPhone(auditor.getPhone());
		auditorForm.setPhoto(auditor.getPhoto());
		auditorForm.setSurname(auditor.getSurname());
		auditorForm.setVatNumber(auditor.getVatNumber());
		auditorForm.setBrandName(auditor.getCreditCard()
				.getBrandName());
		auditorForm.setCVV(auditor.getCreditCard().getCVV());
		auditorForm.setExpirationMonth(auditor.getCreditCard()
				.getExpirationMonth());
		auditorForm.setExpirationYear(auditor.getCreditCard()
				.getExpirationYear());
		auditorForm.setHolderName(auditor.getCreditCard()
				.getHolderName());
		auditorForm.setNumber(auditor.getCreditCard().getNumber());
		auditorForm.setCheckBox(auditorForm.getCheckBox());
		auditorForm.setUsername(auditor.getUserAccount()
				.getUsername());
		return auditorForm;
	}

	public Auditor reconstruct(final AuditorForm auditorForm,
			final BindingResult binding) {
		Auditor result;

		result = this.create();
		result.getUserAccount().setUsername(auditorForm.getUsername());
		result.getUserAccount().setPassword(auditorForm.getPassword());
		result.setAddress(auditorForm.getAddress());
		result.setEmail(auditorForm.getEmail());
		result.setName(auditorForm.getName());
		result.setPhoto(auditorForm.getPhoto());
		result.setSurname(auditorForm.getSurname());
		result.setVatNumber(auditorForm.getVatNumber());
		result.getCreditCard().setBrandName(auditorForm.getBrandName());
		result.getCreditCard().setCVV(auditorForm.getCVV());
		result.getCreditCard().setExpirationMonth(
				auditorForm.getExpirationMonth());
		result.getCreditCard().setExpirationYear(
				auditorForm.getExpirationYear());
		result.getCreditCard().setHolderName(auditorForm.getHolderName());
		result.getCreditCard().setNumber(auditorForm.getNumber());

		if (!StringUtils.isEmpty(auditorForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(auditorForm
					.getPhone());
			if (matcher.matches())
				auditorForm.setPhone(this.customisationRepository
						.findAll().iterator().next().getCountryCode()
						+ auditorForm.getPhone());
		}
		result.setPhone(auditorForm.getPhone());

		if (!auditorForm.getPassword().equals(
				auditorForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker",
					"administrator.validation.passwordsNotMatch",
					"Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(
				auditorForm.getUsername()).isEmpty()
				|| auditorForm.getUsername().equals(
						LoginService.getPrincipal().getUsername()))
			binding.rejectValue("username",
					"administrator.validation.usernameExists",
					"This username already exists");
		if (auditorForm.getCheckBox() == false)
			binding.rejectValue("checkBox",
					"administrator.validation.checkBox",
					"This checkbox must be checked");

		this.validator.validate(result, binding);
		this.auditorRepository.flush();
		return result;
	}

	public Auditor reconstructPruned(final Auditor auditor,
			final BindingResult binding) {
		Auditor result;

		if (auditor.getId() == 0)
			result = auditor;
		else
			result = this.auditorRepository.findOne(auditor.getId());
		result.setAddress(auditor.getAddress());
		result.setEmail(auditor.getEmail());
		result.setName(auditor.getName());
		result.setPhoto(auditor.getPhoto());
		result.setSurname(auditor.getSurname());
		result.setVatNumber(auditor.getVatNumber());

		if (!StringUtils.isEmpty(auditor.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(auditor.getPhone());
			if (matcher.matches())
				auditor.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ auditor.getPhone());
		}
		result.setPhone(auditor.getPhone());

		this.validator.validate(result, binding);
		this.auditorRepository.flush();
		return result;
	}

	public void flush() {
		this.auditorRepository.flush();
	}



}
