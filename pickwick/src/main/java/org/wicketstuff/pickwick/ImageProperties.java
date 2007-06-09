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
package org.wicketstuff.pickwick;

import java.io.File;
import java.io.Serializable;

/**
 * @author <a href="mailto:jbq@apache.org">Jean-Baptiste Quenot</a>
 * @version CVS $Id$
 */
final public class ImageProperties implements Serializable {
    public File file;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("file: ").append(file);
        return sb.toString();
    }

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
