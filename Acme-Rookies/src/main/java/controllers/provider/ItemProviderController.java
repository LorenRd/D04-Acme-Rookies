
package controllers.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

import services.ActorService;
import services.ItemService;
import controllers.AbstractController;
import domain.Actor;
import domain.Item;

@Controller
@RequestMapping("/item/provider")
public class ItemProviderController extends AbstractController {

	@Autowired
	private ItemService		itemService;

	@Autowired
	private ActorService	actorService;


	//List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Item> items;
		items = new ArrayList<Item>();

		final Actor principal = this.actorService.findByPrincipal();

		items = this.itemService.findByProvider(principal.getId());

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("requestURI", "item/provider/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int itemId) {
		// Inicializa resultado
		ModelAndView result;
		Item item;

		// Busca en el repositorio
		item = this.itemService.findOne(itemId);
		Assert.notNull(item);

		// Crea y añade objetos a la vista
		result = new ModelAndView("item/display");
		result.addObject("requestURI", "item/display.do");
		result.addObject("item", item);

		// Envía la vista
		return result;
	}

	//Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Item item;

		item = this.itemService.create();
		result = this.createModelAndView(item);

		return result;
	}

	//Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int itemId) {
		ModelAndView result;
		Item item;

		item = this.itemService.findOne(itemId);
		Assert.notNull(item);
		result = this.createEditModelAndView(item);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("item") Item item, final BindingResult binding) {
		ModelAndView result;

		try {
			item = this.itemService.reconstruct(item, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(item);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				item = this.itemService.save(item);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(item, "item.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView create(@ModelAttribute("item") Item item, final BindingResult binding) {
		ModelAndView result;

		try {
			item = this.itemService.reconstruct(item, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(item);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				item = this.itemService.save(item);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(item, "item.commit.error");
		}
		return result;
	}

	//Delete

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int itemId) {
		ModelAndView result;
		Item item;

		item = this.itemService.findOne(itemId);

		try {
			this.itemService.delete(item);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(item, "item.commit.error");
		}
		return result;
	}

	// -------------------

	protected ModelAndView createEditModelAndView(final Item item) {
		ModelAndView result;

		result = this.createEditModelAndView(item, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Item item, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("item/edit");
		result.addObject("item", item);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView createModelAndView(final Item item) {
		ModelAndView result;

		result = this.createModelAndView(item, null);
		return result;
	}

	private ModelAndView createModelAndView(final Item item, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("item/create");
		result.addObject("item", item);
		result.addObject("message", messageCode);
		return result;
	}

}
