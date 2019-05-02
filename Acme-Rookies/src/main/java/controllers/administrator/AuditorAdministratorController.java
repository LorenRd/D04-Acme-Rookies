package controllers.administrator;

import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import services.CustomisationService;

import controllers.AbstractController;
import domain.Auditor;
import forms.AuditorForm;

@Controller
@RequestMapping("/auditor/administrator")
public class AuditorAdministratorController extends AbstractController {

	@Autowired
	private AuditorService auditorService;

	@Autowired
	private CustomisationService customisationService;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView res;
		Auditor auditor;
		AuditorForm auditorForm;
		auditor = this.auditorService.create();
		auditorForm = this.auditorService.construct(auditor);
		res = this.createRegisterModelAndView(auditorForm);
		res.addObject("formURI", "auditor/administrator/register.do");
		return res;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView register(@ModelAttribute("auditorForm") @Valid final AuditorForm auditorForm,final BindingResult binding) {
		ModelAndView res;
		Auditor auditor;

		try {
			auditor = this.auditorService.reconstruct(auditorForm,
					binding);
			if (binding.hasErrors()) {
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
				res = this.createRegisterModelAndView(auditorForm);
			} else {
				auditor = this.auditorService.save(auditor);
				res = new ModelAndView("welcome/index");
			}
		} catch (final Throwable oops) {
			res = this.createRegisterModelAndView(auditorForm,
					"auditor.commit.error");
		}
		return res;
	}

	// Ancillary
	// Methods------------------------------------------------------------------

	private ModelAndView createRegisterModelAndView(
			final AuditorForm auditorForm) {
		ModelAndView result;
		result = this.createRegisterModelAndView(auditorForm, null);
		return result;
	}

	private ModelAndView createRegisterModelAndView(
			final AuditorForm auditorForm, final String messageCode) {
		ModelAndView res;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		res = new ModelAndView("auditor/administrator/register");
		res.addObject("auditorForm", auditorForm);
		res.addObject("countryCode", countryCode);
		res.addObject("message", messageCode);

		return res;
	}

}
