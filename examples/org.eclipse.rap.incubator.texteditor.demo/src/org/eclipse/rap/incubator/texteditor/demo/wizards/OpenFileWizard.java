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
package org.eclipse.rap.incubator.texteditor.demo.wizards;

import java.io.File;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rap.incubator.texteditor.demo.commands.EditorUtil;
import org.eclipse.rap.incubator.texteditor.demo.commands.PathEditorInput;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class OpenFileWizard extends AbstractNewResourceWizard{

	static final Logger logger = Logger.getLogger(OpenFileWizard.class);
	
	protected OpenFileWizardPage page = null;

	public OpenFileWizard(File container) {
		super(container);
		setWindowTitle("Open File");
	}
	
	@Override
	public void addPages() {
		page = new OpenFileWizardPage("OpenFile");
		page.setTitle("Open File");
		page.setDescription("Open an existing file");
		page.setInitialElementSelections(Collections.singletonList(getSelection()));
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		final IPath filePath = computeFilePath();
		File file = filePath.toFile();
		if (!file.exists()) {
			MessageDialog.openQuestion(getShell(), "Question", "The selected file cannot be found.");
			return false;
		}
		if (filePath.toFile().isDirectory()) {
			return false;
		}
		return openEditor(getWorkbench(), filePath);
	}

	public static boolean openEditor(IWorkbench workbench, IPath path) {
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();
		IEditorDescriptor editorDescriptor = EditorUtil.getDefaultEditor(path);
		if (editorDescriptor == null) {
			MessageDialog.openError(workbenchWindow.getShell()
				,"Error"
				,"There is no editor registered for the file " + path.lastSegment());
			return false;
		}
		else {
			try {
				page.openEditor(new PathEditorInput(path), editorDescriptor.getId());
			}
			catch (PartInitException exception) {
				MessageDialog.openError(
					workbenchWindow.getShell()
					,"Open Editor"
					,exception.getMessage());
				return false;
			}
		}
		return true;
	}
	
	private IPath computeFilePath() {
		return page.getFilePath();
	}
}