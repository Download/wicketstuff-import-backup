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
package org.wicketstuff.pickwick.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.wicketstuff.pickwick.ImageProperties;
import org.wicketstuff.pickwick.PickWickApplication;
import org.wicketstuff.pickwick.bean.Sequence;

/**
 * Various utility methods
 * 
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 */
final public class ImageUtils {
	private Settings settings;

	private FileFilter imageFilter;

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	final public static ImageProperties getImageProperties(File file) throws FileNotFoundException, IOException {
		ImageProperties p = new ImageProperties();
		p.setFile(file);
		return p;
		// TODO load sequence information: image title, caption, date, ...
	}

	public static boolean isImage(File arg0) {
		return arg0.getName().toLowerCase().endsWith(".jpg");
	}

	public static long getSequenceDateMillis(File o1) {
		File sequence = getSequenceFile(o1);
		if (sequence.exists())
			// FIXME get date from sequence file
			return 0;
		else
			return o1.lastModified();
	}

	private static File getSequenceFile(File o1) {
		return new File(o1, "sequence.xml");
	}

	public static Sequence getSequence(File dir) {
		File sequence = getSequenceFile(dir);
		if (sequence.exists()) {
			// FIXME
			return null;
		}
		return null;
	}

	public String getRelativePath(File imageFile) throws IOException {
		if (settings.getImageDirectoryRoot().getCanonicalPath().equals(imageFile.getCanonicalPath()))
			return new String();
		return imageFile.getCanonicalPath().substring(
				(int) settings.getImageDirectoryRoot().getCanonicalPath().length() + 1);
	}

	public String buildSequencePath(File dir) throws IOException {
		return PickWickApplication.SEQUENCE_PAGE_PATH + "/" + getRelativePath(dir);
	}

	public String getPreviousImage(String uri) throws IOException {
		File file = new File(settings.getImageDirectoryRoot(), uri);
		File previous = getSiblingImage(file, -1);
		if (previous == null)
			return null;
		return getRelativePath(previous);
	}

	public String getNextImage(String uri) throws IOException {
		File file = new File(settings.getImageDirectoryRoot(), uri);
		File next = getSiblingImage(file, 1);
		if (next == null)
			return null;
		return getRelativePath(next);
	}

	public File getSiblingImage(File file, int offset) {
		File folder = file.getParentFile();
		boolean next = false;
		File[] children = folder.listFiles(imageFilter);
		if (children.length == 0)
			throw new RuntimeException("Nothing in sequence " + folder);
		List list = Arrays.asList(children);
		int index = list.indexOf(file);
		if (index + offset < 0 || index + offset > children.length - 1)
			return null;
		return children[index + offset];
	}

	public String getFirstImage(String uri) throws IOException {
		File file = new File(settings.getImageDirectoryRoot(), uri);
		File first = getFirstImage(file);
		if (first == null)
			return null;
		return getRelativePath(first);
	}

	public String getLastImage(String uri) throws IOException {
		File file = new File(settings.getImageDirectoryRoot(), uri);
		File last = getLastImage(file);
		if (last == null)
			return null;
		return getRelativePath(last);
	}

	public File getFirstImage(File file) {
		File folder = file.getParentFile();
		File[] children = folder.listFiles(imageFilter);
		if (children.length == 0)
			throw new RuntimeException("Nothing in sequence " + folder);
		if (children[0].equals(file))
			return null;
		return children[0];
	}

	public File getLastImage(File file) {
		File folder = file.getParentFile();
		File[] children = folder.listFiles(imageFilter);
		if (children.length == 0)
			throw new RuntimeException("Nothing in sequence " + folder);
		if (children[children.length - 1].equals(file))
			return null;
		return children[children.length - 1];
	}

	public FileFilter getImageFilter() {
		return imageFilter;
	}

	public void setImageFilter(FileFilter imageFilter) {
		this.imageFilter = imageFilter;
	}
}
