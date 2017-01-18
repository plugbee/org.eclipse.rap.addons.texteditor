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
 *     Amine Lajmi - Initial API and implementation
 *
 * </copyright>
 */
//minify using as YUI Compressor, Google Closure Compiler, or JSMin. 
(function() {
	rap.registerTypeHandler("org.eclipse.rap.incubator.BasicText", {
		factory : function(properties) {
			return new org.eclipse.rap.incubator.BasicText(properties);
		},
		destructor : "destroy",	 
		properties : [ "url", "text", "editable", "status", "annotations", "scope", "proposals", "font", "dirty", "markers", "background"],
		events : ["Modify", "TextChanged", "Save", "FocusIn", "FocusOut", "Selection", "CaretEvent", "ContentAssist"],
		methods : ["setSelection", "addMarker", "removeMarker", "clearMarkers", "insertText", "removeText", "setProposals", "moveCursorFileStart","moveCursorFileEnd"]
	});

	rwt.qx.Class.define("org.eclipse.rap.incubator.BasicText", {
		extend : rwt.widgets.Composite,
		construct : function(properties) {
			this.base(arguments);
			bindAll(this, [ "layout", "onReady", "onSend", "onRender" ]);
			this.parent = rap.getObject(properties.parent);
			var area = this.parent.getClientArea();			
			this.element = document.createElement("pre");
			this.element.id = "editor";
			this.element.style.position = "absolute";
			this.element.style.left = area[0] + "px";
			this.element.style.top = area[1] + "px";
			this.element.style.width = area[2] + 'px';
			this.element.style.height = area[3] + 'px';
			this.parent.append(this.element);
			this.flush();
			this.parent.addListener("Resize", this.layout);	
			rap.on( "render", this.onRender);
		},

		destruct : function() {
			rap.off("send", this.onSend);
			this.editor.destroy();
			this.element.parentNode.removeChild(this.element);
		},
		
		members : {
			ready: false,
			editor: null,
			editable: true,
			selection: null,
			isFocused: false,
			initialContent: true,
			langTools: null,
			annotations: [],
			markers: [],
			scope: [],
			proposals: [],
			completers: null,
			backendCompleter: null,
			useSharedWorker: true,
			
			onReady : function() {
				this.ready = true;
				this.layout();				
				if (this.url) {
					this.setUrl(this.url);
					delete this.url;
				}
				if (this.editable) {
					this.setEditable(this.editable);
					delete this.editable;
				}
				if (this.text) {
					this.editor.setValue(this.text);
					this.editor.clearSelection(); 
					this.editor.getSelection().moveCursorFileStart();
					delete this.text;
				}
				if (this.font) {
					this.setFont(this.font);
					delete this.font;
				}
				if (this.url) {
					this.setUrl(this.url);
					delete this.url;
				}
				if (this.selection) {
					this.setSelection(this.selection);
					delete this.selection;
				}
				if (this.annotations) {
					this.setAnnotations(this.annotations);
					delete this.annotations;
				}
				if (this.markers) {
					this.setMarkers(this.markers);
					delete this.markers;
				}
				if (this.backgroundColor) {
					this.setBackground(this.backgroundColor);
					delete this.backgroundColor;
				}
				if (this.scope) {
					this.setScope(this.scope);
					delete this.scope;
				}
				if (this.proposals) {
					this.setProposals(this.proposals);
					delete this.proposals;
				}
			},

			destroy : function() {
				rap.off("send", this.onSend);
				this.editor.destroy();
				this.element.parentNode.removeChild(this.element);
			},
			
			onAppear: function() {},

			onFocus: function() {
				this.isFocused = true;
				var remoteObject = rap.getRemoteObject(this);
				if (remoteObject) {
					remoteObject.notify("FocusIn", { value : this.editor.getCursorPosition()});
					this.onChangeCursor();
				}
			},
			
			onBlur: function() {
				this.isFocused = false;
				var remoteObject = rap.getRemoteObject(this);
				if (remoteObject) {
					remoteObject.notify("FocusOut", { value : this.editor.getValue()});					
					var range = this.editor.getSelection().getRange();
					if (range.start.row != range.end.row || range.start.column !=range.end.column) {
						remoteObject.notify("Selection", {
							value: this.editor.getSession().doc.getTextRange(range),
							rowStart: range.start.row, 
							rowEnd: range.end.row,
							columnStart: range.start.column,
							columnEnd: range.end.column 
						});
					}
				}
				this.proposals= [];
			},

			onRender : function() {
				if (this.element.parentNode) {
					rap.off("render", this.onRender);
					this.createEditor();
					this.editor.on("instanceReady", this.onReady);
					rap.on("send", this.onSend);
				}
			},

			onSend : function() {},
			
			onChangeCursor: function() {
				var remoteObject = rap.getRemoteObject(this);
				if (remoteObject) {
					remoteObject.notify("CaretEvent", { value : this.editor.getCursorPosition()});
				}				
			},

			onCompletionRequest : function(pos, prefix, callback) {
				if (this.isFocused) {
					var remoteObject = rap.getRemoteObject(this);
					if (remoteObject) {
						remoteObject.call("getProposals", { value : this.editor.getValue(), pos : pos, prefix : prefix});
					}	
					var proposals = this.proposals;		
			        var wordList = Object.keys(proposals);
			        var self = this;
			        callback(null, wordList.map(function(word) {
			            return {
			            	iconClass: " " + self.typeToIcon(proposals[word].type),
			                name: word,
			                value: proposals[word].replacement,
			                score: 1,
			                meta: "[" + proposals[word].type + "]"
			            };
			        }));	
				}
			},
			
			onModify : function() {
				var remoteObject = rap.getRemoteObject(this);
				if (remoteObject && !this.initialContent) {
					remoteObject.notify("TextChanged", { value : this.editor.getValue()});
				} else {
					if (this.editable) {
						//initial setting, avoid notify back the server.
						this.initialContent=false;
						this.editor.getSession().getUndoManager().reset();		
					}
				}	
			},
			
			getOffset: function(properties) {
				var row = properties.row;
				var column = properties.column;
				var offset = 0;
				for (i=0;i<row;i++) {
					offset+= this.editor.getSession().getLine(i).length;
				}
				return offset + column - 1;
			},

			onSave: function() {
				var remoteObject = rap.getRemoteObject(this);
				if (remoteObject && this.editable) {
					remoteObject.set("text",this.editor.getValue());
					remoteObject.notify("Save", { value : this.editor.getValue()});
				}
			},

			setUrl : function(url) {
				this.url = url;
			},	
			
			setText : function(text) {
				if (this.ready) {
					this.editor.setValue(text);	
					this.editor.clearSelection(); 
					this.editor.getSelection().moveCursorFileStart();
				}
				else {
			        this.text = text;
			    }
			},
		
			setEditable : function(editable) {
			   	if (this.ready) {
			   		this.editable = editable;
			   		this.editor.setReadOnly(!editable);
					this.editable = editable;
				}
			},
			
			setSelection : function(selection) {
				if (this.ready) {
					var Range = ace.require("ace/range").Range;		
					var range = new Range(selection.rowStart, selection.rowEnd, selection.columnStart, selection.columnEnd);
					this.editor.getSelection().setSelectionRange(range);
				}
				else {
			        this.selection = selection;
			    }
			},
			
			setStatus : function(status) {
			   	if (this.ready) {
			   		if (this.status=="invalid") {
			   			var annotations = this.editor.session.getAnnotations();
			   			var filtered = [];
			   			this.editor.session.clearAnnotations();
			   			for (var i = 0; i < annotations.length; i++) {
			   				var annotation = annotations[i];
			   				var type = annotation.type;
			   				if (type != "warning") {
			   					filtered.push(annotation)
			   				}
			   			}
			   			this.editor.session.setAnnotations(filtered);
			   		}
				} else {
					this.status = status;
				}
			},

			setAnnotations : function(newAnnotations) {
				if (this.ready) {
					//remove old server annotations
					var annotations = this.editor.session.getAnnotations();
					for (var i = annotations.length; i--;) {
						annotations.pop(annotations[i]);
					}
					//add new server annotations
					if (newAnnotations.length>0) {	
						for (var i = newAnnotations.length; i--;) {
							var annotation = newAnnotations[i];
							for (var key in annotation) {
								var positions = annotation[key].match(/\d+/g);
								if (key=="ERROR")
									annotations.push({row:Math.max(positions[0]-1,0) ,column: 0, text: annotation[key], type:"error", server: true});
								else if (key=="WARNING")
									annotations.push({row:Math.max(positions[0]-1,0) ,column: 0, text: annotation[key], type:"warning", server: true});
								else if (key=="INFO")
									annotations.push({row:Math.max(positions[0]-1,0) ,column: 0, text: annotation[key], type:"info", server: true});
							}
						}
						this.editor.session.setAnnotations(annotations);
					}
				} else {
					this.annotations = newAnnotations;
				}
			},

			setScope : function(scope) {
				if (this.ready) {
					this.scope = scope;	
			    	if(this.worker!=null) {
			        	this.worker.port.postMessage({
			            	message: this.editor.getValue(), 
			            	guid: this.url, 
			            	index: this.scope
			            });     
			    	}	
				} else {
					 this.scope = scope;	
				}
			},

			setProposals : function(proposals) {
				this.proposals = proposals;	
			},
			
			//'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', 'source-code-pro'
			//font = 14px Verdana, "Lucida Sans", Arial, Helvetica, sans-serif
			setFont : function(font) {
				if (this.ready) {
					this.editor.setOptions({fontFamily: "Menlo, monospace"});
					this.editor.setFontSize(16);
				}
				else {
			        this.font = font;
			    }
			},

			setBackground : function(color) {
				if (this.ready) {
			        this.backgroundColor = color;
					ace.require("ace/lib/dom").importCssString('.ace-eclipse {\
						    background-color: rgb('+color.R+', '+color.G+', '+color.B+');\
						}');
				}
				else {
			        this.backgroundColor = color;
			    }
			},
			
			setDirty : function(dirty) {
				if (this.ready) {
					if (!dirty && this.editable) {
						this.editor.getSession().getUndoManager().markClean();
					}
				}
			},

			clearMarkers : function(markers) {
				if (this.ready) {
					var markers = this.editor.getSession().getMarkers(false);					 
			        Object.keys(markers).forEach(function(key) {
			        	if (markers[key].clazz ==="ace_debug_line")
							this.editor.getSession().removeMarker(markers[key].id);
			        }, this);
				}
			},
			
			setMarkers : function(markers) {
				if (this.ready) {					
					this.markers = markers;
					for (var i = this.markers.length; i--;) {
						var marker = this.markers[i];
						var Range = ace.require("ace/range").Range;
						var range = new Range(marker.rowStart, marker.rowEnd, marker.columnStart, marker.columnEnd);
						this.editor.getSession().addMarker(range, "ace_debug_line", "line");
						ace.require("ace/lib/dom").importCssString('.ace_debug_line {\
						    background-color: aquamarine;\
						    position: absolute;\
						}');
		           }
				}
				else {
			        this.markers = markers;
			    }
			},
			
			addMarker : function(marker) {
				if(this.ready) {
					var Range = ace.require("ace/range").Range;
					var range = new Range(marker.rowStart,0,marker.columnStart,1);	
					this.editor.getSession().addMarker(range, "ace_debug_line", "fullLine");					
					ace.require("ace/lib/dom").importCssString('.ace_debug_line {\
					    background-color: aquamarine;\
					    position: absolute;\
					}');	
				}
			},
			
			removeMarker : function(marker) {
				if (this.ready) {
					var markers = this.editor.getSession().getMarkers(false);					 
			        Object.keys(markers).forEach(function(key) {
			        	if (markers[key].id === marker.id)
			        		this.editor.getSession().removeMarker(markers[key].id);
			        }, this);
				}
			},

			insertText : function(properties) {
				if (this.ready) {
					var position = { row:properties.rowStart, column:properties.columnStart};
					var text = properties.text;
					this.editor.getSession().insert(position, text); //where position is an object of the form {row:number, column:number}
				}
			},

			removeText : function(properties) {
				if (this.ready) {			        
					var range = this.editor.getSelectionRange();
					if (range.start.row != range.end.row || range.start.column !=range.end.column) {
			            this.editor.getSession().remove(range);
			            this.editor.clearSelection();
			        }
				}
			},
			
			moveCursorFileStart : function(properties) {
				if (this.ready) {		
					this.editor.getSelection().moveCursorFileStart();
				}
			},
			
			moveCursorFileEnd : function(properties) {
				if (this.ready) {		
					this.editor.getSelection().moveCursorFileEnd();
				}
			},
			
			createEditor : function() {
				var editor = this.editor = ace.edit(this.element);				
				var editable = this.editable;
				if (editor != null) {
					
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
					editor.setReadOnly(!editable);							
					editor.$blockScrolling = Infinity;				
					//Load content assist module
					this.langTools = ace.require("ace/ext/language_tools");
					this.editor.setOptions({
					    enableBasicAutocompletion: true
					});
					
					//Set the Id of this editor
					var guid = this.url;
					
					//Initialize the annotations
					if (this.annotations==null) 
						this.annotations=[];
					
					//Initialize the global index
					if (this.scope==null)
						this.scope=[];
					
					//Initialize the completion proposals
					if (this.proposals==null) 
						this.proposals=[];
					
					var self = this;
					this.backendCompleter = {	
						getCompletions: function(editor, session, pos, prefix, callback) {
							self.onCompletionRequest(pos, prefix, callback);	
						},
						getDocTooltip: function(item) {
						    item.docHTML = ["<b>", item.caption, "</b>", 
						                    "<hr></hr>", 
						                    item.meta.substring(1,item.meta.length-1)
						                    ].join("");
						}
					}
					//Add completer and enable content assist
					this.langTools.addCompleter(this.backendCompleter);
					
					//Add documentation hover
					var TokenTooltip = ace.require("ace/ext/tooltip").TokenTooltip;	
					editor.tokenTooltip = new TokenTooltip(editor);
				 	
				 	//Initialize the index
				 	index = this.scope;

				 	//Initialize the completion proposals
				 	proposals = this.proposals;
					
					if (this.useSharedWorker) {
						if (typeof SharedWorker == 'undefined') {	
							alert("Your browser does not support JavaScript Shared Workers.");
						} else {
							//Compute worker's http URL
							var filePath = 'rwt-resources/src-js/org/eclipse/rap/incubator/basictext/global-index.js';
							var httpURL = computeWorkerPath(filePath);
							var worker = this.worker = new SharedWorker(httpURL);		
							if (this.ready) {
								editor.on("change", function(event) {
									worker.port.postMessage({
										message: editor.getValue(), 
								        guid: guid, 
								        index: index
								    });
							    });
							}
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

				 	//On text change event
					editor.on("change", function(event) {					        
						//implement in subclasses						
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

			flush : function(inResponse) {
				if (inResponse) {
					rwt.remote.EventUtil.setSuspended(true);
					rwt.widgets.base.Widget.flushGlobalQueues();
					rwt.remote.EventUtil.setSuspended(false);
				} else {
					rwt.widgets.base.Widget.flushGlobalQueues();
				}	
			},

			layout : function() {
				if (this.ready) {
					var area = this.parent.getClientArea();
					this.element.style.left = area[0] + "px";
					this.element.style.top = (area[1]-10) + "px";
					this.element.style.width = area[2] + 'px';
					this.element.style.height = (area[3]-5) + 'px';
					this.editor.resize();
				}
			},
			
			computeWorkerPath : function (path) {
		        path = path.replace(/^[a-z]+:\/\/[^\/]+/, ""); // Remove domain name and rebuild path
		        path = location.protocol + "//" + location.host
		            + (path.charAt(0) == "/" ? "" : location.pathname.replace(/\/[^\/]*$/, ""))
		            + "/" + path.replace(/^[\/]+/, "");
		        return path;
		    },
		    
			typeToIcon : function(type) {
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
			}
		}
	});

	var bind = function(context, method) {
		return function() {
			return method.apply(context, arguments);
		};
	};

	var bindAll = function(context, methodNames) {
		for ( var i = 0; i < methodNames.length; i++) {
			var method = context[methodNames[i]];
			context[methodNames[i]] = bind(context, method);
		}
	};

	var async = function(context, func) {
		window.setTimeout(function() {
			func.apply(context);
		}, 0);
	};
    
}());