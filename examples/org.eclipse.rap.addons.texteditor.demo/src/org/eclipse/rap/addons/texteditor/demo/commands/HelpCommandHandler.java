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
package org.eclipse.rap.addons.texteditor.demo.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.rap.addons.texteditor.demo.wizards.AboutDialog;
import org.eclipse.swt.widgets.Display;

public class HelpCommandHandler extends AbstractHandler {

	static final Logger logger = Logger.getLogger(HelpCommandHandler.class);

	public HelpCommandHandler() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String message = "Built on Eclipse RAP";
		message = "Online Text Editor (Incubation)\n\n"
				+ "(c) Copyright PlugBee 2016.  All rights reserved.\n";
		final AboutDialog loginDialog = new AboutDialog(Display.getCurrent().getActiveShell(), "About Online Text Editor",
				message);
		return loginDialog.open();
	}
}
