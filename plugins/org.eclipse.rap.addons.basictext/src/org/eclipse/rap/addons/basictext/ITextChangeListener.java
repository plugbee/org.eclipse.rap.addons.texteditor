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
package org.eclipse.rap.addons.basictext;

import org.eclipse.swt.internal.SWTEventListener;

@SuppressWarnings("restriction")
public interface ITextChangeListener extends SWTEventListener {

	/**
	 * Handle a text changed event
	 * 
	 * @param event
	 *            the text changed event
	 */
	void handleTextChanged(TextChangedEvent event);
}
