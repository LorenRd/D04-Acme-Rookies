/*
 * ActorToStringConverter.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.claseSinNombre;

@Component
@Transactional
public class claseSinNombreToStringConverter implements Converter<claseSinNombre, String> {

	@Override
	public String convert(final claseSinNombre claseSinNombre) {
		String result;

		if (claseSinNombre == null)
			result = null;
		else
			result = String.valueOf(claseSinNombre.getId());

		return result;
	}
}
