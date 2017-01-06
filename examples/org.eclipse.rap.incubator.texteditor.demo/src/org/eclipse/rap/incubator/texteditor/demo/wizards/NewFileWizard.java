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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.dslforge.texteditor.PathEditorInput;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.rap.incubator.texteditor.demo.commands.EditorUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

public class NewFileWizard extends AbstractNewResourceWizard {

	static final Logger logger = Logger.getLogger(NewFileWizard.class);

	protected NewFileWizardPage page = null;

	public NewFileWizard(File container) {
		super(container);
		setWindowTitle("New File");
	}

	@Override
	public void addPages() {
		page = new NewFileWizardPage("NewFile");
		page.setTitle("New File");
		page.setDescription("Create a new file resource");
		page.setInitialElementSelections(Collections.singletonList(getSelection()));
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		IPath path = computeFilePath();
		final File file = page.getFilePath().toFile();
		if (file.exists() || file.isDirectory()) {
			return false;
		}
		if (file.exists()) {
			if (!MessageDialog.openQuestion(getShell(), "Question",
					"The file " + file.getAbsolutePath() + " already exists.  Do you want to replace the existing file?")) {
				return false;
			}
		}
		IRunnableWithProgress operation = new IRunnableWithProgress() {
			public void run(IProgressMonitor progressMonitor) {
				try {
					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException ex) {
							logger.error(ex.getMessage(), ex);
						}
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
				} finally {
					progressMonitor.done();
				}
			}
		};

		try {
			getContainer().run(false, false, operation);
		} catch (InvocationTargetException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (InterruptedException ex) {
			logger.error(ex.getMessage(), ex);
		}
		return openEditor(getWorkbench(), path);
	}

	public void createResource(IPath path) {
		final File file = path.toFile();
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

	public static boolean openEditor(IWorkbench workbench, IPath path) {
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = workbenchWindow.getActivePage();
		IEditorDescriptor editorDescriptor = EditorUtil.getDefaultEditor(path);
		if (editorDescriptor == null) {
			MessageDialog.openError(workbenchWindow.getShell(), "Error",
					"There is no editor registered for the file " + path.lastSegment());
			return false;
		} else {
			try {
				page.openEditor(new PathEditorInput(path), editorDescriptor.getId());
			} catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(), "Open Editor", exception.getMessage());
				return false;
			}
		}
		return true;
	}

	private IPath computeFilePath() {
		return page.getFilePath();
	}
}