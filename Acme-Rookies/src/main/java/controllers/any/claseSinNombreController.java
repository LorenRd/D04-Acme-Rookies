
package controllers.any;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.claseSinNombreService;
import controllers.AbstractController;
import domain.Actor;
import domain.claseSinNombre;

@Controller
@RequestMapping("/claseSinNombre")
public class claseSinNombreController extends AbstractController {

	// Services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private claseSinNombreService	claseSinNombreService;

	//List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<claseSinNombre> claseSinNombres;
		claseSinNombres = new ArrayList<claseSinNombre>();

		final Actor principal = this.actorService.findByPrincipal();

		claseSinNombres = this.claseSinNombreService.findAllByCompany(principal.getId());

		result = new ModelAndView("claseSinNombre/list");
		result.addObject("claseSinNombre", claseSinNombres);
		result.addObject("requestURI", "claseSinNombre/company/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int claseSinNombreId) {
		// Inicializa resultado
		ModelAndView result;
		claseSinNombre claseSinNombre;

		// Busca en el repositorio
		claseSinNombre = this.claseSinNombreService.findOne(claseSinNombreId);
		Assert.notNull(claseSinNombre);

		// Crea y a�ade objetos a la vista
		result = new ModelAndView("claseSinNombre/display");
		result.addObject("requestURI", "claseSinNombre/display.do");
		result.addObject("claseSinNombre", claseSinNombre);

		// Env�a la vista
		return result;
	}
}
