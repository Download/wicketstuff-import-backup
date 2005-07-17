package wicket.contrib.data.model.hibernate.binding;

import wicket.Component;
import wicket.model.AbstractModel;
import wicket.model.IModel;

/**
 * A convenience class to represent the attribute value in an attribute
 * modifier.
 * 
 * @author Phil Kulak
 */
public abstract class AttributeModel extends AbstractModel
{
	public IModel getNestedModel()
	{
		return null;
	}

	public Object getObject(Component component)
	{
		return getAttributeValue();
	}

	public void setObject(Component component, Object object)
	{
		throw new UnsupportedOperationException("Attribute models can "
				+ "not have their models set.");
	}

	protected abstract String getAttributeValue();
}
