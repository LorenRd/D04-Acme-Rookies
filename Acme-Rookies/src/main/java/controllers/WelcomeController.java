/*
 * WelcomeController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomisationService;
import domain.Customisation;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Services

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private ActorService	actorService;



	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") String name) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		Customisation customisation;
		customisation = this.customisationService.find();

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());
		
		try{
			name= this.actorService.findByPrincipal().getName();
		}catch (Exception e) {
		}
		result = new ModelAndView("welcome/index");
		result.addObject("welcomeMessageEs", customisation.getWelcomeMessageEs());
		result.addObject("welcomeMessageEn", customisation.getWelcomeMessageEn());
		result.addObject("name", name);
		result.addObject("moment", moment);

		return result;
	}
}
