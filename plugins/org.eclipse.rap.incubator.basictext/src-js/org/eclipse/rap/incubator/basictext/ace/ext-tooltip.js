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
 *     PlugBee - Initial API and implementation
 *
 * </copyright>
 */
ace.define('ace/ext/tooltip', ['require', 'exports', 'module' , 'ace/lib/dom', 'ace/lib/event', 'ace/range', 'ace/lib/lang'], function(require, exports, module) {

"use strict";

var dom = require("ace/lib/dom");
var oop = require("ace/lib/oop");
var event = require("ace/lib/event");
var lang = require("ace/lib/lang")
var Range = require("ace/range").Range;
var Tooltip = require("ace/tooltip").Tooltip;

function TokenTooltip (editor) {
    if (editor.tokenTooltip)
        return;
    Tooltip.call(this, editor.container);
    editor.tokenTooltip = this;
    this.editor = editor;
    this.update = this.update.bind(this);
    this.onMouseMove = this.onMouseMove.bind(this);
    this.onMouseOut = this.onMouseOut.bind(this);
    event.addListener(editor.renderer.scroller, "mousemove", this.onMouseMove);
    event.addListener(editor.renderer.content, "mouseout", this.onMouseOut);
}

function getDocumentation(token, before) {
	var doc = "Documentation";
	doc+="\n\n" + token.value + " : " + "[" + token.type + "]";
	return doc;
}

oop.inherits(TokenTooltip, Tooltip);

(function(){
    this.token = {};
    this.range = new Range();
 
    this.update = function() {
        this.$timer = null;
        
        var r = this.editor.renderer;
        if (this.lastT - (r.timeStamp || 0) > 1000) {
            r.rect = null;
            r.timeStamp = this.lastT;
            this.maxHeight = window.innerHeight;
            this.maxWidth = window.innerWidth;
        }

        var canvasPos = r.rect || (r.rect = r.scroller.getBoundingClientRect());
        var offset = (this.x + r.scrollLeft - canvasPos.left - r.$padding) / r.characterWidth;
        var row = Math.floor((this.y + r.scrollTop - canvasPos.top) / r.lineHeight);
        var col = Math.round(offset);

        var screenPos = {row: row, column: col, side: offset - col > 0 ? 1 : -1};
        var session = this.editor.session;
        var docPos = session.screenToDocumentPosition(screenPos.row, screenPos.column);
        
        if (screenPos.column-docPos.column>5) {
            session.removeMarker(this.marker);
            this.hide();
            return;
		}
        
        var tokens = session.getTokens(docPos.row);        
        var token = session.getTokenAt(docPos.row, docPos.column);
        var before =null;
		for (var i=0; i < tokens.length; i++) {
			if (tokens[i].value==token.value) {
				if (i>=1)
					before = tokens[i-1];
				if (i>=2 && before.value==" ")
					before = tokens[i-2];
				break;
		   }     
	    }
        
		if (before==null) {
            session.removeMarker(this.marker);
            this.hide();
            return;
		}
        if (!token && !session.getLine(docPos.row)) {
            token = {
                type: "",
                value: "",
                state: session.bgTokenizer.getState(0)
            };
        }
        if (!token) {
            session.removeMarker(this.marker);
            this.hide();
            return;
        }

        var tokenText = token.type;
 
        //paren.lparen
        //paren.rparen
        //text
        //string
        //identifier
        //comment
        //comment.doc
        //support.type
        //punctuation.operator
        if (tokenText=="identifier") {
        	tokenText = getDocumentation(token, before);
        	if (tokenText=="") {
                session.removeMarker(this.marker);
                this.hide();
                return;
        	}
            if (this.tokenText != tokenText) {
                this.setText(tokenText);
                this.tokenText = tokenText;
                this.width = this.getWidth();
                this.height = this.getHeight();
            }
            this.show(null, this.x, this.y);           
            this.token = token;
            session.removeMarker(this.marker);
            this.range = new Range(docPos.row, token.start, docPos.row, token.start + token.value.length);
            this.marker = session.addMarker(this.range, "ace_bracket", "text");
            
        } else {
            this.hide();
            session.removeMarker(this.marker);
            return;	
        }
    };
    
    this.onMouseMove = function(e) {
        this.x = e.clientX;
        this.y = e.clientY;
        if (this.isOpen) {
            this.lastT = e.timeStamp;
            this.setPosition(this.x, this.y);
        }
        if (!this.$timer)
            this.$timer = setTimeout(this.update, 500);
    };

    this.onMouseOut = function(e) {
        if (e && e.currentTarget.contains(e.relatedTarget))
            return;
        this.hide();
        this.editor.session.removeMarker(this.marker);
        this.$timer = clearTimeout(this.$timer);
    };

    this.setPosition = function(x, y) {
        if (x + 10 + this.width > this.maxWidth)
            x = window.innerWidth - this.width - 10;
        if (y > window.innerHeight * 0.75 || y + 20 + this.height > this.maxHeight)
            y = y - this.height - 30;
        Tooltip.prototype.setPosition.call(this, x + 10, y + 20);
    };

    this.destroy = function() {
        this.onMouseOut();
        event.removeListener(this.editor.renderer.scroller, "mousemove", this.onMouseMove);
        event.removeListener(this.editor.renderer.content, "mouseout", this.onMouseOut);
        delete this.editor.tokenTooltip;
    };

}).call(TokenTooltip.prototype);
exports.TokenTooltip = TokenTooltip;

});
