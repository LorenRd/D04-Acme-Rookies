package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Rookie;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml", })
@Transactional
public class RookieServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private RookieService rookieService;
	/*
	 *  Percentage of service tested: 14,7%
	 * 
	 */
	// --------------------------------------------------

	/*
	 * Requirement Tested: 1- Register to the system as a company or a rookie.
	 */

	// Tests

	/*
	 * En este test se va a probar que se deben rellenar los campos obligatorios
	 * para registrarse como rookie
	 */

	@Test
	public void createTestFail() {
		final Object createTest[][] = { {
		/*
		 * Test negativo: Falta introducir un nombre
		 */
		null, "", "Elena", "Hacienda los Olivos", "javierelena@gmail.com",
				"fraelefer", "fraelefer", "+34912345567",
				"https://www.informatica.us.es/docs/imagen-etsii/MarcaUS.png",
				12.0, "Francisco Elena", "MasterCard", "5220 2777 7103 1876",
				9, 23, 627, ConstraintViolationException.class } };
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0],
					(String) createTest[i][1], (String) createTest[i][2],
					(String) createTest[i][3], (String) createTest[i][4],
					(String) createTest[i][5], (String) createTest[i][6],
					(String) createTest[i][7], (String) createTest[i][8],
					(double) createTest[i][9], (String) createTest[i][10],
					(String) createTest[i][11], (String) createTest[i][12],
					(int) createTest[i][13], (int) createTest[i][14],
					(int) createTest[i][15], (Class<?>) createTest[i][16]);
	}

	@Test
	public void createTestSuccess() {
		final Object createTest[][] = { {
		/*
		 * Test positivo:
		 */
		null, "Javier", "Fernández", "Hacienda los Olivos",
				"javierelena@gmail.com", "javferele", "javferele",
				"+34912345567",
				"https://www.informatica.us.es/docs/imagen-etsii/MarcaUS2.png",
				13.0, "Javier Fernández", "MasterCard", "5220 2777 7103 1876",
				11, 25, 227, null } };
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0],
					(String) createTest[i][1], (String) createTest[i][2],
					(String) createTest[i][3], (String) createTest[i][4],
					(String) createTest[i][5], (String) createTest[i][6],
					(String) createTest[i][7], (String) createTest[i][8],
					(double) createTest[i][9], (String) createTest[i][10],
					(String) createTest[i][11], (String) createTest[i][12],
					(int) createTest[i][13], (int) createTest[i][14],
					(int) createTest[i][15], (Class<?>) createTest[i][16]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final String name,
			final String surname, final String address, final String email,
			final String username, final String password, final String phone,
			final String photo, final double vatNumber,
			final String holderName, final String brandName,
			final String number, final int month, final int year,
			final int cvv, final Class<?> class1) {
		Class<?> caught;
		Rookie rookie;

		caught = null;
		try {
			rookie = this.rookieService.create();
			rookie.setName(name);
			rookie.setSurname(surname);
			rookie.setAddress(address);
			rookie.setEmail(email);
			rookie.getUserAccount().setUsername(username);
			rookie.getUserAccount().setPassword(password);
			rookie.setPhone(phone);
			rookie.setPhoto(photo);
			rookie.setVatNumber(vatNumber);
			rookie.getCreditCard().setHolderName(holderName);
			rookie.getCreditCard().setBrandName(brandName);
			rookie.getCreditCard().setCVV(cvv);
			rookie.getCreditCard().setExpirationMonth(month);
			rookie.getCreditCard().setExpirationYear(year);
			rookie.getCreditCard().setNumber(number);
			final Rookie saved = this.rookieService.save(rookie);
			this.rookieService.flush();
			Assert.notNull(this.rookieService.findOne(saved.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
