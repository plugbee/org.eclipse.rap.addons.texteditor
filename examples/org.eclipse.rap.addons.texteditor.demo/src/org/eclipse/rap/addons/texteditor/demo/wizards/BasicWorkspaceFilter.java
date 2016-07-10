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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

@SuppressWarnings("serial")
public class BasicWorkspaceFilter extends ViewerFilter {

	public BasicWorkspaceFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof File) {
			String fileName = ((File)element).getName();
			if (fileName.startsWith(".")) {
				return false;
			}
		}
		return true;
	}
}