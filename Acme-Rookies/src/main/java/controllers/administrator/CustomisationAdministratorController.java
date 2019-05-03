
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import controllers.AbstractController;
import domain.Customisation;

@Controller
@RequestMapping("/customisation/administrator")
public class CustomisationAdministratorController extends AbstractController {

	// Services

	@Autowired
	private CustomisationService	customisationService;


	// Displaying

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		final ModelAndView result;
		final Customisation customisation;

		customisation = this.customisationService.find();

		result = new ModelAndView("customisation/display");
		result.addObject("customisation", customisation);
		return result;

	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Customisation customisation;

		customisation = this.customisationService.find();
		Assert.notNull(customisation);
		result = this.createEditModelAndView(customisation);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(@Valid final Customisation customisation, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(customisation);
			System.out.println(binding.getAllErrors());
		} else
			try {
				this.customisationService.save(customisation);
				result = new ModelAndView("redirect:display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(customisation, "customisation.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Customisation customisation) {
		ModelAndView result;

		result = this.createEditModelAndView(customisation, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Customisation customisation, final String message) {
		ModelAndView result;

		result = new ModelAndView("customisation/edit");
		result.addObject("customisation", customisation);
		result.addObject("message", message);

		return result;
	}

}
