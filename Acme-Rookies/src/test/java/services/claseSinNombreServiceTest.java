
package services;

import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class claseSinNombreServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private claseSinNombreService claseSinNombreService;


	/*
	 * Percentage of service tested: 34,9%
	 */

	// --------------------------------------------------

	/*
	 * 1. An actor who is authenticated as an company must be able to:
	 * 		- Create a claseSinNombre which clarifies something regarding one of their Audits.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como company
	 * puede crear una claseSinNombre
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test positivo:
				 * Probamos como company
				 */
				"company1", null
			}, {
				/*
				 * Test negativo:
				 * Probamos como rookie
				 */
				"rookie1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (Class<?>) createTest[i][1]);
	}


	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.claseSinNombreService.create();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

}
