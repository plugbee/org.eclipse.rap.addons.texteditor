/**
 * <copyright>
 *
 * Copyright (c) 2016 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * </copyright>
 */
package org.eclipse.rap.incubator.texteditor.javascript.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.rap.incubator.basictext.BasicText;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Composite;

public class Javascript extends BasicText {
	
	private static final long serialVersionUID = 1L;
	public static final String REMOTE_TYPE = "org.eclipse.rap.incubator.texteditor.javascript.widget.Javascript";
	
	public Javascript(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected RemoteObject createRemoteObject(Connection connection) {
		return connection.createRemoteObject(REMOTE_TYPE);
	}
	
	@Override 
	protected void setupClient() {
		super.setupClient();
		List<IPath> languageResources = new ArrayList<IPath>();	
		languageResources.add(new Path("src-js/org/eclipse/rap/incubator/texteditor/javascript/ace/mode-javascript.js"));
		languageResources.add(new Path("src-js/org/eclipse/rap/incubator/texteditor/javascript/ace/worker-javascript.js"));
		registerJsResources(languageResources, getClassLoader());
		loadJsResources(languageResources);
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = Javascript.class.getClassLoader();
		return classLoader;
	}
}