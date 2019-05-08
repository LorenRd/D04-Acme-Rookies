package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Auditor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml", })
@Transactional
public class AuditorServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private AuditorService auditorService;

	/*
	 * Percentage of service tested: AuditorService 21,2%
	 */

	// --------------------------------------------------

	/*
	 * Requirement Tested: 4.2 Create useraccounts for new auditors
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un administrator puede crear otra
	 * cuenta de auditor
	 */

	@Test
	public void createTestFail() {
		final Object createTest[][] = { {
		/*
		 * Test negativo: Alguien sin loguear intenta crear una cuenta de
		 * auditor
		 */
		null, "Lorenzo", "Rondán Domínguez", "http://mifoto.com",
				"+34912123123", "C/ A nº1 Sevilla", 1.0, "loren@gmail.com", "auditor10",
				"auditor10", "Lorenzo Domínguez", "MasterCard",
				"5220 2777 7103 1876", 7, 19, 701,
				IllegalArgumentException.class } };
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0],
					(String) createTest[i][1], (String) createTest[i][2],
					(String) createTest[i][3], (String) createTest[i][4],
					(String) createTest[i][5], (double) createTest[i][6],
					(String) createTest[i][7], (String) createTest[i][8],
					(String) createTest[i][9], (String) createTest[i][10],
					(String) createTest[i][11], (String) createTest[i][12],
					(int) createTest[i][13], (int) createTest[i][14],
					(int) createTest[i][15], (Class<?>) createTest[i][16]);
	}

	@Test
	public void createTestSuccess() {
		final Object createTest[][] = { {
		/*
		 * Test positivo: Un administrador crea otra cuenta de auditor
		 */
		"admin", "Nueva", "Nueva Nueva", "http://mifoto2.com", "+34910323123",
				"C/ B nº2 Sevilla", 2.0, "nueva@gmail.com", "auditor10", "auditor10",
				"Nueva Nueva", "MasterCard", "5169 8379 5973 7918", 8, 21, 703,
				null } };
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0],
					(String) createTest[i][1], (String) createTest[i][2],
					(String) createTest[i][3], (String) createTest[i][4],
					(String) createTest[i][5], (double) createTest[i][6],
					(String) createTest[i][7], (String) createTest[i][8],
					(String) createTest[i][9], (String) createTest[i][10],
					(String) createTest[i][11], (String) createTest[i][12],
					(int) createTest[i][13], (int) createTest[i][14],
					(int) createTest[i][15], (Class<?>) createTest[i][16]);
	}


	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String name,
			final String surname, final String photo, final String phone,
			final String address, final double vatNumber, final String email,
			final String username, final String password,
			final String holderName, final String brandName,
			final String number, final int month, final int year,
			final int cvv, final Class<?> class1) {
		Class<?> caught;
		Auditor auditor;

		caught = null;
		try {
			this.authenticate(actor);
			auditor = this.auditorService.create();
			auditor.setName(name);
			auditor.setSurname(surname);
			auditor.setPhoto(photo);
			auditor.setPhone(phone);
			auditor.setAddress(address);
			auditor.setVatNumber(vatNumber);
			auditor.setEmail(email);

			auditor.getUserAccount().setUsername(username);
			auditor.getUserAccount().setPassword(password);

			auditor.getCreditCard().setHolderName(holderName);
			auditor.getCreditCard().setBrandName(brandName);
			auditor.getCreditCard().setCVV(cvv);
			auditor.getCreditCard().setExpirationMonth(month);
			auditor.getCreditCard().setExpirationYear(year);
			auditor.getCreditCard().setNumber(number);

			final Auditor saved = this.auditorService.save(auditor);
			this.unauthenticate();
			this.auditorService.flush();
			Assert.notNull(this.auditorService.findOne(saved.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

}
