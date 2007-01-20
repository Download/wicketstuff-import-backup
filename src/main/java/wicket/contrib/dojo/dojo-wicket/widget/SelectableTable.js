dojo.provide("dojoWicket.widget.SelectableTable");


dojo.require("dojo.event.*");
dojo.require("dojo.widget.*");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.html.*");
dojo.require("dojo.html.selection");

dojo.widget.defineWidget(
	"dojoWicket.widget.SelectableTable",
	dojo.widget.HtmlWidget,{

	pos: 0,
	
	headClass: "",
	tbodyClass: "",
	
	enableAlternateRows: false,
	enableMultipleSelect: true,
	
	rowAlternateClass: "alt",
	rowSelectedClass: "selected",
	rowMouseOverClass: "mouseOver",
	rowSelectedAndMouseOverClass: "selectedMouseOver",
	
	//from HtmlWidget
	isContainer: false,
	templatePath:null,
	templateCssPath : dojo.uri.dojoUri("../dojo-wicket/widget/template/DojoSelectableListContainer.css"),
	
	postCreate:function(){
	
		//headers classes
		var thead=this.domNode.getElementsByTagName("thead")[0];
		if(this.headClass.length>0){
			thead.className=this.headClass;
		}
		
		var tbody=this.domNode.getElementsByTagName("tbody")[0];
		if (this.tbodyClass.length>0) {
			tbody.className=this.tbodyClass;
		}
	
		this.resetSelections();
		
		this.connectEvent();
		
	},
	
	connectEvent: function(){
		var body = this.domNode.getElementsByTagName("tbody")[0];
		var rows = body.getElementsByTagName("tr");
		
		for(var i=0; i<rows.length; i++){
			dojo.html.disableSelection(rows[i]);
			dojo.event.connect(rows[i], "onclick", this, "onUISelect");
			dojo.event.connect(rows[i], "onmouseover", this, "onOver");
			dojo.event.connect(rows[i], "onmouseout", this, "onOut");
			dojo.event.connect(rows[i], "ondblclick", this, "onChoose");
		}
	},
	
	resetSelections:function(){
		var body = this.domNode.getElementsByTagName("tbody")[0];
		var idx = 0;
		var rows = body.getElementsByTagName("tr");

		for(var i=0; i<rows.length; i++){
			if(rows[i].parentNode==body){
				rows[i].removeAttribute("selected");

				if(this.enableAlternateRows && idx%2==1){
					rows[i].className=this.rowAlternateClass;
				}else{
					rows[i].className="";
				}
				rows[i].setAttribute("pos", idx);
				idx++;
			}
		}
	},
	
	showSelections:function(){
		var body = this.domNode.getElementsByTagName("tbody")[0];
		var rows = body.getElementsByTagName("tr");
		var idx = 0;
		for(var i=0; i<rows.length; i++){
			if(rows[i].parentNode==body){
				if(dojo.html.getAttribute(rows[i],"selected")=="true"){
					rows[i].className = this.rowSelectedClass;
				} else {
					if(this.enableAlternateRows && idx%2==1){
						rows[i].className = this.rowAlternateClass;
					}else{
						rows[i].className="";
					}
				}
				idx++;
			}
		}
	},
	
	onOver:function(/* DomEvent */ e){ 
		var row=dojo.html.getParentByType(e.target,"tr");
		var selected = (dojo.html.getAttribute(row,"selected")=="true");
        row.className = selected ? this.rowSelectedAndMouseOverClass : this.rowMouseOverClass;
	},
	
	onOut:function(/* DomEvent */ e){ 
		this.showSelections();
	},
	
	onUISelect:function(/* DomEvent */ e){
		//	summary
		//	fired when a user selects a row
		var row=dojo.html.getParentByType(e.target,"tr");
		var body=dojo.html.getParentByType(row,"tbody");
		if(this.enableMultipleSelect){
			if(e.metaKey||e.ctrlKey){
				if(row.getAttribute("selected") != null && row.getAttribute("selected") == "true"){
					row.removeAttribute("selected");
				}else{
					row.setAttribute("selected","true");
				}
			}else if(e.shiftKey){
				//	the tricky one.  We need to figure out the *last* selected row above, 
				//	and select all the rows in between.
				var startRow;
				var rows=body.getElementsByTagName("tr");
				//	if there's a selection above, we go with that first. 
				for(var i=0;i<rows.length;i++){
					if(rows[i].parentNode==body){
						if(rows[i]==row) break;
						if(dojo.html.getAttribute(rows[i],"selected")=="true"){
							startRow=rows[i];
						}
					}
				}
				//	if there isn't a selection above, we continue with a selection below.
				if(!startRow){
					startRow=row;
					for(;i<rows.length;i++){
						if(dojo.html.getAttribute(rows[i],"selected")=="true"){
							row=rows[i];
							break;
						}
					}
				}
				this.resetSelections(body);
				if(startRow==row){
					//	this is the only selection
					row.setAttribute("selected","true");
				}else{
					var doSelect=false;
					for(var i=0; i<rows.length; i++){
						if(rows[i].parentNode==body){
							rows[i].removeAttribute("selected");
							if(rows[i]==startRow){
								doSelect=true;
							}
							if(doSelect){
								rows[i].setAttribute("selected","true");
							}
							if(rows[i]==row){
								doSelect=false;
							}
						}
					}
				}
			}else{
				//	reset the selection
				this.resetSelections(body);
				row.setAttribute("selected","true");
			}
		}else{
			//	reset the selection and go.
			this.resetSelections(body);
			row.setAttribute("selected","true");
		}
		this.showSelections();
		this.onSelect(e);
		e.stopPropagation();
		e.preventDefault();
	},
	
	onChoose:function(/* DomEvent */ e){ 
		
	},

	//	the following the user can override.
	onSelect:function(/* DomEvent */ e){ 
		//	summary
		//	empty function for the user to attach code to, fired by onUISelect
	}

});

function getSelectableTableSelection(id) {
	var container = document.getElementById(id);
	var body = container.getElementsByTagName('tbody')[0];
	var rows=body.getElementsByTagName('tr')
	var selection = '';
	var index = 0;
	for(var i=0; i<rows.length; i++) {
		if(rows[i].parentNode==body) {
			if(dojo.html.getAttribute(rows[i],'selected')=='true') {
				selection += '&select=' + index;
			}
			index++;
		}
	}
	return selection;
}
