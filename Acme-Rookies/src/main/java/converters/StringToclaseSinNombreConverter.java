
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.claseSinNombreRepository;
import domain.claseSinNombre;

@Component
@Transactional
public class StringToclaseSinNombreConverter implements Converter<String, claseSinNombre> {

	@Autowired
	claseSinNombreRepository	claseSinNombreRepository;


	@Override
	public claseSinNombre convert(final String text) {
		claseSinNombre result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.claseSinNombreRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
