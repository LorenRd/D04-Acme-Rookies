
package controllers.rookie;

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

import services.PositionService;
import services.RookieService;
import services.claseSinNombreService;
import controllers.AbstractController;
import domain.Position;
import domain.Rookie;
import domain.claseSinNombre;

@Controller
@RequestMapping("/claseSinNombre/rookie")
public class claseSinNombreRookieController extends AbstractController {

	// Services

	@Autowired
	private claseSinNombreService	claseSinNombreService;

	@Autowired
	private RookieService			rookieService;

	@Autowired
	private PositionService			positionService;


	// Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<claseSinNombre> cSNs;
		Rookie principal;

		principal = this.rookieService.findByPrincipal();

		cSNs = this.claseSinNombreService.findByRookie(principal.getId());

		result = new ModelAndView("claseSinNombre/list");
		result.addObject("claseSinNombres", cSNs);
		result.addObject("requestURI", "claseSinNombre/rookie/list.do");

		return result;

	}

	//Create

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		ModelAndView result;
		claseSinNombre claseSinNombre;
		Position position;

		position = this.positionService.findOne(positionId);

		claseSinNombre = this.claseSinNombreService.create();
		claseSinNombre.setPosition(position);
		result = this.createModelAndView(claseSinNombre);

		return result;
	}

	// Delete

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int claseSinNombreId) {
		claseSinNombre claseSinNombre;
		ModelAndView result;

		claseSinNombre = this.claseSinNombreService.findOne(claseSinNombreId);
		try {
			this.claseSinNombreService.delete(claseSinNombre);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.displayModelAndView(claseSinNombre, "claseSinNombre.commit.error");

		}
		return result;
	}

	//Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int claseSinNombreId) {
		ModelAndView result;
		claseSinNombre claseSinNombre;

		claseSinNombre = this.claseSinNombreService.findOne(claseSinNombreId);
		Assert.notNull(claseSinNombre);
		result = this.createEditModelAndView(claseSinNombre);

		return result;
	}

	//Save Draft --- CREATE

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView createDraft(@ModelAttribute("claseSinNombre") claseSinNombre claseSinNombre, @RequestParam final int positionId, final BindingResult binding) {
		ModelAndView result;

		try {
			claseSinNombre = this.claseSinNombreService.reconstruct(claseSinNombre, positionId, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(claseSinNombre);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				claseSinNombre = this.claseSinNombreService.save(claseSinNombre, true);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(claseSinNombre, "claseSinNombre.commit.error");
		}
		return result;
	}

	//Save Final --- CREATE

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView createFinal(@ModelAttribute("claseSinNombre") claseSinNombre claseSinNombre, @RequestParam final int positionId, final BindingResult binding) {
		ModelAndView result;

		try {
			claseSinNombre = this.claseSinNombreService.reconstruct(claseSinNombre, positionId, binding);
			if (binding.hasErrors()) {
				result = this.createModelAndView(claseSinNombre);
				for (final ObjectError e : binding.getAllErrors())
					System.out.println(e.getObjectName() + " error [" + e.getDefaultMessage() + "] " + Arrays.toString(e.getCodes()));
			} else {
				claseSinNombre = this.claseSinNombreService.save(claseSinNombre, false);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			result = this.createModelAndView(claseSinNombre, "claseSinNombre.commit.error");
		}
		return result;
	}

	// Save Draft --- Edit

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(@ModelAttribute("claseSinNombre") claseSinNombre claseSinNombre, final BindingResult binding) {
		ModelAndView result;

		try {
			claseSinNombre = this.claseSinNombreService.reconstruct(claseSinNombre, 0, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.editModelAndView(claseSinNombre);
			} else {
				claseSinNombre = this.claseSinNombreService.save(claseSinNombre, true);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.editModelAndView(claseSinNombre, "claseSinNombre.commit.error");
		}
		return result;
	}

	// Save Final --- Edit

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(@ModelAttribute("claseSinNombre") claseSinNombre claseSinNombre, final BindingResult binding) {
		ModelAndView result;

		try {
			claseSinNombre = this.claseSinNombreService.reconstruct(claseSinNombre, 0, binding);
			if (binding.hasErrors()) {
				System.out.println(binding.getAllErrors());
				result = this.editModelAndView(claseSinNombre);
			} else {
				claseSinNombre = this.claseSinNombreService.save(claseSinNombre, false);
				result = new ModelAndView("redirect:/welcome/index.do");
			}

		} catch (final Throwable oops) {
			oops.printStackTrace();
			result = this.editModelAndView(claseSinNombre, "claseSinNombre.commit.error");
		}
		return result;
	}

	// ------------------- Ancillary Methods

	protected ModelAndView createEditModelAndView(final claseSinNombre claseSinNombre) {
		ModelAndView result;

		result = this.createEditModelAndView(claseSinNombre, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final claseSinNombre claseSinNombre, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("claseSinNombre/edit");
		result.addObject("claseSinNombre", claseSinNombre);

		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView displayModelAndView(final claseSinNombre claseSinNombre, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("claseSinNombre/display");
		result.addObject("claseSinNombre", claseSinNombre);
		result.addObject("message", messageCode);

		return result;
	}

	private ModelAndView createModelAndView(final claseSinNombre claseSinNombre) {
		ModelAndView result;

		result = this.createModelAndView(claseSinNombre, null);
		return result;
	}

	private ModelAndView createModelAndView(final claseSinNombre claseSinNombre, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("claseSinNombre/create");
		result.addObject("claseSinNombre", claseSinNombre);
		result.addObject("message", messageCode);
		return result;
	}

	private ModelAndView editModelAndView(final claseSinNombre claseSinNombre) {
		ModelAndView result;

		result = this.editModelAndView(claseSinNombre, null);
		return result;
	}

	private ModelAndView editModelAndView(final claseSinNombre claseSinNombre, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("claseSinNombre/edit");
		result.addObject("claseSinNombre", claseSinNombre);
		result.addObject("message", messageCode);
		return result;
	}

}
