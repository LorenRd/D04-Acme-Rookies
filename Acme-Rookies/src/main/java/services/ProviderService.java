
package services;

import java.util.Collection;
import java.util.List;
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
import repositories.ProviderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Item;
import domain.Message;
import domain.Provider;
import forms.ProviderForm;

@Service
@Transactional
public class ProviderService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ProviderRepository		providerRepository;

	@Autowired
	private UserAccountRepository	useraccountRepository;

	@Autowired
	private CustomisationRepository	customisationRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ItemService				itemService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;


	// Additional functions

	// Simple CRUD Methods

	public Provider create() {
		Provider result;
		CreditCard creditCard;

		result = new Provider();
		creditCard = new CreditCard();

		// Nuevo userAccount con provider en la lista de authorities
		final UserAccount userAccount = this.actorService.createUserAccount(Authority.PROVIDER);

		result.setUserAccount(userAccount);
		result.setCreditCard(creditCard);

		return result;
	}

	public Provider save(final Provider provider) {
		Provider saved;
		UserAccount logedUserAccount;

		final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
		logedUserAccount = this.actorService.createUserAccount(Authority.PROVIDER);
		Assert.notNull(provider, "provider.not.null");

		if (provider.getId() == 0) {
			CreditCard creditCard;
			provider.getUserAccount().setPassword(passwordEncoder.encodePassword(provider.getUserAccount().getPassword(), null));
			creditCard = this.creditCardService.saveNew(provider.getCreditCard());
			provider.setCreditCard(creditCard);
			saved = this.providerRepository.saveAndFlush(provider);

		} else {
			logedUserAccount = LoginService.getPrincipal();
			Assert.notNull(logedUserAccount, "provider.notLogged");
			Assert.isTrue(logedUserAccount.equals(provider.getUserAccount()), "provider.notEqual.userAccount");
			saved = this.providerRepository.findOne(provider.getId());
			Assert.notNull(saved, "provider.not.null");
			Assert.isTrue(saved.getUserAccount().getUsername().equals(provider.getUserAccount().getUsername()));
			Assert.isTrue(saved.getUserAccount().getPassword().equals(provider.getUserAccount().getPassword()));
			saved = this.providerRepository.saveAndFlush(provider);
		}

		return saved;
	}
	public void delete() {

		Provider principal;
		Collection<Item> items;
		Collection<Message> messages;

		principal = this.findByPrincipal();
		Assert.notNull(principal);

		items = this.itemService.findByProvider(principal.getId());

		this.itemService.deleteInBatch(items);

		messages = this.messageService.findBySenderId(principal.getId());
		this.messageService.deleteInBach(messages);

		this.providerRepository.delete(principal);

		this.creditCardService.delete(principal.getCreditCard());
	}

	public Provider findOne(final int providerId) {
		Provider result;

		result = this.providerRepository.findOne(providerId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Provider> findAll() {
		Collection<Provider> result;

		result = this.providerRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Provider findByPrincipal() {
		Provider result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.providerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Provider findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);
		Provider result;
		result = this.providerRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public boolean exists(final Integer arg0) {
		return this.providerRepository.exists(arg0);
	}

	public ProviderForm construct(final Provider provider) {
		final ProviderForm providerForm = new ProviderForm();
		providerForm.setAddress(provider.getAddress());
		providerForm.setEmail(provider.getEmail());
		providerForm.setIdProvider(provider.getId());
		providerForm.setName(provider.getName());
		providerForm.setPhone(provider.getPhone());
		providerForm.setPhoto(provider.getPhoto());
		providerForm.setSurname(provider.getSurname());
		providerForm.setMake(provider.getMake());
		providerForm.setVatNumber(provider.getVatNumber());
		providerForm.setBrandName(provider.getCreditCard().getBrandName());
		providerForm.setCVV(provider.getCreditCard().getCVV());
		providerForm.setExpirationMonth(provider.getCreditCard().getExpirationMonth());
		providerForm.setExpirationYear(provider.getCreditCard().getExpirationYear());
		providerForm.setHolderName(provider.getCreditCard().getHolderName());
		providerForm.setNumber(provider.getCreditCard().getNumber());
		providerForm.setCheckBox(providerForm.getCheckBox());
		providerForm.setUsername(provider.getUserAccount().getUsername());
		return providerForm;
	}

	public Provider reconstruct(final ProviderForm providerForm, final BindingResult binding) {
		Provider result;

		result = this.create();
		result.getUserAccount().setUsername(providerForm.getUsername());
		result.getUserAccount().setPassword(providerForm.getPassword());
		result.setAddress(providerForm.getAddress());
		result.setEmail(providerForm.getEmail());
		result.setName(providerForm.getName());
		result.setPhoto(providerForm.getPhoto());
		result.setSurname(providerForm.getSurname());
		result.setMake(providerForm.getMake());
		result.setVatNumber(providerForm.getVatNumber());
		result.getCreditCard().setBrandName(providerForm.getBrandName());
		result.getCreditCard().setCVV(providerForm.getCVV());
		result.getCreditCard().setExpirationMonth(providerForm.getExpirationMonth());
		result.getCreditCard().setExpirationYear(providerForm.getExpirationYear());
		result.getCreditCard().setHolderName(providerForm.getHolderName());
		result.getCreditCard().setNumber(providerForm.getNumber());

		if (!StringUtils.isEmpty(providerForm.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(providerForm.getPhone());
			if (matcher.matches())
				providerForm.setPhone(this.customisationRepository.findAll().iterator().next().getCountryCode() + providerForm.getPhone());
		}
		result.setPhone(providerForm.getPhone());

		if (!providerForm.getPassword().equals(providerForm.getPasswordChecker()))
			binding.rejectValue("passwordChecker", "member.validation.passwordsNotMatch", "Passwords doesnt match");
		if (!this.useraccountRepository.findUserAccountsByUsername(providerForm.getUsername()).isEmpty())
			binding.rejectValue("username", "member.validation.usernameExists", "This username already exists");
		if (providerForm.getCheckBox() == false)
			binding.rejectValue("checkBox", "member.validation.checkBox", "This checkbox must be checked");

		this.validator.validate(result, binding);
		this.providerRepository.flush();

		return result;
	}

	public Provider reconstructPruned(final Provider provider, final BindingResult binding) {
		Provider result;

		if (provider.getId() == 0)
			result = provider;
		else
			result = this.providerRepository.findOne(provider.getId());
		result.setAddress(provider.getAddress());
		result.setEmail(provider.getEmail());
		result.setName(provider.getName());
		result.setPhoto(provider.getPhoto());
		result.setSurname(provider.getSurname());
		result.setMake(provider.getMake());
		result.setVatNumber(provider.getVatNumber());

		if (!StringUtils.isEmpty(provider.getPhone())) {
			final Pattern pattern = Pattern.compile("^\\d{4,}$", Pattern.CASE_INSENSITIVE);
			final Matcher matcher = pattern.matcher(provider.getPhone());
			if (matcher.matches())
				provider.setPhone(this.customisationRepository.findAll().iterator().next().getCountryCode() + provider.getPhone());
		}
		result.setPhone(provider.getPhone());

		this.validator.validate(result, binding);
		this.providerRepository.flush();
		return result;
	}

	public void flush() {
		this.providerRepository.flush();
	}

	public List<Provider> topFiveProvidersInItems() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		List<Provider> providers;
		List<Provider> result;

		providers = this.providerRepository.topFiveProvidersInItems();

		if (providers.size() > 5)
			result = providers.subList(0, 4);
		else
			result = providers;

		return result;
	}
}
