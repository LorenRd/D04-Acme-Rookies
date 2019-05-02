
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ItemRepository;
import security.Authority;
import domain.Actor;
import domain.Item;
import domain.Provider;

@Service
@Transactional
public class ItemService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ItemRepository	itemRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ProviderService	providerService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	// Simple CRUD Methods

	public boolean exist(final Integer itemId) {
		return this.itemRepository.exists(itemId);
	}

	public Item findOne(final int itemId) {
		Item result;

		result = this.itemRepository.findOne(itemId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Item> findAll() {
		Collection<Item> result;

		result = this.itemRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Item create() {
		Item result;
		final Provider principal;

		principal = this.providerService.findByPrincipal();
		Assert.notNull(principal);

		result = new Item();
		result.setProvider(principal);
		return result;
	}

	public Item save(final Item item) {
		Provider principal;
		Item result;

		principal = this.providerService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(item);
		Assert.isTrue(item.getProvider() == principal);

		result = this.itemRepository.save(item);
		Assert.notNull(result);

		return result;
	}

	public void delete(final Item item) {
		Provider principal;

		Assert.notNull(item);

		principal = this.providerService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(item.getProvider().getId() == principal.getId());

		this.itemRepository.delete(item);
	}

	// Business Methods

	public Collection<Item> findByProvider(final int providerId) {
		Collection<Item> result;

		result = this.itemRepository.findByProviderId(providerId);
		return result;
	}

	public Item reconstruct(final Item item, final BindingResult binding) {
		Item result;
		if (item.getId() == 0) {
			result = item;
			result.setProvider(this.providerService.findByPrincipal());
		} else {
			result = this.itemRepository.findOne(item.getId());

			result.setName(item.getName());
			result.setDescription(item.getDescription());
			result.setInformationLink(item.getInformationLink());
			result.setPictures(item.getPictures());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.itemRepository.flush();
	}

	public Double avgItemsPerProvider() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.itemRepository.avgItemsPerProvider();

		return result;
	}

	public Double minItemsPerProvider() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.itemRepository.minItemsPerProvider();

		return result;
	}

	public Double maxItemsPerProvider() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.itemRepository.maxItemsPerProvider();

		return result;
	}

	public Double stddevItemsPerProvider() {
		final Authority authority = new Authority();
		authority.setAuthority(Authority.ADMIN);
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor);
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(authority));
		Double result;

		result = this.itemRepository.stddevItemsPerProvider();

		return result;
	}

	public void deleteInBatch(final Collection<Item> items) {
		this.itemRepository.deleteInBatch(items);
	}
}
