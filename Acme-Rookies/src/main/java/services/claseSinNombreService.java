
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
import domain.Position;
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
	private Validator					validator;


	// Simple CRUD Methods

	public boolean exists(final Integer arg0) {
		return this.claseSinNombreRepository.exists(arg0);
	}

	private String generateTicker() {
		String result;
		final Calendar now = Calendar.getInstance();
		String year = String.valueOf(now.get(Calendar.YEAR));
		year = year.substring(year.length() - 2, year.length());
		final String month = String.valueOf(now.get(Calendar.MONTH) + 1);
		String date = String.valueOf(now.get(Calendar.DATE));
		date = date.length() == 1 ? "0".concat(date) : date;
		final Random r = new Random();
		final char a = (char) (r.nextInt(26) + 'a');
		final char b = (char) (r.nextInt(26) + 'a');
		final char c = (char) (r.nextInt(26) + 'a');
		final char d = (char) (r.nextInt(26) + 'a');
		final char f = (char) (r.nextInt(26) + 'a');
		final char g = (char) (r.nextInt(26) + 'a');
		String code = String.valueOf(a) + String.valueOf(b) + String.valueOf(c) + String.valueOf(d) + String.valueOf(f) + String.valueOf(g);
		code = code.toUpperCase();
		result = year + month + date + "-" + code;
		return result;
	}

	public claseSinNombre create(final Position position) {
		claseSinNombre result;
		Rookie principal;

		principal = this.rookieService.findByPrincipal();
		Assert.notNull(principal);

		result = new claseSinNombre();

		result.setTicker(this.generateTicker());
		result.setPublicationMoment(new Date(System.currentTimeMillis() - 1));
		result.setIsDraft(true);
		result.setPosition(position);
		result.setRookie(principal);
		return result;
	}

	public claseSinNombre save(final claseSinNombre cSN, final Boolean draft) {
		claseSinNombre result;
		Rookie principal;

		principal = this.rookieService.findByPrincipal();
		Assert.notNull(principal);

		Assert.notNull(cSN);
		Assert.isTrue(cSN.getRookie() == principal);
		cSN.setIsDraft(draft);

		result = this.claseSinNombreRepository.save(cSN);
		Assert.notNull(result);
		return result;
	}

	public void delete(final claseSinNombre cSN) {

		Rookie principal;

		principal = this.rookieService.findByPrincipal();
		Assert.notNull(principal);

		Assert.isTrue(principal.getId() == cSN.getRookie().getId());
		Assert.notNull(cSN);
		Assert.isTrue(cSN.getId() != 0);
		Assert.isTrue(cSN.getIsDraft());

		this.claseSinNombreRepository.delete(cSN);
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

	public claseSinNombre reconstruct(final claseSinNombre cSN, final BindingResult binding) {
		claseSinNombre result;
		if (cSN.getId() == 0) {
			result = cSN;
			result.setRookie(this.rookieService.findByPrincipal());
			result.setIsDraft(true);
			result.setPublicationMoment(new Date(System.currentTimeMillis() - 1));
			result.setPosition(cSN.getPosition());

			if (cSN.getPosition() == null)
				binding.rejectValue("position", "claseSinNombre.validation.position", "Can't be null");

		} else {
			result = this.claseSinNombreRepository.findOne(cSN.getId());

			result.setTicker(cSN.getTicker());
			result.setBody(cSN.getBody());
			result.setPicture(cSN.getPicture());
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.claseSinNombreRepository.flush();
	}

}
