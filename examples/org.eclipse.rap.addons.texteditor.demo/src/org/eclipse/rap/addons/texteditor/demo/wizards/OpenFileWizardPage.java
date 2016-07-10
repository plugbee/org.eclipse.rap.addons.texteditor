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
package org.eclipse.rap.addons.texteditor.demo.wizards;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class OpenFileWizardPage extends AbstractNewResourceWizardPage {

	private static final long serialVersionUID = 1L;

	protected ModifyListener validator = new ModifyListener() {

		private static final long serialVersionUID = 1L;

		public void modifyText(ModifyEvent e) {
			setPageComplete(validatePage());
		}
	};

	protected OpenFileWizardPage(String pageName) {
		super(pageName);
	}

	@Override
	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		super.viewerSelectionChanged(event);
		validatePage();
	}

	protected boolean validatePage() {
		setErrorMessage(null);
		return true;
	}
	protected IPath getFilePath() {
		IStructuredSelection selection = (IStructuredSelection) getContainerViewer().getSelection();
		String path = selection.getFirstElement().toString();
		return new Path(path);
	}

	@Override
	public void createControl(Composite parent) {
		Composite workArea = new Composite(parent, SWT.NONE);
		setControl(workArea);
		workArea.setLayout(new GridLayout());
		workArea.setLayoutData(new GridData(SWT.BORDER | GridData.FILL_HORIZONTAL));
		createContainerArea(workArea);
	}

	protected void updateWidgetEnablements() {
		setPageComplete(validatePage());
	}
}