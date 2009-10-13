package com.inmethod.grid.common;


import org.apache.wicket.IClusterable;


/**
 * State entry for single column.
 * 
 * @author Matej Knopp
 */
public class Entry  implements IClusterable {

	private static final long serialVersionUID = 1L;

	private final String columnId;
	private int currentWidth = -1;
	private boolean visible = true;

	/**
	 * Creates new entry instance
	 * 
	 * @param columnId
	 */
	public Entry(String columnId) {
		this.columnId = columnId;
	}

	/**
	 * Returns the current width, or -1 if the width is not set. In that case the initial column
	 * width will be used.
	 * 
	 * @return current column width
	 */
	public int getCurrentWidth() {
		return currentWidth;
	}

	/**
	 * Sets the current column width. If <code>currentWidth</code> is -1, the initial column
	 * width will be used.
	 * 
	 * @param currentWidth
	 */
	public void setCurrentWidth(int currentWidth) {
		this.currentWidth = currentWidth;
	}

	/**
	 * Return whether the column is visible.
	 * 
	 * @return <code>true</code> if the column is visible, <code>false</code> otherwise.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visibility of the column.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns column identifier.
	 * 
	 * @return column id
	 */
	public String getColumnId() {
		return columnId;
	};
};
