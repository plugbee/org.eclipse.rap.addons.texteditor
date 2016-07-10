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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.rap.addons.texteditor.demo.ITextEditorDemoConfiguration;
import org.eclipse.rap.addons.texteditor.demo.internal.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

@SuppressWarnings("serial")
public abstract class AbstractNewResourceWizardPage extends WizardPage {

	private Object[] result;
	private List<IStructuredSelection> selections = new ArrayList<IStructuredSelection>();
	private TreeViewer fViewer;
	protected final String rootPath;
	
	public TreeViewer getContainerViewer() {
		return fViewer;
	}

	protected AbstractNewResourceWizardPage(String pageName) {
		super(pageName);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		rootPath = store.getDefaultString(ITextEditorDemoConfiguration.WORKSPACE_SETTING);
	}

	protected TreeViewer createViewer(Composite parent) {
		TreeViewer treeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = convertWidthInCharsToPixels(40);
		gd.heightHint = convertHeightInCharsToPixels(15);
		treeViewer.getTree().setLayoutData(gd);

		treeViewer.setLabelProvider(new FileSystemLabelProvider());
		treeViewer.setContentProvider(new FileSystemContentProvider());
		treeViewer.addFilter(new BasicWorkspaceFilter());


		File root = new Path(rootPath).toFile();
		if (!root.exists()) {
			MessageDialog.openError(getShell(), "Error",
					"The workspace folder of the editor is not properly configured: the folder " + rootPath
							+ " does not exist. Please make sure you have properly created the workspace folder and granted read/write access on it.");
		}
		treeViewer.setInput(root);
		return treeViewer;
	}

	protected void createContainerArea(Composite composite) {
		Composite result = new Composite(composite, SWT.NONE);
		result.setLayout(new GridLayout());
		Label label = new Label(result, SWT.NONE);
		label.setText(rootPath);
		
		fViewer = createViewer(result);
		GridData data = new GridData(GridData.FILL_BOTH);
		result.setLayoutData(data);
		fViewer.getControl().setLayoutData(data);		
		StructuredSelection initialSelection = (StructuredSelection) getSelectedElements().get(0);
		getContainerViewer().setSelection(initialSelection);
		getContainerViewer().setExpandedElements(new Object[] { initialSelection.getFirstElement() });
		getContainerViewer().addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				viewerSelectionChanged(event);
			}
		});
	}

	protected void viewerSelectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		selections = Collections.singletonList(selection);
	}

	protected List<IStructuredSelection> getSelectedElements() {
		return selections;
	}

	public void setInitialSelections(Object[] selectedElements) {
		selections = new ArrayList<IStructuredSelection>(selectedElements.length);
		for (int i = 0; i < selectedElements.length; i++) {
			selections.add((IStructuredSelection) selectedElements[i]);
		}
	}

	public void setInitialElementSelections(List<IStructuredSelection> selectedElements) {
		selections = selectedElements;
	}

	protected void setResult(List<IStructuredSelection> newResult) {
		if (newResult == null) {
			result = null;
		} else {
			result = new Object[newResult.size()];
			newResult.toArray(result);
		}
	}

	protected void setSelectionResult(Object[] newResult) {
		result = newResult;
	}
}