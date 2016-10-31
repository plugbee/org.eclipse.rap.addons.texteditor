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
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
//minify using as YUI Compressor, Google Closure Compiler, or JSMin. 
(function() {
	rap.registerTypeHandler("org.eclipse.rap.addons.texteditor.json.widget.Json", {
		factory : function(properties) {
			return new org.eclipse.rap.addons.texteditor.json.widget.Json(properties);
		},	
		destructor : "destroy",	 
		properties : [ "url", "text", "editable", "status", "annotations", "scope", "proposals", "font", "dirty", "markers", "background"],
		events : ["Modify", "TextChanged", "Save", "FocusIn", "FocusOut", "Selection", "CaretEvent", "ContentAssist"],
		methods : ["addMarker", "insertText", "removeText", "setProposals"]
 		
	});

	rwt.qx.Class.define("org.eclipse.rap.addons.texteditor.json.widget.Json", {
		extend :org.eclipse.rap.addons.BasicText,

		construct : function(properties) {
			this.base(arguments, properties);
		},
		members : {
						
			createEditor : function() {
				var basePath = 'rwt-resources/src-js/org/eclipse/rap/addons/basictext/ace';
				ace.require("ace/config").set("basePath", basePath);
				var workerPath = 'rwt-resources/src-js/org/eclipse/rap/addons/texteditor/json/ace';
				ace.require("ace/config").set("workerPath", workerPath);
				var themePath = 'rwt-resources/src-js/org/eclipse/rap/addons/texteditor/json/ace';

				ace.require("ace/config").set("themePath", themePath);
				var editor = this.editor = ace.edit(this.element);
				var editable = this.editable;
				var self = this;
				if (editor != null) {
					//Set the Id of this editor
					var guid = this.url;
					
					//Set language mode
					editor.getSession().setMode("ace/mode/json");	

					//Set theme
					editor.setTheme("ace/theme/basictext");	

					//Default settings
					editor.getSession().setUseWrapMode(true);
				    editor.getSession().setTabSize(4);
				    editor.getSession().setUseSoftTabs(true);
					editor.getSession().getUndoManager().reset();
					editor.setShowPrintMargin(false);
					editor.setBehavioursEnabled(true);
					editor.setWrapBehavioursEnabled(true);
					editor.$blockScrolling = Infinity;
					this.setFont("10px");
					
					//Configure content assist feature
					this.langTools = ace.require("ace/ext/language_tools");
					this.editor.setOptions({
					    enableBasicAutocompletion: true
					});

					//Add text hover
					var TokenTooltip = ace.require("ace/ext/tooltip").TokenTooltip;	
					editor.tokenTooltip = new TokenTooltip(editor);		 	
				 	
					//Handle the global index
				 	if (this.useSharedWorker) {
						if (typeof SharedWorker == 'undefined') {	
							alert("Your browser does not support JavaScript shared workers.");
						} else {
							//Compute worker's http URL
							var filePath = 'rwt-resources/src-js/org/eclipse/rap/addons/basictext/global-index.js';
							var httpURL = computeWorkerPath(filePath);
							var worker = this.worker = new SharedWorker(httpURL);		
							editor.on("change", function(event) {
								worker.port.postMessage({
									message: editor.getValue(), 
							        guid: guid, 
							        index: index
							    });
						    });
							worker.port.onmessage = function(e) {
							 	//update the index reference
							 	index = e.data.index;
						    };	
						}	
				 	} 

				 	//On focus get event
					editor.on("focus", function() {
				 		self.onFocus();
				 	});
					
					//On focus lost event
				 	editor.on("blur", function() {
				 		self.onBlur();
				 	});
				 	
				 	//On input event
				 	editor.on("input", function() {
						if (!editor.getSession().getUndoManager().isClean())
							self.onModify();
				 	});
				 	
				 	//On mouse down event
				 	editor.on("mousedown", function() { 
				 	    // Store the Row/column values 
				 	}) 
				 	
				 	//On cursor move event
				 	editor.getSession().getSelection().on('changeCursor', function() { 
				 	    if (editor.$mouseHandler.isMousePressed)  {
				 	      // the cursor changed using the mouse
				 	    }
				 	    // the cursor changed
				 	    self.onChangeCursor();
				 	});
				 	editor.getSession().on('changeCursor', function() { 
				 	    if (editor.$mouseHandler.isMousePressed)  {
				 	      // remove last stored values 
				 	    }
				 	    // Store the Row/column values 
				 	}); 

				 	//On text change event
					editor.on("change", function(event) {					        
						//customize					
			        });
					
					//Bind keyboard shorcuts
					editor.commands.addCommand({
						name: 'saveFile',
						bindKey: {
						win: 'Ctrl-S',
						mac: 'Command-S',
						sender: 'editor|cli'
						},
						exec: function(env, args, request) {
							self.onSave();
						}
					});
					
					//Done
			        this.onReady();
				}
			},

			destroy : function() {
				this.base(arguments);
			},
			
		}
	});
	
	var computeWorkerPath = function (path) {
        path = path.replace(/^[a-z]+:\/\/[^\/]+/, "");
        path = location.protocol + "//" + location.host
            + (path.charAt(0) == "/" ? "" : location.pathname.replace(/\/[^\/]*$/, ""))
            + "/" + path.replace(/^[\/]+/, "");
        return path;
    };
    
	var typeToIcon = function(type) {
		var cls = "ace-";
		var suffix;
		if (type == "?") suffix = "unknown";
		else if (type == "keyword") suffix = type;
		else if (type == "identifier") suffix = type;
		else if (type == "snippet") suffix = "snippet";
		else if (type == "number" || type == "string" || type == "bool") suffix = type;
		else if (/^fn\(/.test(type)) suffix = "fn";
		else if (/^\[/.test(type)) suffix = "array";
		else suffix = "object";
		return cls + "completion " + cls + "completion-" + suffix;
	};
    
}());
