package org.eclipse.rap.incubator.texteditor.demo;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.rap.incubator.texteditor.demo.internal.Activator;

public class TextEditorPreferenceInitializer extends AbstractPreferenceInitializer {

	public TextEditorPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(ITextEditorDemoConfiguration.WORKSPACE_SETTING, ITextEditorDemoConfiguration.WORKSPACE_DEFAULT_PATH);
	}
}
