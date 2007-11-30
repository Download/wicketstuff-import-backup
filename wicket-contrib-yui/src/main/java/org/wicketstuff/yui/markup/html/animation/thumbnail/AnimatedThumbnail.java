package org.wicketstuff.yui.markup.html.animation.thumbnail;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.wicketstuff.yui.behavior.Anim;
import org.wicketstuff.yui.behavior.AnimEffect;
import org.wicketstuff.yui.behavior.Attributes;
import org.wicketstuff.yui.behavior.Easing;
import org.wicketstuff.yui.helper.CSSInlineStyle;
import org.wicketstuff.yui.markup.html.image.URIImagePanel;
/**
 * This component shows a thumbnail image which will 
 * a) animate when the mouseover it with some caption
 * b) displays an actual picture when it is clicked.
 * 
 * @author josh
 *
 */
@SuppressWarnings("serial")
public class AnimatedThumbnail extends AnimatedItem 
{
	private AnimatedThumbnailSettings settings;
	
	public AnimatedThumbnail(String id, AnimatedThumbnailSettings settings)
	{
		super(id);
		this.settings = settings;
		add(HeaderContributor.forCss(AnimatedThumbnail.class, "AnimatedThumbnail.css"));
		init();
	}

	/*
	 * (non-Javadoc)
	 * @see org.wicketstuff.yui.markup.html.animation.thumbnail.AnimatedItem#newMouseoverItem(java.lang.String)
	 */
	@Override
	public Component newMouseoverItem(String id)
	{
		Component mouseover = new Label(id, getSettings().getCaptionText());
		mouseover.add(new StyleSizeModifier());
		return mouseover;
	}

	@Override
	public AnimEffect mouseoverEffect()
	{
		Attributes attributes = new Attributes();
		attributes.add(SHOW_ATTRIBUTE);
		attributes.add(new Attributes("opacity", 0, getSettings().getOpacity()));
		return new Anim(AnimEffect.Type.Anim, attributes, 0, Easing.easeNone);	
	}
	
	@Override
	public AnimEffect mouseoutEffect()
	{
		Attributes attributes = new Attributes();
		attributes.add(HIDE_ATTRIBUTE);
		attributes.add(new Attributes("opacity", getSettings().getOpacity(), 0f));
		return new Anim(AnimEffect.Type.Anim, attributes, 1, Easing.easeNone);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.wicketstuff.yui.markup.html.animation.thumbnail.AnimatedItem#newOnclickItem(java.lang.String)
	 */
	@Override
	public Component newOnclickItem(String id)
	{
		Component onclick = new URIImagePanel(id, getSettings().getPictureURI());
		onclick.add(new StyleSizeModifier());
		return onclick;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.wicketstuff.yui.markup.html.animation.thumbnail.AnimatedItem#onselectEffect()
	 */
	@Override
	public AnimEffect onselectEffect()
	{
		Attributes attributes = new Attributes();
		attributes.add(SHOW_ATTRIBUTE);
		attributes.add(new Attributes("width", 0, getSettings().getPictureWidth()));
		attributes.add(new Attributes("height", 0, getSettings().getPictureHeight()));
		attributes.add(new Attributes("top", 0, getSettings().getPictureTop()));
		attributes.add(new Attributes("left", 0, getSettings().getPictureLeft()));
		
		return new Anim(AnimEffect.Type.Anim, attributes, 1, Easing.easeNone);	
	}
	
	@Override
	public AnimEffect onunselectEffect()
	{
		Attributes attributes = new Attributes();
		attributes.add(HIDE_ATTRIBUTE);
		attributes.add(new Attributes("width", getSettings().getPictureWidth(), 0));
		attributes.add(new Attributes("height", getSettings().getPictureHeight(), 0));
		return new Anim(AnimEffect.Type.Anim, attributes, 1, Easing.easeNone);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.wicketstuff.yui.markup.html.animation.thumbnail.AnimatedItem#newOnloadItem(java.lang.String)
	 */
	@Override
	public Component newOnloadItem(String id)
	{
		Component onload = new URIImagePanel(id, getSettings().getThumbnailURI());
		onload.add(new StyleSizeModifier());
		return onload;
	}
	
	/**
	 * Style modifier to modifier the width and height 
	 * @author josh
	 *
	 */
	private class StyleSizeModifier extends AttributeModifier
	{
		public StyleSizeModifier()
		{
			super("style", true, new AbstractReadOnlyModel()
			{
				@Override
				public Object getObject()
				{
					CSSInlineStyle style = new CSSInlineStyle();
					style.add("width", getSettings().getThumbnailWidth() + "px");
					style.add("height", getSettings().getThumbnailHeight() + "px");
					return style.getStyle();
				}
			});
		}
	}
	
	public AnimatedThumbnailSettings getSettings()
	{
		return settings;
	}

	public void setSettings(AnimatedThumbnailSettings settings)
	{
		this.settings = settings;
	}
}
