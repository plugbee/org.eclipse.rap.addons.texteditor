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
package org.eclipse.rap.addons.texteditor.demo;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.rap.addons.texteditor.demo.internal.Activator;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class TextEditorDemoWorkbench implements EntryPoint {

	public static final String DEMO_PRESENTATION = "org.eclipse.rap.addons.texteditor.demo.presentation";

	public int createUI() {
		WorkbenchAdvisor worbenchAdvisor = new TextEditorDemoAdvisor();		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();		
		store.setDefault(ITextEditorDemoConfiguration.WORKSPACE_SETTING, ITextEditorDemoConfiguration.WORKSPACE_DEFAULT_PATH);
		Display display = PlatformUI.createDisplay();
		int result = PlatformUI.createAndRunWorkbench(display, worbenchAdvisor);
		display.dispose();
		return result;
	}
}