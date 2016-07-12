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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class TextEditorDemoWindowAdvisor extends WorkbenchWindowAdvisor {

	public TextEditorDemoWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public void preWindowOpen() {
		getWindowConfigurer().setShowPerspectiveBar(false);
		getWindowConfigurer().setShowStatusLine(true);
		getWindowConfigurer().setShowMenuBar(true);
		getWindowConfigurer().setShowCoolBar(true);
		getWindowConfigurer().setTitle("Online Styled Text Editor");
	}

	@Override
	public void postWindowOpen() {
		initializeBounds();
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new TextEditorDemoActionBarAdvisor(configurer);
	}

	private void initializeBounds() {
		Shell shell = getWindowConfigurer().getWindow().getShell();
		Point size = getWindowConfigurer().getInitialSize();
		shell.setSize(size.x * 10 / 16, size.y);
		Monitor primary = shell.getDisplay().getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
	}
}