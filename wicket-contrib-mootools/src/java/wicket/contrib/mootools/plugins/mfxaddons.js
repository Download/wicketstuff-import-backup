// author: Victor Igumnov
var Logger = new Class({
    initialize: function() {
	this.setDebug = false;
	this.setInfo = true;
	this.setWarn = true;
	this.setTrace = false;
	this.logBox = $('log');
	if(this.logBox == null) {
	    //alert('No DOM element named "log" found, logger will not work.');
	} else {
	    this.logBox.setStyle('font-size','12px');
	    this.logBox.setStyle('color','#a3a3a3');
	    this.logBox.setStyle('font-family','arial');
	}
    }
});
Logger.implement({
    log: function(txt) {
	if(this.logBox != null) {
	    var current = this.logBox.innerHTML;
	    this.logBox.setHTML(current + '<br/>' + txt);
	}
    },
    info: function(txt) {
	if(this.setInfo == true) {
	    this.log(txt);
	}
    },
    warn: function(txt) {
	if(this.setWarn == true) {
	    this.log(txt);
	}
    },
    trace: function(txt) {
	if(this.setTrace == true) {
	    this.log(txt);
	}
    },
    debug: function(txt) {
	if(this.setDebug == true) {
	    this.log(txt);
	}
    }
});


var BoxFactory = new Class({
    initialize: function(picture,size, callback ) {
    	this.initialize(picture,size,callback, null);
    },
    initialize: function(picture,size, callback, logger) {
	this.picture = picture;
	this.callbackUrl = callback;
	this.boxes = new Array(size);
	this.count = 0;
	this.open = false;

	if(logger == null) {
	    this.logger = new Logger();
	} else {
	    this.logger = logger; 
	}

	this.logger.debug('init boxFactory with ' + this.count);
	this.attachShowIn(this.picture.getParent());
	this.attachShowOut(this.picture.getParent());

    }
});
BoxFactory.implement({
    showBoxes: function(fac) {
	for(var i = 0;i <= fac.count; i = i + 1) {
	    var el = fac.boxes[i];
	    try {
		el[0].setStyle('display','block');
	    } catch(err) {}
	}
	fac.logger.trace('called showBoxes');
    },
    hideBoxes: function(fac) {
	for(var i = 0 ; i <= fac.count; i = i + 1) {
	    var el = fac.boxes[i];
	    try {
		el[0].setStyle('display','none');
	    } catch(err) { }
	}
	fac.logger.trace('called hideBoxes');
    },
    attachShowIn: function(el) {
	var fac = this;
	el.addEvent('mouseover', function() {
	    fac.showBoxes(fac);
	});
    },
    attachShowOut: function(el) {
	var fac = this;
	el.addEvent('mouseout', function() {
	    fac.hideBoxes(fac);
	});
    },
    loadBox: function(x,y,txt) {
	this.count = this.count + 1;
	this.logger.debug('upgraded counter '+this.count);

	var factory = new DOMFactory(this.logger);
	var staticBox = factory.getDisplayBox(x,y,txt);
	var info = factory.getDisplayText(x, y+staticBox.getStyle('height').toInt()+10, txt);
	info.setStyle('display','none');
	info.inject(document.body);
	
	staticBox.addEvent('mouseover', function() { 
		info.setStyle('display','block');
	});
	staticBox.addEvent('mouseout', function() {
		info.setStyle('display','none');
	});
	staticBox.inject(this.picture.getParent());

	tagElement = new Array(3);
	tagElement[0] = staticBox;
	tagElement[0].setStyle('display','none');
	this.boxes[this.count] = tagElement;
    },
    saveAction: function(tagElement) {
	    this.count = this.count + 1;
	    this.logger.debug('upgraded counter '+this.count);

	    // potentially expensive?
	    var factory = new DOMFactory(this.logger);
	    var staticBox = factory.getDisplayBox(tagElement[0].getPosition().x,tagElement[0].getPosition().y);

	    this.logger.debug('setting ' + tagElement[0].getPosition().x + ' and ' + tagElement[0].getPosition().y);

	    var savedText= tagElement[1].getValue();
	    var xCord = tagElement[0].getPosition().x;
	    var yCord = tagElement[0].getPosition().y;

	    var info = factory.getDisplayText(xCord,factory.getOffsetY(tagElement[0],10), savedText);
	    info.setStyle('display','none');
	    info.inject(document.body); 
	    staticBox.addEvent('mouseover', function() { 
		info.setStyle('display','block');
	    });
	    staticBox.addEvent('mouseout', function() {
		info.setStyle('display','none');
	    });


	    staticBox.inject(this.picture.getParent());
	    this.picture.getParent().removeChild(tagElement[0]);
	    tagElement[0] = staticBox;
	    tagElement[0].setStyle('display','none');
	    tagElement[1].setStyle('display','none');
	    tagElement[2].setStyle('display','none');
	    tagElement[3].setStyle('display','none');
	    this.logger.debug('saving as '+this.count);
	    this.open=false;
	    this.boxes[this.count] = tagElement; 
	    
	    // wicket callback...
	    

	    var getData = "&callback="+savedText+"&x="+xCord+"&y="+yCord;
		var response = this.callbackUrl+getData;
		wicketAjaxGet(response, function() { }.bind(this), function() { }.bind(this));
    },
    cancelAction: function(tagElement) {
	    this.logger.debug('canceling action ');
	    tagElement[0].getParent().removeChild(tagElement[0]);
	    tagElement[1].getParent().removeChild(tagElement[1]);
	    tagElement[2].getParent().removeChild(tagElement[2]);
	    tagElement[3].getParent().removeChild(tagElement[3]);
	    this.open=false;
    },
    showTagBox: function () {
	if(this.open == true) {
	    return;
	}

	var frame = this.picture.getParent();
	var tagElement = new Array(3);
	factory = new DOMFactory(this.logger);
	var boxes = this.boxes;
	this.open = true;


	var boxArea = factory.getDisplayBox(this.picture.getPosition().x+30,this.picture.getPosition().y+30);
	tagElement[0]=boxArea;
	this.attachShowIn(boxArea);
	this.attachShowOut(boxArea);
	boxArea.inject(frame);

	var textArea = factory.getTextArea(boxArea.getPosition().x, factory.getOffsetY(boxArea,10) ); 
	tagElement[1] = textArea;
	this.attachShowIn(textArea);
	this.attachShowOut(textArea);
	textArea.inject(frame);

	var fac = this;
	var saveBtn = factory.getButton(boxArea.getPosition().x, factory.getOffsetY(textArea,20) ,'Save', function() { fac.saveAction(tagElement) });
	tagElement[2]=saveBtn;
	this.attachShowIn(saveBtn);
	this.attachShowOut(saveBtn);
	saveBtn.inject(frame);

	var cancelBtn = factory.getButton(boxArea.getPosition().x+60, factory.getOffsetY(textArea,20) ,'Cancel', function() { fac.cancelAction(tagElement) });
	tagElement[3]=cancelBtn;
	this.attachShowIn(cancelBtn);
	this.attachShowOut(cancelBtn);
	cancelBtn.inject(frame);

	    new Drag.Move(boxArea, {container: frame, onDrag: function () {
		textArea.setStyle('left',boxArea.getPosition().x);
		textArea.setStyle('top', factory.getOffsetY(boxArea,10));

		saveBtn.setStyle('left',boxArea.getPosition().x);
		saveBtn.setStyle('top',factory.getOffsetY(textArea,20) );

		cancelBtn.setStyle('left',boxArea.getPosition().x+60);
		cancelBtn.setStyle('top', factory.getOffsetY(textArea,20) );

	    }
	}); 
    }
});


var DOMFactory = new Class({
    initialize: function() {
	this.initialize(null);
    },
    initialize: function(logger) {
	if(logger != null) {
	    this.logger=logger;
	} else {
	    this.logger = new Logger();
	}
    }
});
DOMFactory.implement({
    getDisplayText : function(x,y,txt) {
	this.logger.debug('Returning DisplayText');

	var displayText = new Element('div');
	displayText.setStyle('background','#FFF6BF');
	displayText.setStyle('float','left');
	displayText.setStyle('position','absolute');
	displayText.setStyle('padding','5px');
	displayText.setStyle('font-family','arial');
	displayText.setStyle('font-size','12px');

	displayText.setStyle('left',x);
	displayText.setStyle('top',y);

	displayText.setHTML(txt);

	return displayText;
    },
    getDisplayBox: function (x,y) {
	return this.getDisplayBox(x,y,'none');
    },
    getDisplayBox : function (x,y,clazzName) {
	this.logger.debug('Creating new Display Box.');
	var boxArea = new Element('div');
	var boxArea2 = new Element('div');

	boxArea.setStyle('border','1px solid');
	boxArea.setStyle('height','50px');
	boxArea.setStyle('width','50px');
	boxArea.setStyle('position','absolute');
	boxArea.setStyle('cursor','pointer');
	boxArea.setAttribute('class',clazzName);
	boxArea.setStyle('background','url(inv.gif)');
	//boxArea.setStyle('background','#aaa');
	//boxArea.setStyle('opacity','0.4');

	boxArea2.setStyle('border','1px #fff solid');
	boxArea2.setStyle('height','50px');
	boxArea2.setStyle('width','50px');
	boxArea2.setStyle('position','absolute');
	boxArea2.setStyle('cursor','pointer');
	boxArea2.setAttribute('class',clazzName+'inner');

	boxArea2.inject(boxArea);
	
	boxArea.setStyle('left',x);
	boxArea.setStyle('top',y);

	return boxArea;
    },
    getTextArea: function(x,y) {
	this.logger.debug('Creating new TextArea');
	var textArea = new Element('textarea');
	textArea.setStyle('border','1px #a3a3a3 solid');
	textArea.setStyle('position','absolute');
	textArea.setAttribute('id','thetext');
	textArea.setStyle('background','#FFF6BF');
	textArea.setStyle('padding','5px');
	textArea.setStyle('width','180px !important');
	textArea.setStyle('height','60px !important');
	textArea.setStyle('font-size','12px');
	textArea.setStyle('font-family','arial');
	textArea.innerHTML="Add your note here.";
	textArea.setStyle('left',x);
	textArea.setStyle('top',y);
	return textArea;
    },
    getButton: function(x,y,txt,cl) {
	this.logger.debug('Creating new input button');
	var btn = new Element('input');
	btn.setStyle('position','absolute');
	btn.setStyle('border','1px #a3a3a3 solid');
	btn.setAttribute('type','button');
	btn.setAttribute('value',txt);
	btn.setStyle('left',x);
	btn.setStyle('top',y);
	btn.addEvent('click', cl);
	return btn;
    },
    getOffsetY: function(el,y)  {
	var offset = el.getStyle('height').toInt()+y;
	return el.getPosition().y + offset;
    },
    getOffsetX: function(el,x) {
	var offset = el.getStyle('width').toInt()+x;
	return el.getPosition().x + offset;
    }
});

