
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.claseSinNombreRepository;
import domain.Rookie;
import domain.claseSinNombre;

@Service
@Transactional
public class claseSinNombreService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private claseSinNombreRepository	claseSinNombreRepository;

	@Autowired
	private RookieService				rookieService;

	@Autowired
	private PositionService				positionService;

	@Autowired
	private Validator					validator;


	// Simple CRUD Methods

	public boolean exists(final Integer arg0) {
		return this.claseSinNombreRepository.exists(arg0);
	}

	//	private String generateTicker() {
	//		String result;
	//		String numbers;
	//
	//		final Random random = new Random();
	//
	//		final Date today = new Date();
	//		final Calendar cal = Calendar.getInstance();
	//		cal.setTime(today);
	//
	//		final String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	//		String month = String.valueOf(cal.get(Calendar.MONTH));
	//		String year = String.valueOf(cal.get(Calendar.YEAR));
	//
	//		year = year.substring(2, 4);
	//
	//		if (Integer.parseInt(month) < 10)
	//			month = "0" + month;
	//
	//		numbers = String.format("%04d", random.nextInt(10000));
	//		result = year + month + day + "-" + numbers;
	//		if (this.repeatedTicker(result))
	//			this.generateTicker();
	//
	//		return result;
	//	}

	//	private String generateTicker() {
	//		String result;
	//		final Calendar now = Calendar.getInstance();
	//		String year = String.valueOf(now.get(Calendar.YEAR));
	//		year = year.substring(year.length() - 2, year.length());
	//		final String month = String.valueOf(now.get(Calendar.MONTH) + 1);
	//		String date = String.valueOf(now.get(Calendar.DATE));
	//		date = date.length() == 1 ? "0".concat(date) : date;
	//		final Random r = new Random();
	//		final char a = (char) (r.nextInt(26) + 'a');
	//		final char b = (char) (r.nextInt(26) + 'a');
	//		final char c = (char) (r.nextInt(26) + 'a');
	//		final char d = (char) (r.nextInt(26) + 'a');
	//		String code = String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + String.valueOf(d);
	//		code = code.toUpperCase();
	//		result = year + month + date + "-" + code;
	//
	//		if (this.repeatedTicker(result))
	//			this.generateTicker();
	//
	//		return result;
	//	}

	private String generateTicker() {
		String result;

		final Date today = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(today);

		final String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
		String month = String.valueOf(cal.get(Calendar.MONTH));
		String year = String.valueOf(cal.get(Calendar.YEAR));

		year = year.substring(2, 4);

		if (Integer.parseInt(month) < 10)
			month = "0" + month;

		final Random r = new Random();
		final char a = (char) (r.nextInt(26) + 'a');
		final char b = (char) (r.nextInt(26) + 'a');
		final char c = (char) (r.nextInt(26) + 'a');
		final char d = (char) (r.nextInt(26) + 'a');
		String code = String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + String.valueOf(d);
		code = code.toUpperCase();
		result = year + month + day + "-" + code;

		if (this.repeatedTicker(result))
			this.generateTicker();

		return result;
	}

	public boolean repeatedTicker(final String ticker) {
		Boolean isRepeated = false;
		int repeats;

		repeats = this.claseSinNombreRepository.findRepeatedTickers(ticker);

		if (repeats > 0)
			isRepeated = true;

		return isRepeated;
	}

	public claseSinNombre create() {
		claseSinNombre result;
		final Rookie principal;

		principal = this.rookieService.findByPrincipal();
		Assert.notNull(principal);

		result = new claseSinNombre();
		result.setIsDraft(true);
		result.setTicker(this.generateTicker());

		return result;
	}

	public claseSinNombre save(final claseSinNombre claseSinNombre, final Boolean draft) {
		claseSinNombre result;
		Rookie principal;

		principal = this.rookieService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(claseSinNombre);
		Assert.isTrue(claseSinNombre.getRookie().getId() == principal.getId());
		claseSinNombre.setIsDraft(draft);
		if (!draft)
			claseSinNombre.setPublicationMoment(new Date(System.currentTimeMillis() - 1));

		result = this.claseSinNombreRepository.save(claseSinNombre);
		Assert.notNull(result);
		return result;
	}

	public void delete(final claseSinNombre claseSinNombre) {
		Rookie principal;

		principal = this.rookieService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(principal.getId() == claseSinNombre.getRookie().getId());
		Assert.notNull(claseSinNombre);
		Assert.isTrue(claseSinNombre.getId() != 0);
		Assert.isTrue(claseSinNombre.getIsDraft());

		this.claseSinNombreRepository.delete(claseSinNombre);
	}

	public void delete2(final claseSinNombre claseSinNombre) {

		this.claseSinNombreRepository.delete(claseSinNombre);
	}

	public claseSinNombre findOne(final int cSNId) {
		claseSinNombre result;

		result = this.claseSinNombreRepository.findOne(cSNId);
		Assert.notNull(result);
		return result;
	}

	public Collection<claseSinNombre> findAll() {
		Collection<claseSinNombre> result;

		result = this.claseSinNombreRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	// Other business methods

	public Collection<claseSinNombre> findAllByPosition(final int positionId) {
		Collection<claseSinNombre> result;

		result = this.claseSinNombreRepository.findAllByPositionId(positionId);
		return result;
	}

	public Collection<claseSinNombre> findByPosition(final int positionId) {
		Collection<claseSinNombre> result;

		result = this.claseSinNombreRepository.findByPositionId(positionId);
		return result;
	}

	public Collection<claseSinNombre> findByRookie(final int positionId) {
		Collection<claseSinNombre> result;

		result = this.claseSinNombreRepository.findByRookieId(positionId);
		return result;
	}

	public claseSinNombre reconstruct(final claseSinNombre claseSinNombre, final int positionId, final BindingResult binding) {
		claseSinNombre result;
		if (claseSinNombre.getId() == 0) {
			result = claseSinNombre;
			result.setBody(claseSinNombre.getBody());
			result.setPicture(claseSinNombre.getPicture());
			result.setTicker(this.generateTicker());
			result.setPosition(this.positionService.findOne(positionId));
			result.setIsDraft(true);
			result.setRookie(this.rookieService.findByPrincipal());

		} else {
			result = this.claseSinNombreRepository.findOne(claseSinNombre.getId());

			result.setBody(claseSinNombre.getBody());
			result.setPicture(claseSinNombre.getPicture());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.claseSinNombreRepository.flush();
	}

}
