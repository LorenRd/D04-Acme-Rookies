
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
import domain.Audit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class AuditServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private AuditService auditService;


	/*
	 * Percentage of service tested: 25%
	 */

	// --------------------------------------------------

	/*
	 * 3. An actor who is authenticated as an auditor must be able to:
	 * 2. Manage their audits, which includes listing, showing, creating, updating, and deleting them.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como auditor
	 * puede crear una auditoria
	 */

	@Test
	public void createTest() {
		final Object createTest[][] = {
			{
				/*
				 * Test negativo:
				 * Probamos como rookie
				 */
				"rookie1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * Probamos como auditor
				 */
				"auditor1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (Class<?>) createTest[i][1]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como auditor
	 * puede actualizar una auditoria
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Dejar el campo de text vacío
				 */
				"auditor1", "audit2", null, ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Introducir otra descripción
				 */
				"auditor1", "audit2", "other description", null
			}
		};
		for (int i = 0; i < updateTest.length; i++)
			this.UpdateTemplate((String) updateTest[i][0], (String) updateTest[i][1], (String) updateTest[i][2], (Class<?>) updateTest[i][3]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como proveedor
	 * puede eliminar un producto.
	 */

	@Test
	public void deleteTest() {
		final Object deleteTest[][] = {
			{
				/*
				 * Test negativo:
				 * Alguien sin autentificar intenta eliminar una auditoria
				 */
				null, "audit2", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * El auditor al que le pertenece lo elimina
				 */
				"auditor1", "audit2", null
			}
		};
		for (int i = 0; i < deleteTest.length; i++)
			this.DeleteTemplate((String) deleteTest[i][0], super.getEntityId((String) deleteTest[i][1]), (Class<?>) deleteTest[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	private void CreateTemplate(final String actor, final Class<?> class1) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(actor);
			this.auditService.create();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String description, final Class<?> class1) {
		Class<?> caught;
		Audit audit;
		caught = null;
		try {
			this.authenticate(actor);
			audit = this.auditService.findOne(this.getEntityId(thing));
			audit.setText(description);
			this.auditService.save(audit, true);
			this.unauthenticate();
			this.auditService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final int thing, final Class<?> class1) {
		Class<?> caught;
		Audit audit;

		caught = null;
		try {
			this.authenticate(actor);
			audit = this.auditService.findOne(thing);
			this.auditService.delete(audit);
			this.auditService.flush();
			this.unauthenticate();
			Assert.isTrue(!this.auditService.exist(audit.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
