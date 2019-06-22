package controllers.auditor;

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
import services.AuditService;
import services.AuditorService;
import services.PositionService;
import services.claseSinNombreService;
import controllers.AbstractController;
import domain.Actor;
import domain.Audit;
import domain.Auditor;
import domain.Position;
import domain.claseSinNombre;

@Controller
@RequestMapping("/audit/auditor")
public class AuditAuditorController extends AbstractController {
	
	// Services
	
	@Autowired
	private ActorService	actorService;

	@Autowired
	private AuditService		auditService;

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private PositionService	positionService;
	
	@Autowired
	private claseSinNombreService	claseSinNombreService;

	
	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<Audit> audits;
		Auditor principal;

		principal = this.auditorService.findByPrincipal();

		audits = this.auditService.findAllByAuditor(principal.getId());

		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);
		result.addObject("requestURI", "audit/auditor/list.do");

		return result;

	}
	
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
	
	//Create
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.create();
		result = this.createModelAndView(audit);

		return result;
	}
	
	
	// Delete
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int auditId) {
		Audit audit;
		ModelAndView result;
		
		audit = this.auditService.findOne(auditId);
		try {
			this.auditService.delete(audit);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(audit, "audit.commit.error");
		}
		return result;
	}
	
	//Edit
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int auditId) {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.findOne(auditId);
		Assert.notNull(audit);
		result = this.createEditModelAndView(audit);

		return result;
	}
	//Save draft --- CREATE
	
		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveDraft")
		public ModelAndView createDraft(@ModelAttribute("audit") Audit audit, final BindingResult binding) {
			ModelAndView result;

			try {
				audit = this.auditService.reconstruct(audit, binding);
				if (binding.hasErrors()) {
					result = this.createModelAndView(audit);
					for (final ObjectError e : binding.getAllErrors())
						System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				} else {
					audit = this.auditService.save(audit, true);
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				result = this.createModelAndView(audit, "audit.commit.error");
			}
			return result;
		}
		
		//Save Final --- CREATE
		
		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveFinal")
		public ModelAndView createFinal(@ModelAttribute("audit") Audit audit, final BindingResult binding) {
			ModelAndView result;

			try {
				audit = this.auditService.reconstruct(audit, binding);
				if (binding.hasErrors()) {
					result = this.createModelAndView(audit);
					for (final ObjectError e : binding.getAllErrors())
						System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
				} else {
					audit = this.auditService.save(audit, false);
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				result = this.createModelAndView(audit, "audit.commit.error");
			}
			return result;
		}
		// Save Draft --- Edit
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
		public ModelAndView saveDraft(@ModelAttribute("audit") Audit audit, final BindingResult binding) {
			ModelAndView result;

			try {
				audit = this.auditService.reconstruct(audit, binding);
				if (binding.hasErrors()) {
					System.out.println(binding.getAllErrors());
					result = this.editModelAndView(audit);
				} else {
					audit = this.auditService.save(audit, true);
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(audit, "audit.commit.error");
			}
			return result;
		}
		
		// Save Draft --- Edit
		
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
		public ModelAndView saveFinal(@ModelAttribute("audit") Audit audit, final BindingResult binding) {
			ModelAndView result;

			try {
				audit = this.auditService.reconstruct(audit, binding);
				if (binding.hasErrors()) {
					System.out.println(binding.getAllErrors());
					result = this.editModelAndView(audit);
				} else {
					audit = this.auditService.save(audit, false);
					result = new ModelAndView("redirect:/welcome/index.do");
				}

			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = this.editModelAndView(audit, "audit.commit.error");
			}
			return result;
		}
		
		
		// ------------------- Ancillary Methods
		protected ModelAndView createEditModelAndView(final Audit audit) {
			ModelAndView result;
		
			result = this.createEditModelAndView(audit, null);
		
			return result;
		}
		
		protected ModelAndView createEditModelAndView(final Audit audit, final String messageCode) {
			ModelAndView result;
		
			result = new ModelAndView("audit/edit");
			result.addObject("audit", audit);
		
			result.addObject("message", messageCode);
		
			return result;
		}
		
		private ModelAndView createModelAndView(final Audit audit) {
			ModelAndView result;
		
			result = this.createModelAndView(audit, null);
			return result;
		}
		
		private ModelAndView createModelAndView(final Audit audit, final String messageCode) {
			ModelAndView result;
			Collection<Position> positions;
			
			positions = this.positionService.findAllFinalNotAudit();
			
			result = new ModelAndView("audit/create");
			result.addObject("audit", audit);
			result.addObject("positions",positions);
			result.addObject("message", messageCode);
			return result;
		}
		
		
		private ModelAndView editModelAndView(final Audit audit) {
			ModelAndView result;
		
			result = this.editModelAndView(audit, null);
			return result;
		}
		
		private ModelAndView editModelAndView(final Audit audit, final String messageCode) {
			ModelAndView result;
		
			result = new ModelAndView("audit/edit");
			result.addObject("audit", audit);
			result.addObject("message", messageCode);
			return result;
		}
		
		

}
