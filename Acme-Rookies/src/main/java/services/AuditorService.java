package services;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.AuditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Audit;
import domain.Auditor;
import domain.CreditCard;
import domain.Message;

@Service
@Transactional
public class AuditorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private AuditorRepository auditorRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private MessageService messageService;
	 
	@Autowired
	private AuditService auditService;
	
	 // Simple CRUD Methods

	 public boolean exists(final Integer arg0) {
		 return this.auditorRepository.exists(arg0);
	 }

	public Auditor create() {
		Auditor result;
		UserAccount userAccount;
		Authority authority;
		CreditCard creditCard;

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

		 principal = this.findByPrincipal();
		 Assert.notNull(principal);		 		
		 
		 audits = this.auditService.findAllByAuditor(principal.getId());
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


}
