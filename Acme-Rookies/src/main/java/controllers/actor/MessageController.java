package controllers.actor;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomisationService;
import services.MessageService;

import controllers.AbstractController;
import domain.Actor;
import domain.Customisation;
import domain.Message;

@Controller
@RequestMapping("/message/actor")
public class MessageController extends AbstractController {

	// Services

	@Autowired
	private ActorService actorService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private CustomisationService	customisationService;

	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Message> messages;
		Customisation customisation;
		customisation = this.customisationService.find();
		try {
			final int actorId = this.actorService.findByPrincipal().getId();
			Assert.notNull(actorId);

			messages = this.messageService.findByActorId(actorId);

			result = new ModelAndView("message/list");
			result.addObject("messages", messages);
			result.addObject("requestURI", "message/actor/list.do");

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = new ModelAndView("message/list");
			result.addObject("message", "message.retrieve.error");
			result.addObject("messages", new ArrayList<Message>());
			result.addObject("rebrandMessage", customisation.getRebrandingAnnouncement());
		}

		return result;
	}

	@RequestMapping(value = "/exportData")
	public ModelAndView exportData() {
		ModelAndView result;
		Message mensaje;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		try {
			mensaje = this.messageService.create();
			mensaje.getRecipients().add(principal);
			mensaje.setBody("Name: " + principal.getName() + " || Surname: "
					+ principal.getSurname() + " || Photo: "
					+ principal.getPhoto() + " || Phone: "
					+ principal.getPhone() + " || Address: "
					+ principal.getAddress() + " || VAT number: "
					+ principal.getVatNumber() + " || Username: "
					+ principal.getUserAccount().getUsername());
			mensaje.setSubject("Your data");

			this.messageService.save(mensaje);

			result = new ModelAndView("redirect:/message/actor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/message/actor/list.do");
		}

		return result;
	}
	
	@RequestMapping(value = "/warning")
	public ModelAndView warning() {
		ModelAndView result;
		Message mensaje;

		try {
			mensaje = this.messageService.create();
			mensaje.setBody("Passwords have been leaked!" + "¡Las contraseñas han sido filtradas!");
			mensaje.setSubject("WARNING!" + "¡ALERTA!");

			this.messageService.broadcast(mensaje);

			result = new ModelAndView("redirect:/message/actor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/message/actor/list.do");
		}

		return result;
	}
	
	@RequestMapping(value = "/rebrand")
	public ModelAndView rebrand() {
		ModelAndView result;
		Message mensaje;
		Customisation customisation;
		customisation = this.customisationService.find();
		

		try {
			Assert.isTrue(!customisation.getRebrandingAnnouncement());
			mensaje = this.messageService.create();
			mensaje.setBody("Now Acme Hacker Rank is named Acme Rookies!" + "¡Cambiamos de nombre, ahora Acme Hacker Rank se llama Acme Rookies!");
			mensaje.setSubject("Rebrand!" + "¡Cambiamos de nombre!");

			this.messageService.broadcast(mensaje);
			customisation.setRebrandingAnnouncement(true);
			this.customisationService.save(customisation);
			result = new ModelAndView("redirect:/message/actor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/message/actor/list.do");
		}

		return result;
	}
}
