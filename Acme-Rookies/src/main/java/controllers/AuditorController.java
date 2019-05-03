/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import domain.Auditor;
import forms.AuditorForm;

import services.AuditorService;
import services.CustomisationService;

@Controller
@RequestMapping("/auditor")
public class AuditorController extends AbstractController {

	@Autowired
	private AuditorService auditorService;

	@Autowired
	private CustomisationService customisationService;

	@RequestMapping("/viewProfile")
	public ModelAndView view() {
		ModelAndView result;

		result = new ModelAndView("auditor/viewProfile");
		result.addObject("actor", this.auditorService.findByPrincipal());

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Auditor auditor;
		auditor = this.auditorService.findByPrincipal();
		Assert.notNull(auditor);
		result = this.editModelAndView(auditor);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(
			@ModelAttribute("auditor") Auditor auditor,
			final BindingResult binding) {
		ModelAndView result;

		try {
			auditor = this.auditorService.reconstructPruned(
					auditor, binding);
			if (binding.hasErrors()) {
				result = this.editModelAndView(auditor);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error ["
							+ e.getDefaultMessage() + "] "
							+ Arrays.toString(e.getCodes()));
			} else {
				auditor = this.auditorService.save(auditor);
				result = new ModelAndView("redirect:/welcome/index.do");
			}
		} catch (final Throwable oops) {
			result = this.editModelAndView(auditor,
					"auditor.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete() {
		ModelAndView result;

		try {
			this.auditorService.delete();

			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/auditor/viewProfile.do");
		}

		return result;
	}

	protected ModelAndView createEditModelAndView(
			final Auditor auditor) {
		ModelAndView result;
		AuditorForm auditorForm;
		auditorForm = this.auditorService.construct(auditor);
		result = this.createEditModelAndView(auditorForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(
			final AuditorForm auditorForm, final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("auditor/edit");
		result.addObject("auditorForm", auditorForm);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView editModelAndView(final Auditor auditor) {
		ModelAndView result;
		result = this.editModelAndView(auditor, null);
		return result;
	}

	private ModelAndView editModelAndView(final Auditor auditor,
			final String messageCode) {
		ModelAndView result;
		String countryCode;

		countryCode = this.customisationService.find().getCountryCode();

		result = new ModelAndView("auditor/edit");
		result.addObject("auditor", auditor);
		result.addObject("countryCode", countryCode);
		result.addObject("message", messageCode);

		return result;
	}

}
