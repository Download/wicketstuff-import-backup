package org.wicketstuff.objectautocomplete;

import java.util.Iterator;
import java.io.Serializable;

/**
 * Interfaces to specify a producer for items in an autocompletion menu
 * based on a single search string
 *
 * @author roland
 * @since May 24, 2008
 */
public interface AutoCompletionChoicesProvider<T> extends Serializable {

	/**
	 * Callback method that should return an iterator over all possible choice objects. These
	 * objects will be passed to the renderer to generate output for the autocomplete menu
     *
	 * @param input current input
	 * @return iterator over all possible choice objects
	 */
    Iterator<T> getChoices(String input);
}
