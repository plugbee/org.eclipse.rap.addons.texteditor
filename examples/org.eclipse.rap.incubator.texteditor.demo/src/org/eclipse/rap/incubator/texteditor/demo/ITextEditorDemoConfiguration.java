/**
 * <copyright>
 *
 * Copyright (c) 2015 PlugBee. All rights reserved.
 * 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     PlugBee - Initial API and implementation
 *
 * </copyright>
 */
package org.eclipse.rap.incubator.texteditor.demo;

import java.util.HashMap;
import java.util.Map;

public interface ITextEditorDemoConfiguration {
	
	/**
	 * The root path where the files will be stored.
	 */
	public static final String WORKSPACE_DEFAULT_PATH = "D:/www/texteditor";
		
	public static final String BASIC_WORKBENCH_VIEW_ID = "org.eclipse.rap.incubator.texteditor.demo.ui.parts.view";	
	public static final String ID_PERSPECTIVE = "org.eclipse.rap.incubator.texteditor.demo.ui.parts.perspective";
	public static final String NAVIGATOR_VIEW_ID = "org.eclipse.rap.incubator.navigator.view";
	public static final String LEFT = "left";
	public static final String TOP_LEFT = "topLeft";
	public static final String BOTTOM_RIGHT = "bottomRight";
	public static final String M_SETTINGS = "settings";
	public static final String WORKSPACE_SETTING = "WorkspacePreferences";
	
	public class Registry implements ITextEditorDemoConfiguration {

		public static Map<String, String> getAavailableFileExtensions() {
			Map<String, String> availableFileExtensions = new HashMap<String, String>();
			availableFileExtensions.put("Java", "java");
			availableFileExtensions.put("JavaScript", "js");
			availableFileExtensions.put("JSON", "json");
			return availableFileExtensions;
		}
	}
}
