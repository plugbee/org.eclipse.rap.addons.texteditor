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

import static org.eclipse.rap.rwt.RWT.getClient;

import org.eclipse.rap.incubator.basictext.BasicText;
import org.eclipse.rap.rwt.client.service.ClientFileLoader;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.widgets.Composite;

public class Javascript extends BasicText {

	private static final long serialVersionUID = 2976687489855687368L;

	public static final String REMOTE_TYPE = "org.eclipse.rap.incubator.texteditor.javascript.widget.Javascript";

	private static final String ACE_MODE_KEY = "org.eclipse.rap.incubator.basictext.ace.mode-javascript";
	private static final String ACE_MODE_VALUE = "https://cdnjs.cloudflare.com/ajax/libs/ace/1.2.4/mode-javascript.js";
	
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
		getClient().getService(ClientFileLoader.class).requireJs(System.getProperty(ACE_MODE_KEY, ACE_MODE_VALUE));
	}

	@Override
	protected ClassLoader getClassLoader() {
		ClassLoader classLoader = Javascript.class.getClassLoader();
		return classLoader;
	}
}