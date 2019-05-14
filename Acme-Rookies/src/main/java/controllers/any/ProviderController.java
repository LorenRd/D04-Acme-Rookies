
package controllers.any;

import java.util.Arrays;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import services.ItemService;
import services.ProviderService;
import controllers.AbstractController;
import domain.Item;
import domain.Provider;
import forms.ProviderForm;

@Controller
@RequestMapping("/provider")
public class ProviderController extends AbstractController {

	// Services

	@Autowired
	private ProviderService			providerService;

	@Autowired
	private ItemService				itemService;

	@Autowired
	private CustomisationService	customisationService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Provider> providers;

		providers = this.providerService.findAll();

		result = new ModelAndView("provider/list");
		result.addObject("providers", providers);
		result.addObject("requestURI", "provider/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(required = false) final Integer providerId) {
		final ModelAndView result;
		Provider provider = new Provider();
		Collection<Item> items;

		if (providerId == null)
			provider = this.providerService.findByPrincipal();
		else
			provider = this.providerService.findOne(providerId);

		items = this.itemService.findByProvider(provider.getId());

		result = new ModelAndView("provider/display");
		result.addObject("provider", provider);
		result.addObject("items", items);

		return result;

	}

	// Create
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Provider provider;
		ProviderForm providerForm;

		provider = this.providerService.create();
		providerForm = this.providerService.construct(provider);
		result = this.createEditModelAndView(providerForm);

		return result;
	}

	// Save de Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("provider") Provider provider, final BindingResult binding) {
		ModelAndView result;

		try {
			provider = this.providerService.reconstructPruned(provider, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(provider);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else
				provider = this.providerService.save(provider);
			result = new ModelAndView("redirect:/provider/display.do");
		} catch (final Throwable oops) {
			result = this.editModelAndView(provider, "provider.commit.error");

		}

		return result;
	}

	// Save de Register
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(@ModelAttribute("providerForm") @Valid final ProviderForm providerForm, final BindingResult binding) {
		ModelAndView result;
		Provider provider;

		try {
			provider = this.providerService.reconstruct(providerForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(providerForm);
			} else {
				provider = this.providerService.save(provider);
				result = new ModelAndView("redirect:/provider/display.do?providerId=" + provider.getId());
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(providerForm, "provider.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Provider provider;

		provider = this.providerService.findByPrincipal();
		Assert.notNull(provider);
		result = this.editModelAndView(provider);

		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.providerService.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/provider/display.do");
		}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	private ModelAndView editModelAndView(final Provider provider) {
		ModelAndView result;
		result = this.editModelAndView(provider, null);
		return result;
	}

	private ModelAndView editModelAndView(final Provider provider, final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("provider/edit");
		result.addObject("provider", provider);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ProviderForm providerForm) {
		ModelAndView result;
		result = this.createEditModelAndView(providerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ProviderForm providerForm, final String message) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();
		if (providerForm.getIdProvider() != 0)
			result = new ModelAndView("provider/edit");
		else
			result = new ModelAndView("provider/register");

		result.addObject("providerForm", providerForm);
		result.addObject("actionURI", "provider/edit.do");
		result.addObject("redirectURI", "welcome/index.do");
		result.addObject("countryCode", countryCode);

		result.addObject("message", message);

		return result;
	}
}
