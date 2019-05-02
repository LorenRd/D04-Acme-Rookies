
package controllers.any;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import controllers.AbstractController;
import domain.Item;

@Controller
@RequestMapping("/item")
public class ItemController extends AbstractController {

	// Services

	@Autowired
	private ItemService	itemService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Item> items;

		items = this.itemService.findAll();

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("requestURI", "item/list.do");

		return result;
	}

	// ListProviderId

	@RequestMapping(value = "/listProviderId", method = RequestMethod.GET)
	public ModelAndView listProvider(@RequestParam final int providerId) {
		ModelAndView result;
		Collection<Item> items;

		items = this.itemService.findByProvider(providerId);

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("requestURI", "item/list.do");

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
}
