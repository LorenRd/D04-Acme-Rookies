
package controllers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.claseSinNombreService;
import controllers.AbstractController;
import domain.claseSinNombre;

@Controller
@RequestMapping("/claseSinNombre")
public class claseSinNombreController extends AbstractController {

	// Services

	@Autowired
	private claseSinNombreService	claseSinNombreService;


	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int claseSinNombreId) {
		// Inicializa resultado
		ModelAndView result;
		claseSinNombre cSN;

		// Busca en el repositorio
		cSN = this.claseSinNombreService.findOne(claseSinNombreId);
		Assert.notNull(cSN);

		// Crea y añade objetos a la vista
		result = new ModelAndView("claseSinNombre/display");
		result.addObject("requestURI", "claseSinNombre/display.do");
		result.addObject("claseSinNombre", cSN);

		// Envía la vista
		return result;
	}
}
