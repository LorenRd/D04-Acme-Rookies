
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
import domain.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml",
})
@Transactional
public class ItemServiceTest extends AbstractTest {

	// Service under test

	@Autowired
	private ItemService	itemService;


	/*
	 * Percentage of service tested: ??,? %
	 */

	// --------------------------------------------------

	/*
	 * 10. An actor who is authenticated as a provider must be able to:
	 * 1. Manage their items, which includes listing, showing, creating, updating, and deleting them.
	 */

	// Tests

	/*
	 * En este test se va a probar que sólo un actor logueado como provider
	 * puede crear un producto
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
				 * Probamos como proveedor
				 */
				"provider1", null
			}
		};
		for (int i = 0; i < createTest.length; i++)
			this.CreateTemplate((String) createTest[i][0], (Class<?>) createTest[i][1]);
	}

	/*
	 * En este test se va a probar que sólo un actor logueado como proveedor
	 * puede actualizar un producto
	 */

	@Test
	public void updateTest() {
		final Object updateTest[][] = {
			{
				/*
				 * Test negativo:
				 * Dejar el campo de descripción vacío
				 */
				"provider1", "item1", null, ConstraintViolationException.class
			}, {
				/*
				 * Test positivo:
				 * Introducir otra descripción
				 */
				"provider1", "item1", "other description", null
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
				 * Alguien sin autentificar intenta eliminar un producto
				 */
				null, "item1", IllegalArgumentException.class
			}, {
				/*
				 * Test positivo:
				 * El proveedor al que le pertenece lo elimina
				 */
				"provider1", "item1", null
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
			this.itemService.create();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void UpdateTemplate(final String actor, final String thing, final String description, final Class<?> class1) {
		Class<?> caught;
		Item item;
		caught = null;
		try {
			this.authenticate(actor);
			item = this.itemService.findOne(super.getEntityId(thing));
			item.setDescription(description);
			this.itemService.save(item);
			this.unauthenticate();
			this.itemService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}

	private void DeleteTemplate(final String actor, final int thing, final Class<?> class1) {
		Class<?> caught;
		Item item;

		caught = null;
		try {
			this.authenticate(actor);
			item = this.itemService.findOne(thing);
			this.itemService.delete(item);
			this.itemService.flush();
			this.unauthenticate();
			Assert.isTrue(!this.itemService.exist(item.getId()));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(class1, caught);
	}
}
