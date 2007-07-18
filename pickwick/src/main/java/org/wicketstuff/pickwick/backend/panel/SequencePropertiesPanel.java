package org.wicketstuff.pickwick.backend.panel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.markup.html.richtexteditor.DojoRichTextEditorBehavior;
import org.wicketstuff.pickwick.backend.DefaultSettings;
import org.wicketstuff.pickwick.backend.ImageUtils;
import org.wicketstuff.pickwick.backend.XmlBeanMapper;
import org.wicketstuff.pickwick.bean.Sequence;

import com.google.inject.Inject;

/**
 * FIXME get image directory using wrapped model to avoid replacing the whole panel
 */
public class SequencePropertiesPanel extends Panel {
	private static final Logger log = LoggerFactory.getLogger(SequencePropertiesPanel.class);
	@Inject
	private ImageUtils imageUtils;
	public static final String FORM = "sequenceForm";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String DATE = "date";
	
	public Sequence sequenceProperties;
	
	
	
	public SequencePropertiesPanel(String id, final File imageDirectory) {
		super(id);	

		sequenceProperties = imageUtils.readSequence(imageDirectory);
		log.debug("sequence: " + sequenceProperties);
		
		Form form = new Form(FORM, new CompoundPropertyModel(sequenceProperties)){

			@Override
			protected void onSubmit() {
				Sequence sequence = (Sequence)getModelObject();
				imageUtils.writeSequence(sequence, imageDirectory);
				log.info("Wrote sequence: " + sequence + " to image directory: " + imageDirectory);
			}
			
		};
		
		TextField title = new TextField(TITLE);
		TextArea description = new TextArea(DESCRIPTION);
		description.add(new DojoRichTextEditorBehavior());
		DojoDatePicker date = new DojoDatePicker(DATE, "yyyy-MM-dd");
		
		form.add(title);
		form.add(description);
		form.add(date);
		
		add(form);
		add(new FeedbackPanel("feedback"));
	}
}
