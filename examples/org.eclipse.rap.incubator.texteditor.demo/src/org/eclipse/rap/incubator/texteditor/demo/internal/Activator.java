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
package org.eclipse.rap.incubator.texteditor.demo.internal;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.rap.incubator.texteditor.demo"; //$NON-NLS-1$
	
	private static BundleContext bundleContext;
	  
	// The shared instance
	private static Activator plugin;

	public static Activator getInstance() {
		return plugin;
	}

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	    bundleContext = context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);		
	    bundleContext = context;
	}
	
	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String relativeURL) {
		URL entry = plugin.getBundle().getEntry(relativeURL);
		if (entry != null) {
			return ImageDescriptor.createFromURL(entry);
		}
		return null;
	} 
	

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		addImageFilePath(TextEditorDemoImageProvider.USER);
		addImageFilePath(TextEditorDemoImageProvider.FILE);
		addImageFilePath(TextEditorDemoImageProvider.MODEL);
		addImageFilePath(TextEditorDemoImageProvider.FOLDER);
		addImageFilePath(TextEditorDemoImageProvider.PROJECT);
		addImageFilePath(TextEditorDemoImageProvider.UNKNOWN);
		addImageFilePath(TextEditorDemoImageProvider.DELETE_RESOURCE);
		addImageFilePath(TextEditorDemoImageProvider.RUN_EXEC);
		addImageFilePath(TextEditorDemoImageProvider.COMPILE);
		addImageFilePath(TextEditorDemoImageProvider.JAVA);
		addImageFilePath(TextEditorDemoImageProvider.BINARY);		
	}

	private void addImageFilePath(String relativeURL) {
		Image image = plugin.getImageRegistry().get(relativeURL);
		if (image == null) {
			URL imageURL = plugin.getBundle().getEntry(relativeURL);
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(imageURL);
			image = descriptor.createImage();
			plugin.getImageRegistry().put(relativeURL, image);
		}
	}
}