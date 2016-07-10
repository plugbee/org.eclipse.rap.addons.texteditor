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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.rap.addons.texteditor.demo.internal.Activator;
import org.eclipse.rap.addons.texteditor.demo.internal.TextEditorDemoImageProvider;
import org.eclipse.swt.graphics.Image;

public class FileSystemLabelProvider implements ILabelProvider {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public Image getImage(Object element){
		if (element instanceof File){
			File file = (File) element;
			if (file.isDirectory()) {
				return Activator.getImageDescriptor(TextEditorDemoImageProvider.PROJECT).createImage();
			}
			String fileName = file.getName();
			int i = fileName.lastIndexOf('.');
			if (i > 0) {
			    String extension = fileName.substring(i+1);
			    if (extension.equals("java")) {
			    	return Activator.getImageDescriptor(TextEditorDemoImageProvider.JAVA).createImage();
			    } else if(extension.equals("class")) {
			    	return Activator.getImageDescriptor(TextEditorDemoImageProvider.BINARY).createImage();	
			    } else if (extension.equals("txt")) {
			    	return Activator.getImageDescriptor(TextEditorDemoImageProvider.FILE).createImage();	 	
			    }
			}
			return Activator.getImageDescriptor(TextEditorDemoImageProvider.MODEL).createImage();
		}
		return Activator.getImageDescriptor(TextEditorDemoImageProvider.UNKNOWN).createImage();
	}

	@Override
	public String getText(Object element){
		String label = null;
		if (element instanceof File){
			File file = (File) element;
			label =  file.getName();
			if (file.isDirectory())
				return label;
			//ignore files in src-gen folder
			if (file.getParent().contains("\\src-gen"))
				return label;
			return label;
		}
		return "unknown element";
	}

	@Override
	public void addListener(ILabelProviderListener listener){
		// NOP
	}

	@Override
	public void dispose(){
		// NOP
	}

	@Override
	public boolean isLabelProperty(Object element, String property){
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener){
		// NOP
	}	
}