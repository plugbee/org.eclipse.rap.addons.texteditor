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
 * Contributors:
 *     PlugBee - Initial API and implementation
 *
 * </copyright>
 */
package org.eclipse.rap.addons.texteditor.demo.wizards;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rap.addons.texteditor.demo.ITextEditorDemoConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class AboutDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	private Label aboutLabel;
	private final String title;
	private final String message;
	private final int columns = 1;
	
	public AboutDialog(Shell parent, String title, String message) {
		super(parent);
		this.title = title;
		this.message = message;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (title != null) {
			shell.setText(title);
		}
		// Workaround for RWT Text Size Determination
		shell.addListener(SWT.Resize, new Listener() {
			private static final long serialVersionUID = 1L;
			public void handleEvent(Event event) {
				initializeBounds();
			}
		});
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		aboutLabel = new Label(composite, SWT.NONE);
		GridData messageData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		messageData.horizontalSpan = 2;
		aboutLabel.setLayoutData(messageData);
		initilizeDialogArea();
		Table table = new Table(parent, SWT.FILL | SWT.CENTER | SWT.BORDER);
		GridData tableData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		table.setLayoutData(tableData);

		Set<String> metamodels = ITextEditorDemoConfiguration.Registry.getAavailableFileExtensions().keySet();
		int preferredWidth = getPreferredWidth(metamodels);
		for (int i = 0; i < columns; i++) {
			final TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Available contributions");
			column.setToolTipText("Deployed languages");
			column.setMoveable(false);
			column.setWidth(convertWidthInCharsToPixels(preferredWidth));
		}

		for (String metamodel : metamodels) {
			addItem(table, metamodel);
		}
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		Menu menu = new Menu(table);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("Menu for Table");
		table.setMenu(menu);
		return composite;
	}

	private int getPreferredWidth(Set<String> values) {
		int maxValue = 0;
		for (String m : values) {
			if (m.length() > maxValue) {
				maxValue = m.length();
			}
		}
		return maxValue;
	}

	private TableItem addItem(Table table, String value) {
		TableItem item = new TableItem(table, SWT.NONE);
		for (int i = 0; i < columns; i++) {
			item.setText(i, value);
		}
		return item;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", false);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
	}

	private void initilizeDialogArea() {
		if (message != null) {
			aboutLabel.setText(message);
		}
	}
}
