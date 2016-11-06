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
 * </copyright>
 */
package org.eclipse.rap.incubator.texteditor.javascript.widget;

import org.eclipse.rap.ui.resources.IResource;

public final class JavascriptResource implements IResource {

  public String getCharset() {
    return "UTF-8";
  }

  public ClassLoader getLoader() {
    return this.getClass().getClassLoader();
  }

  public String getLocation() { 	
    return "src-js/org/eclipse/rap/incubator/texteditor/javascript/Javascript.js";
  }
  
  public boolean isExternal() {
    return false;
  }

  public boolean isJSLibrary() {
    return true;
  }

  public boolean isAccessible() {
    return false;
  }
}
