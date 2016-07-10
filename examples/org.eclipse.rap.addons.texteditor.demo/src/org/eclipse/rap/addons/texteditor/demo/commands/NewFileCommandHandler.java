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

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rap.addons.texteditor.demo.ITextEditorDemoConfiguration;
import org.eclipse.rap.addons.texteditor.demo.internal.Activator;
import org.eclipse.rap.addons.texteditor.demo.wizards.NewFileWizard;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;


public class NewFileCommandHandler extends AbstractHandler {

	static final Logger logger = Logger.getLogger(NewFileCommandHandler.class);
	
	public NewFileCommandHandler() {
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		File file = CommandUtil.unwrap(event, File.class);
		if (file == null) {
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String path = store.getDefaultString(ITextEditorDemoConfiguration.WORKSPACE_SETTING);
			file = new Path(path).toFile();
		}
		NewFileWizard wizard = new NewFileWizard(file);
		StructuredSelection selection = new StructuredSelection(file);
		wizard.init(PlatformUI.getWorkbench(), selection);
		WizardDialog wizardDialog = new WizardDialog(activeShell, wizard);
		wizardDialog.create();
		wizardDialog.getShell().setSize(400, 400);
		Rectangle bounds = activeShell.getBounds();
		Rectangle rect = wizardDialog.getShell().getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		wizardDialog.getShell().setLocation(x, y);
		return wizardDialog.open();
	}
	
	public void createFile(IPath filePath) {
		final File file = filePath.toFile();
		if (!file.exists()) {
			Display.getCurrent().syncExec(new Runnable() {
				@Override
				public void run() {
					try {
						file.createNewFile();
					} catch (IOException ex) {
						logger.error(ex.getMessage(), ex);
					}
				}
			});
		}
	}
}
