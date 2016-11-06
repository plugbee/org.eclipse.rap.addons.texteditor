package org.eclipse.rap.incubator.texteditor.demo.commands;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoadFileDialog extends Dialog {
	
	private static final long serialVersionUID = 1L;

	protected static final int CONTROL_OFFSET = 20;
	protected String title;
	protected int style;
	protected Text pathField;
	protected String pathText;

	public LoadFileDialog(Shell parent, String title, int style) {
		super(parent);
		this.title = "load file from disk";
		this.style = style;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		FormLayout layout = new FormLayout();
		composite.setLayout(layout);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 360;
		composite.setLayoutData(gridData);

		Label resourceURILabel = new Label(composite, SWT.LEFT);
		resourceURILabel.setText("Load resource by path");
		FormData data = new FormData();
		data.left = new FormAttachment(0, CONTROL_OFFSET);
		data.top = new FormAttachment(0, CONTROL_OFFSET);
		resourceURILabel.setLayoutData(data);

		Composite buttonComposite = new Composite(composite, SWT.NONE);data = new FormData();
		data.top = new FormAttachment(resourceURILabel, CONTROL_OFFSET, SWT.CENTER);
		data.left = new FormAttachment(resourceURILabel, CONTROL_OFFSET);
		data.right = new FormAttachment(100, -CONTROL_OFFSET);
		buttonComposite.setLayoutData(data);
		buttonComposite.setLayout(new FormLayout());
	
		
		Text rootField = new Text(composite, SWT.BORDER);
		data = new FormData();
		data.top = new FormAttachment(buttonComposite, CONTROL_OFFSET);
		data.left = new FormAttachment(0, CONTROL_OFFSET);
		data.right = new FormAttachment(100, -CONTROL_OFFSET);
		rootField.setLayoutData(data);
		rootField.setText("root");
		
		
		pathField = new Text(composite, SWT.BORDER);
		data = new FormData();
		data.top = new FormAttachment(rootField, CONTROL_OFFSET);
		data.left = new FormAttachment(0, CONTROL_OFFSET);
		data.right = new FormAttachment(100, -CONTROL_OFFSET);
		pathField.setLayoutData(data);
		
		Label separatorLabel = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		data = new FormData();
		data.top = new FormAttachment(pathField, (int) (1.5 * CONTROL_OFFSET));
		data.left = new FormAttachment(0, -CONTROL_OFFSET);
		data.right = new FormAttachment(100, CONTROL_OFFSET);
		separatorLabel.setLayoutData(data);

		
	
		composite.setTabList(new Control[] { pathField, buttonComposite });
		return composite;
	}

	@Override
	protected void okPressed() {
		pathText = getPathText();
		super.okPressed();
	}

	public String getPathText() {
		return pathField != null && !pathField.isDisposed() ? pathField.getText() : pathText;
	}

	public IPath getFilePath() {
		return new Path(getPathText());
	}
}