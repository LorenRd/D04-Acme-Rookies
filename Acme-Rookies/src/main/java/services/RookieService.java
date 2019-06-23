package services;

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

import repositories.CustomisationRepository;
import repositories.RookieRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.Application;
import domain.CreditCard;
import domain.Rookie;
import domain.Message;
import forms.RookieForm;

@Service
@Transactional
public class RookieService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private RookieRepository rookieRepository;

	@Autowired
	private UserAccountRepository useraccountRepository;

	@Autowired
	private CustomisationRepository customisationRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private AnswerService answerService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator validator;

	// Additional functions

	// Simple CRUD Methods

	public Rookie create() {
		Rookie result;
		CreditCard creditCard;

		result = new Rookie();
		creditCard = new CreditCard();

		// Nuevo userAccount con Member en la lista de authorities
		final UserAccount userAccount = this.actorService
				.createUserAccount(Authority.ROOKIE);

		result.setUserAccount(userAccount);
		result.setCreditCard(creditCard);

		return result;
	}

	public Rookie save(final Rookie rookie) {
		Rookie saved;
		UserAccount logedUserAccount;

		final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService
				.createUserAccount(Authority.ROOKIE);
		Assert.notNull(rookie, "rookie.not.null");

		if (rookie.getId() == 0) {
			CreditCard creditCard;
			rookie.getUserAccount().setPassword(
					passwordEncoder.encodePassword(rookie.getUserAccount()
							.getPassword(), null));
			creditCard = this.creditCardService.saveNew(rookie.getCreditCard());
			rookie.setCreditCard(creditCard);
			saved = this.rookieRepository.saveAndFlush(rookie);

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "rookie.notLogged");
			Assert.isTrue(logedUserAccount.equals(rookie.getUserAccount()),
					"rookie.notEqual.userAccount");
			saved = this.rookieRepository.findOne(rookie.getId());
			Assert.notNull(saved, "rookie.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername()
					.equals(rookie.getUserAccount().getUsername()));
			Assert.isTrue(saved.getUserAccount().getPassword()
					.equals(rookie.getUserAccount().getPassword()));
			saved = this.rookieRepository.saveAndFlush(rookie);
		}

		return saved;
	}
	 public void delete() {
		 /*
		  * Orden de borrado:
		  * 	1 Answer de las applications
		  * 	2 Application
		  * 	3 Mensajes 
		  * 	4 Rookie
		  * 	5 CC
		  */
		 Rookie principal;
		 Collection<Application> applications;
		 Collection<Message> messages;

		 principal = this.findByPrincipal();
		 Assert.notNull(principal);
		 		 
		 applications = this.applicationService.findAllApplicationsByRookieId(principal.getId());
		 for (Application a : applications) {
				if(a.getAnswer()!= null)
					this.answerService.delete(a.getAnswer());
		 }
		 this.applicationService.deleteInBatch(applications);	 
		 
		 messages = this.messageService.findBySenderId(principal.getId());
		 this.messageService.deleteInBach(messages);

		 
		 this.rookieRepository.delete(principal);
		 
		 this.creditCardService.delete(principal.getCreditCard());

	 }
	public Rookie findOne(final int rookieId) {
		Rookie result;

		result = this.rookieRepository.findOne(rookieId);
		Assert.notNull(result);
		return result;

	}

	public Collection<Rookie> findAll() {
		Collection<Rookie> result;

		result = this.rookieRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Rookie findByPrincipal() {
		Rookie result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.rookieRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;

	}

	public Rookie findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Rookie result;
		result = this.rookieRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.rookieRepository.exists(arg0);
	}

	public RookieForm construct(final Rookie rookie) {
		final RookieForm rookieForm = new RookieForm();
		rookieForm.setAddress(rookie.getAddress());
		rookieForm.setEmail(rookie.getEmail());
		rookieForm.setIdRookie(rookie.getId());
		rookieForm.setName(rookie.getName());
		rookieForm.setPhone(rookie.getPhone());
		rookieForm.setPhoto(rookie.getPhoto());
		rookieForm.setSurname(rookie.getSurname());
		rookieForm.setVatNumber(rookie.getVatNumber());
		rookieForm.setBrandName(rookie.getCreditCard().getBrandName());
		rookieForm.setCVV(rookie.getCreditCard().getCVV());
		rookieForm.setExpirationMonth(rookie.getCreditCard()
				.getExpirationMonth());
		rookieForm
				.setExpirationYear(rookie.getCreditCard().getExpirationYear());
		rookieForm.setHolderName(rookie.getCreditCard().getHolderName());
		rookieForm.setNumber(rookie.getCreditCard().getNumber());
		rookieForm.setCheckBox(rookieForm.getCheckBox());
		rookieForm.setUsername(rookie.getUserAccount().getUsername());
		return rookieForm;
	}

	public Rookie reconstruct(final RookieForm rookieForm,
			final BindingResult binding) {
		Rookie result;

		result = this.create();
		result.getUserAccount().setUsername(rookieForm.getUsername());
		result.getUserAccount().setPassword(rookieForm.getPassword());
		result.setAddress(rookieForm.getAddress());
		result.setEmail(rookieForm.getEmail());
		result.setName(rookieForm.getName());
		result.setPhoto(rookieForm.getPhoto());
		result.setSurname(rookieForm.getSurname());
		result.setVatNumber(rookieForm.getVatNumber());
		result.getCreditCard().setBrandName(rookieForm.getBrandName());
		result.getCreditCard().setCVV(rookieForm.getCVV());
		result.getCreditCard().setExpirationMonth(
				rookieForm.getExpirationMonth());
		result.getCreditCard()
				.setExpirationYear(rookieForm.getExpirationYear());
		result.getCreditCard().setHolderName(rookieForm.getHolderName());
		result.getCreditCard().setNumber(rookieForm.getNumber());

		if (!StringUtils.isEmpty(rookieForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(rookieForm.getPhone());
			if (matcher.matches())
				rookieForm.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ rookieForm.getPhone());
		}
		result.setPhone(rookieForm.getPhone());

		if (!rookieForm.getPassword().equals(rookieForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker",
					"member.validation.passwordsNotMatch",
					"Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(
				rookieForm.getUsername()).isEmpty())
			binding.rejectValue("username", "member.validation.usernameExists",
					"This username already exists");
		if (rookieForm.getCheckBox() == false)
			binding.rejectValue("checkBox", "member.validation.checkBox",
					"This checkbox must be checked");

		this.validator.validate(result, binding);
		this.rookieRepository.flush();

		return result;
	}

	public Rookie reconstructPruned(final Rookie rookie,
			final BindingResult binding) {
		Rookie result;

		if (rookie.getId() == 0)
			result = rookie;
		else
			result = this.rookieRepository.findOne(rookie.getId());
		result.setAddress(rookie.getAddress());
		result.setEmail(rookie.getEmail());
		result.setName(rookie.getName());
		result.setPhoto(rookie.getPhoto());
		result.setSurname(rookie.getSurname());
		result.setVatNumber(rookie.getVatNumber());

		if (!StringUtils.isEmpty(rookie.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$",
					Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(rookie.getPhone());
			if (matcher.matches())
				rookie.setPhone(this.customisationRepository.findAll()
						.iterator().next().getCountryCode()
						+ rookie.getPhone());
		}
		result.setPhone(rookie.getPhone());

		this.validator.validate(result, binding);
		this.rookieRepository.flush();
		return result;
	}

	public void flush() {
		this.rookieRepository.flush();
	}

	public Collection<Rookie> rookiesWithMoreApplications() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities()
				.contains(authority));
		Collection<Rookie> result;

		result = this.rookieRepository.rookiesWithMoreApplications();

		return result;
	}

}
