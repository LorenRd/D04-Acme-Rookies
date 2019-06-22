
package controllers.any;

import java.util.ArrayList;
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

import services.ActorService;
import services.AuditService;
import services.claseSinNombreService;
import controllers.AbstractController;
import domain.Actor;
import domain.Audit;
import domain.claseSinNombre;

@Controller
@RequestMapping("/audit")
public class AuditController extends AbstractController {

	// Services

	@Autowired
	private AuditService	auditService;
	
	@Autowired
	private ActorService	actorService;

	@Autowired
	private claseSinNombreService	claseSinNombreService;

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
		
		//Dates
		Calendar cal = Calendar.getInstance();
		//1 month old
		cal.add(Calendar.MONTH, -1);
		Date dateOneMonth = cal.getTime();
		//2 months old
		cal.add(Calendar.MONTH, -1);
		Date dateTwoMonths = cal.getTime();
		result.addObject("dateOneMonth", dateOneMonth);
		result.addObject("dateTwoMonths", dateTwoMonths);

		
		
		//Actor para en caso de ser company poder crear claseSinNombre
		try{
			Actor principal;
			principal = this.actorService.findByPrincipal();		
			result.addObject("principal", principal);
			String rol;
			Collection<claseSinNombre> claseSinNombres = new ArrayList<claseSinNombre>();
			rol = principal.getUserAccount().getAuthorities().iterator().next().toString();

			if(rol.equals("COMPANY"))
				claseSinNombres = this.claseSinNombreService.findByAudit(auditId);
			else if(rol.equals("AUDITOR"))
				claseSinNombres = this.claseSinNombreService.findByAuditFinal(auditId);
			result.addObject("claseSinNombre", claseSinNombres);

		}catch (Exception e) {
		}

		// Envía la vista
		return result;
	}
}
