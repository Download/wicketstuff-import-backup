/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wicketstuff.minis.apanel;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.parser.XmlTag;

import java.util.*;
import java.io.Serializable;

/**
 * GridLayout puts markup of components in &lt;table&gt;&lt;table/&gt; according to
 * {@link org.wicketstuff.minis.apanel.GridLayoutConstraint}s. Component without
 * a constraint will be added to the first empty cell. If there is no empty cells for the component
 * WicketRuntimeException will be thrown.
 */
public class GridLayout implements ILayout
{
	private static final long serialVersionUID = 1L;
	private static final Component EMPTY_CELL_COMPONENT = null;

	private final RenderersList renderersList;
	private final int width;
	private final int height;

	private final SortedMap<GridLayoutConstraint, Component> constraintsMap =
			new TreeMap<GridLayoutConstraint, Component>(new GridLayoutConstraintComparator());

	/**
	 * Constructor.
	 *
	 * @param width  width of grid
	 * @param height height of grid
	 */
	public GridLayout(final int width, final int height)
	{
		this(width, height, RenderersList.getDefaultRenderers());
	}

	/**
	 * Constructor.
	 *
	 * @param width	 width of grid
	 * @param height	height of grid
	 * @param renderers list of renderers to customize component rendering in this layout
	 */
	public GridLayout(final int width, final int height, final List<IComponentRenderer<?>> renderers)
	{
		this.height = height;
		this.width = width;
		this.renderersList = new RenderersList(renderers);
	}

	public CharSequence renderComponents(final List<? extends Component> components)
	{
		constraintsMap.clear();

		// TODO check constraints

		fillConstraintsMap(components);

		final StringBuilder stringBuilder = new StringBuilder();
		writeOutput(stringBuilder);
		return stringBuilder;
	}

	private void fillConstraintsMap(final List<? extends Component> components)
	{
		final List<Component> componentsWithNoConstraint = new ArrayList<Component>();

		for (Component component : components)
		{
			//noinspection unchecked
			final List<IBehavior> behaviors = component.getBehaviors();

			boolean isConstraintFound = false;
			for (IBehavior behavior : behaviors)
			{
				if (behavior instanceof GridLayoutConstraint)
				{
					constraintsMap.put((GridLayoutConstraint) behavior, component);
					isConstraintFound = true;
					break;
				}
			}

			if (!isConstraintFound)
			{
				componentsWithNoConstraint.add(component);
			}
		}

		for (Component component : componentsWithNoConstraint)
		{
			final GridLayoutConstraint constraint = findEmptyCell();
			if (constraint != null)
			{
				constraintsMap.put(constraint, component);
			}
			else
			{
				throw new WicketRuntimeException("There is no free cells in grid for the component " + component);
			}
		}

		assert constraintsMap.size() == components.size();

		// filling cells that has no components so that they can be rendered
		for (GridLayoutConstraint constraint = findEmptyCell(); constraint != null; constraint = findEmptyCell())
		{
			constraintsMap.put(constraint, EMPTY_CELL_COMPONENT);
		}
	}

	private void writeOutput(final StringBuilder stringBuilder)
	{
		stringBuilder.append("<table>");

		final GridConstraintIterator iterator = new GridConstraintIterator(constraintsMap.entrySet());
		while (iterator.hasNext())
		{
			final Map.Entry<GridLayoutConstraint, Component> entry = iterator.next();
			final GridLayoutConstraint constraint = entry.getKey();
			final Component component = entry.getValue();

			CharSequence markup = "";
			if (component != EMPTY_CELL_COMPONENT)
			{
				final IComponentRenderer<Component> componentRenderer =
						renderersList.findRendererForClass(component.getClass());
				markup = componentRenderer.getMarkup(component);
			}

			if (iterator.isNewRow()) stringBuilder.append("</tr>");
			if (iterator.isNewRow() || iterator.isAtFirstConstraint())
			{
				final XmlTag xmlTag = createXmlTag("tr", XmlTag.OPEN);
				onGridRow(xmlTag);
				stringBuilder.append(xmlTag.toCharSequence());
			}

			final XmlTag xmlTag = createXmlTag("td", XmlTag.OPEN);
			onGridCell(component, xmlTag);
			if (constraint.getColSpan() > 1) xmlTag.put("colspan", constraint.getColSpan());
			if (constraint.getRowSpan() > 1) xmlTag.put("rowspan", constraint.getRowSpan());
			stringBuilder.append(xmlTag.toCharSequence());

			stringBuilder.append(markup);

			stringBuilder.append("</td>");
		}

		stringBuilder.append("</tr>");
		stringBuilder.append("</table>");
	}

	private static XmlTag createXmlTag(final String name, final XmlTag.Type type)
	{
		final XmlTag xmlTag = new XmlTag();
		xmlTag.setType(type);
		xmlTag.setName(name);
		return xmlTag;
	}

	/**
	 * Might be overriden to modify &lt;tr&gt; tag
	 *
	 * @param xmlTag &lt;tr&gt; tag
	 */
	protected void onGridRow(final XmlTag xmlTag)
	{
	}

	/**
	 * Might be overriden to modify &lt;td&gt; tag
	 *
	 * @param component component
	 * @param xmlTag	&lt;td&gt; tag
	 */
	protected void onGridCell(final Component component, final XmlTag xmlTag)
	{
	}

	private GridLayoutConstraint findEmptyCell()
	{
		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				if (!isIntersectingWithAnyConstraint(col, row))
				{
					return new GridLayoutConstraint(col, row);
				}
			}
		}
		return null;
	}

	private boolean isIntersectingWithAnyConstraint(final int col, final int row)
	{
		for (GridLayoutConstraint constraint : constraintsMap.keySet())
		{
			if (constraint.contains(col, row))
			{
				return true;
			}
		}
		return false;
	}

	private static class GridConstraintIterator implements Iterator<Map.Entry<GridLayoutConstraint, Component>>
	{
		private final Iterator<Map.Entry<GridLayoutConstraint, Component>> iterator;
		private int currentRow = 0;
		private boolean isNewRow;
		private int currentIndex = 0;

		public GridConstraintIterator(final Set<Map.Entry<GridLayoutConstraint, Component>> set)
		{
			this.iterator = set.iterator();
		}

		public boolean hasNext()
		{
			return iterator.hasNext();
		}

		public Map.Entry<GridLayoutConstraint, Component> next()
		{
			final Map.Entry<GridLayoutConstraint, Component> entry = iterator.next();

			currentIndex++;

			if (currentRow != entry.getKey().getRow())
			{
				currentRow = entry.getKey().getRow();
				isNewRow = true;
			}
			else
			{
				isNewRow = false;
			}

			return entry;
		}

		public boolean isAtFirstConstraint()
		{
			return currentIndex == 1;
		}

		public void remove()
		{
			iterator.remove();
		}

		public boolean isNewRow()
		{
			return isNewRow;
		}
	}

	private static class GridLayoutConstraintComparator implements Comparator<GridLayoutConstraint>, Serializable
	{
		private static final long serialVersionUID = 1L;

		public int compare(final GridLayoutConstraint o1, final GridLayoutConstraint o2)
		{
			if (o1.getRow() > o2.getRow()) return 1;
			if (o1.getRow() < o2.getRow()) return -1;
			if (o1.getCol() > o2.getCol()) return 1;
			if (o1.getCol() < o2.getCol()) return -1;
			return 0;
		}
	}
}
