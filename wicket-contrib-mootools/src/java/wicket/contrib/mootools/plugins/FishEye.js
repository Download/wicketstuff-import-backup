/***************************************************************
*  Fisheye Dock Menu Class
*  Chris Esler
*  12-13-2006
*
****************************************************************
*
*  Based on:
*  Mac OS X Dock script by John Pennypacker
*  http://www.pennypacker.net
*
***************************************************************/

var fisheyeClass = new Class({

	minZ : 32,
	maxZ : 96,
	range : 3,
	tX : 0,
	
	
	theDiv: null,
	incZ : null,
	IEeM : null,
	
	dock :  null,
	
	dockArray : [],
	spanEffects : [],
	imageEffects : [],
	
	setup: function(){
		this.incZ = (this.maxZ - this.minZ)/this.range;
		this.IEeM = document.all?true:false;
	},
	
	initialize: function(el){
	
		this.setup();
	
		this.dock = $(el);
		
		this.dock.setStyle('padding-top', (this.minZ-(this.minZ/4)) + "px");

		this.dockItems = this.dock.getElements('li');
		var myarray = this.dockItems;
		
		this.dockItems.each(function(el,i) {
		
			var dockFirst = el.getFirst();
			
			dockFirst.setHTML('').setStyles({
				'margin' : '0 -.5em',
				'position' : 'relative'
			});
			

			var itemName = dockFirst.getProperty('title');
			var itemImage = dockFirst.getProperty('alt');
			//var itemImage = itemName.replace(/\s+/g, "");
			//var itemImage = "images/" + itemImage.toLowerCase() + ".png";
			
			el.addEvent('mouseover', function() { this.desc(i); }.bind(this));
			el.addEvent('mouseout', function() { this.undesc(i); }.bind(this));
			
			if(this.IEeM) { dockFirst.setStyle('padding-top', 0); }

			//create the span
			var s = new Element('span').setProperties({
				'class':'desc',
				'id':'desc'+i
			}).setStyles({
				'visibility': 'hidden',
				'position':'absolute',
				'left': 0
			}).appendText(itemName).injectInside(dockFirst);
			
			this.spanEffects[i] = new Fx.Style($(s), 'opacity', {wait: false, duration:5000});
	
			//create the icon
			var a = new Element('img').setProperties({
				'src': '/'+itemImage,
				'alt': itemName
			}).setStyles({
				'width': this.minZ + "px",
				'height': this.minZ + "px"
			}).injectInside(dockFirst);

			this.dockArray[i] = el;
		
		}.bind(this));
		
		
		this.dockCap = new Element('li');
		var dockCapImg = new Element('img').setProperties({ id: 'dockCap', src: mfxshim, style: "width: "+this.minZ+'px;height:'+this.minZ+'px;'}).injectInside(this.dockCap);
		this.dockCap.injectInside(this.dock);
		//this.dockArray[this.dockArray.length+1] = dockCap;
		
		
		document.addEvent('mousemove', this.getMouseXY.create({'bind': this, 'event': Event.MOUSEMOVE}));
		//document.captureEvents(Event.MOUSEMOVE);
		
		if(!this.IEeM) {document.captureEvents(Event.MOUSEMOVE);} //hide this from IE
	},
	
	desc: function(i){
		this.dockArray[i].getFirst().getElement('span').setStyle('display','block');
		this.spanEffects[i].custom(0,100);
	},
	
	undesc: function(i){
		this.dockArray[i].getFirst().getElement('span').setStyles({'display':'none','visibility':'hidden','opacity':0});
	},	
	
	getMouseXY: function(e) {
		if(this.IEeM) { // grab the x-y if browser is IE
			var tX = event.clientX + document.body.scrollLeft;
			var tY = event.clientY + document.body.scrollTop;
		} else {  // grab the x-y pos.s if browser is not IE
			var tX = e.pageX;
			var tY = e.pageY;
		} 

		
		
		var dockX = this.dock.getCoordinates().left;
		var widthX = this.dockCap.offsetLeft+350;
		//console.log(widthX);
		
		//console.log(this.dockArray[(this.dockArray.length-1)].getFirst().tagName);
		
		var dockY = $('dockContainer').getCoordinates().top;
		var MouseArea = $('dockMouseArea').getCoordinates();
		
		if(dockX && this.dockArray) {
			if(tY>MouseArea.top && tY<MouseArea.bottom && tX>MouseArea.left && tX<MouseArea.right){
				if(tY>dockY && tX>dockX  && tX<widthX) {
					for(var j=0; j<this.dockArray.length; j++) {
						var x = this.dockArray[j].getCoordinates().left-50;
						
						var xw = (x*1)+this.maxZ;
						if(tX>x && tX<(xw)) {
							var xdif = tX - x;
							var oxdif = this.maxZ-xdif;
							var xPercent = xdif/this.maxZ;
							var oxPercent = 1-xPercent;
							
							this.dockArray[j].getFirst().getElement('img').setStyles({width:this.maxZ + "px",height:this.maxZ + "px"});
	
							if(this.dockArray[j+1]) {
								var size = (this.maxZ-this.incZ) + (xPercent*this.incZ) + "px";
								this.dockArray[j+1].getFirst().getElement('img').setStyles({ width: size, height:size });
	
							}
							if(this.dockArray[j+2]) {
								var size = this.maxZ-(2*this.incZ) + ((xPercent*this.incZ)/3) + "px"
								this.dockArray[j+2].getFirst().getElement('img').setStyles({ width: size, height: size });
							}
							if(this.dockArray[j+3]) {
								var size = this.minZ + ((xPercent*this.incZ)/4) + "px";
								this.dockArray[j+3].getFirst().getElement('img').setStyles({ width: size, height: size });

							}
							if(this.dockArray[j+4]) {
								this.dockArray[j+4].getFirst().getElement('img').setStyles({ width: this.minZ + "px", height:this.minZ + "px" });
							}
		
							if(this.dockArray[j-1]) {
								var size = (this.maxZ-this.incZ) + (oxPercent*this.incZ) + "px";
								this.dockArray[j-1].getFirst().getElement('img').setStyles({ width: size, height:size });
							}
							if(this.dockArray[j-2]) {
								var size = this.maxZ-(2*this.incZ) + (oxPercent*this.incZ) + "px";
								this.dockArray[j-2].getFirst().getElement('img').setStyles({ width: size, height: size });
							
							}
							if(this.dockArray[j-3]) {
								var size = this.minZ + (oxPercent*this.incZ) + "px";
								this.dockArray[j-3].getFirst().getElement('img').setStyles({ width: size, height: size });
								
							}
							if(this.dockArray[j-4]) {
								this.dockArray[j-4].getFirst().getElement('img').setStyles({ width: this.minZ + "px", height: this.minZ + "px" });
							
							}
						}
					}
				}
				if(dockY>tY || tX<2 || tX<dockX || tX>(this.dockArray[(this.dockArray.length-1)].offsetLeft+(this.maxZ*1))) {
					//reset sizes
					for (var k=0; k<this.dockArray.length; k++) {
						this.dockArray[k].getFirst().getElement('img').setStyles({ width: this.minZ + "px", height: this.minZ + "px" });
					}
				}
				
			}else{
				//reset sizes
				for (var k=0; k<this.dockArray.length; k++) {
					this.dockArray[k].getFirst().getElement('img').setStyles({ width: this.minZ + "px", height: this.minZ + "px" });
				}
			}
		}
		return true;
	}			
	

});

/*window.addEvent('domready',function(){

	var fisheye = new fisheyeClass($('dock'));

});*/