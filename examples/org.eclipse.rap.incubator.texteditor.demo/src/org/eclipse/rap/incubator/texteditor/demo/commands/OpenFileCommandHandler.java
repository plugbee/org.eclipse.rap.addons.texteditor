package org.eclipse.rap.incubator.texteditor.demo.commands;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.rap.incubator.texteditor.demo.ITextEditorDemoConfiguration;
import org.eclipse.rap.incubator.texteditor.demo.internal.Activator;
import org.eclipse.rap.incubator.texteditor.demo.wizards.OpenFileWizard;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class OpenFileCommandHandler extends AbstractHandler {

	public OpenFileCommandHandler() {
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell activeShell = Display.getDefault().getActiveShell();
		File file = CommandUtil.unwrap(event, File.class);
		if (file == null) {
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String path = store.getDefaultString(ITextEditorDemoConfiguration.WORKSPACE_SETTING);
			file = new Path(path).toFile();
		}
		OpenFileWizard wizard = new OpenFileWizard(file);
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
}