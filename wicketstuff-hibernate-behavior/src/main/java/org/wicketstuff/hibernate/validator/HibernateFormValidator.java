package org.wicketstuff.hibernate.validator;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * <p>Validates Model's object over Hibernate Validator framework</p>
 * 
 * @author miojo
 */
public class HibernateFormValidator implements IFormValidator {

	private Class clazz;

	@SuppressWarnings("unchecked")
	public HibernateFormValidator(Class name) {
		clazz = name;
	}

	public FormComponent[] getDependentFormComponents() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void validate(Form form) {
		Object object = form.getModelObject();
		
		ClassValidator validator = new ClassValidator(clazz);
		InvalidValue[] invalidValues = validator.getInvalidValues(object);

		for (InvalidValue iv : invalidValues) {
			form.error(iv.getMessage());
		}
	}

}