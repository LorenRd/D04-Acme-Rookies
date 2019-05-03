
package controllers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import controllers.AbstractController;
import domain.Audit;

@Controller
@RequestMapping("/audit")
public class AuditController extends AbstractController {

	// Services

	@Autowired
	private AuditService	auditService;

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int auditId) {
		// Inicializa resultado
		ModelAndView result;
		Audit audit;

		// Busca en el repositorio
		audit = this.auditService.findOne(auditId);
		Assert.notNull(audit);

		// Crea y añade objetos a la vista
		result = new ModelAndView("audit/display");
		result.addObject("requestURI", "audit/display.do");
		result.addObject("audit", audit);

		// Envía la vista
		return result;
	}
}
