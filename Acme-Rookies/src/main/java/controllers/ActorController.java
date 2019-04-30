package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Actor;
import domain.Administrator;
import domain.Rookie;
import domain.Company;

import services.ActorService;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService actorService;

	// Display ----------------------------------------------------------------
	@RequestMapping(value = "/display")
	public ModelAndView display() {
		final ModelAndView result;
		Actor principal;

		result = new ModelAndView("actor/display");

		principal = null;
		try {
			principal = this.actorService.findByPrincipal();
		} catch (final RuntimeException oops) {
		}
		if (principal == null)
			result.addObject("actor", null);
		else {

			result.addObject("actor", principal);

			if (principal instanceof Rookie) {
				Rookie rookie;
				rookie = (Rookie) principal;
				result.addObject("rookie", rookie);
			} else if (principal instanceof Company) {
				Company company;
				company = (Company) principal;
				result.addObject("company", company);
			} else {
				Administrator administrator;
				administrator = (Administrator) principal;
				result.addObject("administrator", administrator);
			}
		}
		return result;
	}

	@RequestMapping(value = "/display", params = "actorId")
	public ModelAndView display(@RequestParam final int actorId) {
		final ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(actorId);

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);

		if (actor != null)
			if (actor instanceof Rookie) {
				Rookie rookie;
				rookie = (Rookie) actor;
				result.addObject("rookie", rookie);
			} else if (actor instanceof Company) {
				Company company;
				company = (Company) actor;
				result.addObject("company", company);
			} else {
				Administrator administrator;
				administrator = (Administrator) actor;
				result.addObject("administrator", administrator);
			}
		return result;

	}

}
