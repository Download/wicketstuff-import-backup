package wicket.contrib.scriptaculous.sortable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wicket.AttributeModifier;
import wicket.MarkupContainer;
import wicket.ajax.AjaxRequestTarget;
import wicket.behavior.AbstractAjaxBehavior;
import wicket.contrib.scriptaculous.JavascriptBuilder;
import wicket.contrib.scriptaculous.ScriptaculousAjaxHandler;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.Model;

/**
 *
 * @see http://wiki.script.aculo.us/scriptaculous/show/Sortable.create
 * @author <a href="mailto:wireframe6464@sf.net">Ryan Sonnek</a>
 */
public abstract class SortableContainer extends WebMarkupContainer {
	private AbstractAjaxBehavior onUpdateHandler;
	private Map options = new HashMap();

	public SortableContainer(MarkupContainer parent, String id, String itemId, final List items) {
		super(parent, id);

		setOutputMarkupId(true);

		onUpdateHandler = new ScriptaculousAjaxHandler() {
			protected String getImplementationId() {
				return "scriptaculous";
			}

			public void onRequest() {
				AjaxRequestTarget target = new AjaxRequestTarget();
				String[] parameters = getRequestCycle().getRequest().getParameters(getMarkupId() + "[]");

				if (parameters != null) {
					List originalItems = new ArrayList(items);
					for (int index = 0; index < items.size(); index++) {
						int newIndex = Integer.parseInt(parameters[index]);
						items.set(index, originalItems.get(newIndex));
					}
				}

				getRequestCycle().setRequestTarget(target);

				target.addComponent(getComponent());
			}
		};
		add(onUpdateHandler);

		new ListView(this, itemId, items) {
			protected void populateItem(ListItem item) {
				item.add(new AttributeModifier("id", true, new Model(getMarkupId() + "_" + item.getIndex())));

				populateItemInternal(item);
			}
		};

		options.put("onUpdate", new JavascriptBuilder.JavascriptFunction("function(element) { wicketAjaxGet('" + onUpdateHandler.getCallbackUrl() + "&' + Sortable.serialize(element)); }"));
	}

	public void setConstraint(boolean value) {
		options.put("constraint", Boolean.valueOf(value));
	}
	protected abstract void populateItemInternal(ListItem item);

	protected void onRender(MarkupStream markupStream) {
		super.onRender(markupStream);

		JavascriptBuilder builder = new JavascriptBuilder();
		builder.addLine("Sortable.create('" + getMarkupId() + "', ");
		builder.addOptions(options);
		builder.addLine(");");
		getResponse().write(builder.buildScriptTagString());
	}
}
