/*
 * This piece of code is dedicated to the wicket project (http://www.wicketframework.org).
 */
package wicket.extensions.markup.html.menubar;


import java.io.Serializable;
import java.util.List;

import wicket.model.Model;


/**
 * The {@code Menu} class represents a complete menu within a {@link MenuBarPanel}. A
 * {@link MenuBarPanel} consist of a {@code List} of {@link Menu}s. 
 *
 * @author Stefan Lindner (lindner@visionet.de)
 */
public class Menu implements Serializable{

	private static final long serialVersionUID = 1L;

	private final Model<String> model;
	private final List<MenuItem> menuItems;
	private boolean visible = true;

	/**
	 * Constructs a menu.
	 *
	 * @param model The {@link Model} that name of the menu
	 * @param menuItems A {@code List} of the {@link MenuItem}s that belong to this {@link Menu}.
	 */
	public Menu(final Model<String> model, final List<MenuItem> menuItems) {
		if (model == null) {
			throw new IllegalArgumentException("argument [model] cannot be null");
		}
		if (menuItems == null) {
			throw new IllegalArgumentException("argument [menuItems] cannot be null");
		}
		if (menuItems.size() < 1) {
			throw new IllegalArgumentException(
					"argument [menuItems] must contain a list of at least one menuItem");
		}
		this.model = model;
		this.menuItems = menuItems;
	}


	/**
	 * Gets the model. It returns the object that wraps the backing model. The model of a
	 * {@link Menu} is used to hold the {@code String} that is used to disply the title
	 * of a menu.
	 * 
	 * @return The model
	 */
	public Model<String> getModel() {
		return this.model;
	}


	/**
	 * Gets the {@link MenuItem}s of this {@link Menu}.
	 *
	 * @return All {@link MenuItem}s of this {@link Menu}.
	 */
	public List<MenuItem> getMenuItems() {
		return this.menuItems;
	}


	/**
	 * Sets wheter the complete {@link Menu} is visible.
	 *
	 * @param visible {@code true} if this {@link Menu} should be visible.
	 * @return this
	 */
	public Menu setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}


	/**
	 * Gets whether the {@link Menu} is visible.
	 *
	 * @return {@code true} if the {@link Menu} is visible.
	 */
	public boolean isVisible() {
		return this.visible;
	}
}
