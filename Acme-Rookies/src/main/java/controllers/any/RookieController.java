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
import services.RookieService;
import controllers.AbstractController;
import domain.Rookie;
import forms.RookieForm;

@Controller
@RequestMapping("/rookie")
public class RookieController extends AbstractController {

	// Services

	@Autowired
	private RookieService rookieService;

	@Autowired
	private CustomisationService customisationService;

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Rookie> rookies;

		rookies = this.rookieService.findAll();

		result = new ModelAndView("rookie/list");
		result.addObject("rookies", rookies);
		result.addObject("requestURI", "rookie/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView show(
			@RequestParam(required = false) final Integer rookieId) {
		final ModelAndView result;
		Rookie rookie = new Rookie();

		if (rookieId == null)
			rookie = this.rookieService.findByPrincipal();
		else
			rookie = this.rookieService.findOne(rookieId);

		result = new ModelAndView("rookie/display");
		result.addObject("rookie", rookie);

		return result;

	}

	// Create
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Rookie rookie;
		RookieForm rookieForm;

		rookie = this.rookieService.create();
		rookieForm = this.rookieService.construct(rookie);
		result = this.createEditModelAndView(rookieForm);

		return result;
	}

	// Save de Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("rookie") Rookie rookie,
			final BindingResult binding) {
		ModelAndView result;

		try {
			rookie = this.rookieService.reconstructPruned(rookie, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(rookie);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else
				rookie = this.rookieService.save(rookie);
			result = new ModelAndView("welcome/index");
		} catch (final Throwable oops) {
			result = this.editModelAndView(rookie, "rookie.commit.error");

		}

		return result;
	}

	// Save de Register
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(
			@ModelAttribute("rookieForm") @Valid final RookieForm rookieForm,
			final BindingResult binding) {
		ModelAndView result;
		Rookie rookie;

		try {
			rookie = this.rookieService.reconstruct(rookieForm, binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
				result = this.createEditModelAndView(rookieForm);
			} else {
				rookie = this.rookieService.save(rookie);
				result = new ModelAndView("welcome/index");
			}
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(rookieForm,
					"rookie.commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Rookie rookie;

		rookie = this.rookieService.findByPrincipal();
		Assert.notNull(rookie);
		result = this.editModelAndView(rookie);

		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.rookieService.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/company/display.do");
		}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	private ModelAndView editModelAndView(final Rookie rookie) {
		ModelAndView result;
		result = this.editModelAndView(rookie, null);
		return result;
	}

	private ModelAndView editModelAndView(final Rookie rookie,
			final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("rookie/edit");
		result.addObject("rookie", rookie);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RookieForm rookieForm) {
		ModelAndView result;
		result = this.createEditModelAndView(rookieForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RookieForm rookieForm,
			final String message) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();
		if (rookieForm.getIdRookie() != 0)
			result = new ModelAndView("rookie/edit");
		else
			result = new ModelAndView("rookie/register");

		result.addObject("rookieForm", rookieForm);
		result.addObject("actionURI", "rookie/edit.do");
		result.addObject("redirectURI", "welcome/index.do");
		result.addObject("countryCode", countryCode);

		result.addObject("message", message);

		return result;
	}
}
