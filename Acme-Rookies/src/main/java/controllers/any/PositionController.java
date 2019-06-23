
package controllers.any;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import services.PositionService;
import services.claseSinNombreService;
import controllers.AbstractController;
import domain.Audit;
import domain.Position;
import domain.claseSinNombre;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

	// Services

	@Autowired
	private PositionService			positionService;

	@Autowired
	private AuditService			auditService;

	@Autowired
	private claseSinNombreService	claseSinNombreService;


	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String keyword, @RequestParam(required = false, defaultValue = "false") final Boolean keywordBool) {
		ModelAndView result;
		Collection<Position> positions;

		if (keywordBool && keyword != null)
			positions = this.positionService.findByKeywordFinal(keyword);
		else
			positions = this.positionService.findAllFinal();

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/list.do");

		return result;
	}

	// ListCompanyId

	@RequestMapping(value = "/listCompanyId", method = RequestMethod.GET)
	public ModelAndView listCompany(@RequestParam final int companyId, @RequestParam(required = false) final String keyword, @RequestParam(required = false, defaultValue = "false") final Boolean keywordBool) {
		ModelAndView result;
		Collection<Position> positions;

		if (keywordBool && keyword != null)
			positions = this.positionService.findByKeywordFinalCompany(keyword, companyId);
		else
			positions = this.positionService.findAllFinalCompany(companyId);

		result = new ModelAndView("position/list");
		result.addObject("positions", positions);
		result.addObject("requestURI", "position/list.do");

		return result;
	}

	// Display

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int positionId) {
		// Inicializa resultado
		ModelAndView result;
		Position position;
		Collection<Audit> audits;
		Collection<claseSinNombre> claseSinNombres;

		// Busca en el repositorio
		position = this.positionService.findOne(positionId);
		Assert.notNull(position);
		audits = this.auditService.findAllByPosition(positionId);
		claseSinNombres = this.claseSinNombreService.findByPosition(positionId);

		// Crea y añade objetos a la vista
		result = new ModelAndView("position/display");
		result.addObject("requestURI", "position/display.do");
		result.addObject("position", position);
		result.addObject("audits", audits);
		result.addObject("claseSinNombres", claseSinNombres);

		//Dates
		final Calendar cal = Calendar.getInstance();
		//1 month old 
		cal.add(Calendar.MONTH, -1);
		final Date dateOneMonth = cal.getTime();
		//2 months old 
		cal.add(Calendar.MONTH, -1);
		final Date dateTwoMonths = cal.getTime();
		result.addObject("dateOneMonth", dateOneMonth);
		result.addObject("dateTwoMonths", dateTwoMonths);

		// Envía la vista
		return result;
	}
}
