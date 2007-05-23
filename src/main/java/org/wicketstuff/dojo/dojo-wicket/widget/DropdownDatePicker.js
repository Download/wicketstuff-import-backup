dojo.provide("dojoWicket.widget.DropDownDatePicker");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.DropdownContainer");
dojo.require("dojo.widget.DatePicker");
dojo.require("dojo.event.*");
dojo.require("dojo.html.*");
dojo.require("dojo.date.format");
dojo.require("dojo.date.serialize");
dojo.require("dojo.string.common");
dojo.require("dojo.i18n.common");
dojo.requireLocalization("dojo.widget", "DropdownDatePicker");

dojo.widget.defineWidget(
	"dojo.widget.DropdownDatePicker",	//hack to overwrite dropDownDatePicker
	dojo.widget.DropdownContainer,
	{
		/*
		summary: 
			A form input for entering dates with a pop-up dojo.widget.DatePicker to aid in selection

	 	description: 
			This is DatePicker in a DropdownContainer, it supports all features of DatePicker.
	
			The value displayed in the widget is localized according to the default algorithm provided
			in dojo.date.format and dojo.date.parse.  It is possible to customize the user-visible formatting
			with either the formatLength or displayFormat attributes.  The value sent to the server is
			typically a locale-independent value in a hidden field as defined by the name attribute.
			RFC3339 representation is used by default, but other options are available with saveFormat.

	 	usage: 
	 	              var ddp = dojo.widget.createWidget("DropdownDatePicker", {},   
	 	              dojo.byId("DropdownDatePickerNode")); 
	 	 
	 	              <input dojoType="DropdownDatePicker">
		*/

		iconURL: dojo.uri.dojoUri("src/widget/templates/images/dateIcon.gif"),
		zIndex: "10",

		//String
		// 	Type of visible formatting used, appropriate to locale (choice of long, short, medium or full)
		//  See dojo.date.format for details.
		formatLength: "short",
		//String
		// 	Pattern used to display formatted date.  Setting this overrides the locale-specific settings
		//  which are used by default.  See dojo.date.format for a reference which defines the formatting patterns.
		displayFormat: "",
		//String
		//	Formatting scheme used when submitting the form element.  This formatting is used in a hidden
		//  field (name) intended for server use, and is therefore typically locale-independent.
		//  By default, uses rfc3339 style date formatting (rfc)
		//	Use a pattern string like displayFormat or one of the following:
		//	rfc|iso|posix|unix
		saveFormat: "",
		//String|Date
		//	form value property if =='today' will default to todays date
		value: "", 
		//String
		// 	name of the form element, used to create a hidden field by this name for form element submission.
		name: "",

		// Implement various attributes from DatePicker

		//Integer
		//	total weeks to display default 
		displayWeeks: 6, 
		//Boolean
		//	if true, weekly size of calendar changes to accomodate the month if false, 42 day format is used
		adjustWeeks: false,
		//String|Date
		//	first available date in the calendar set
		startDate: "1492-10-12",
		//String|Date
		//	last available date in the calendar set
		endDate: "2941-10-12",
		//Integer
		//	adjusts the first day of the week 0==Sunday..6==Saturday
		weekStartsOn: "",
		//Boolean
		//	disable all incremental controls, must pick a date in the current display
		staticDisplay: false,
		
		//If true disabled the input from keyboard
		inputNotAllowed:false,
		
		templatePath:  dojo.uri.dojoUri("src/widget/templates/DatePicker.htm"),
		templateCssPath:  dojo.uri.dojoUri("src/widget/templates/DatePicker.css"),
		
		//store a bad value
		inputValue: null,
		
		postMixInProperties: function(localProperties, frag){
			// summary: see dojo.widget.DomWidget

			this.inputValue = this.value;

			dojo.widget.DropdownDatePicker.superclass.postMixInProperties.apply(this, arguments);
			var messages = dojo.i18n.getLocalization("dojo.widget", "DropdownDatePicker", this.lang);
			this.iconAlt = messages.selectDate;
			
			if(typeof(this.value)=='string'&&this.value.toLowerCase()=='today'){
				this.value = new Date();
			}
			if(this.value && isNaN(this.value)){
				var orig = this.value;
				this.value = dojo.date.fromRfc3339(this.value);
			}
			if(this.value && !isNaN(this.value)){
				this.value = new Date(this.value);
			}
		},

		fillInTemplate: function(args, frag){
			// summary: see dojo.widget.DomWidget
			dojo.widget.DropdownDatePicker.superclass.fillInTemplate.call(this, args, frag);
			//attributes to be passed on to DatePicker
			
			var dpArgs = {widgetContainerId: this.widgetId, lang: this.lang, value: this.value,
				startDate: this.startDate, endDate: this.endDate, displayWeeks: this.displayWeeks,
				weekStartsOn: this.weekStartsOn, adjustWeeks: this.adjustWeeks, staticDisplay: this.staticDisplay, 
				templateCssPath:  this.templateCssPath, templatePath: this.templatePath
				};
			
			
			//build the args for DatePicker based on the public attributes of DropdownDatePicker
			this.datePicker = dojo.widget.createWidget("DatePicker", dpArgs, this.containerNode, "child");
			dojo.event.connect(this.datePicker, "onValueChanged", this, "onSetDate");
			
			if(this.value){
				this.onSetDate();
			}
			this.containerNode.style.zIndex = this.zIndex;
			this.containerNode.explodeClassName = "calendarBodyContainer";
			this.valueNode.name=this.name;
			
			//Can disable the input if inputNotAllowed = true
			this.inputNode.disabled = this.inputNotAllowed;
			
			if (this.value == null && this.inputValue != null){
				this.inputNode.value = this.inputValue;
				this.valueNode.value = this.inputValue; 
			}
		},

		getValue: function(){
			// summary: return current date in RFC 3339 format
			return this.valueNode.value; /*String*/
		},

		getDate: function(){
			// summary: return current date as a Date object
			return this.datePicker.value; /*Date*/
		},

		setValue: function(/*Date|String*/rfcDate){
			//summary: set the current date from RFC 3339 formatted string or a date object, synonymous with setDate
			this.setDate(rfcDate);
		},

		setDate: function(/*Date|String*/dateObj){
		//summary: set the current date and update the UI
			this.datePicker.setDate(dateObj);
			this._synchValueNode();
		},
	
		onSetDate: function(){
			this.inputNode.value = dojo.date.format(this.datePicker.value,
				{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});
			if(this.value < this.datePicker.startDate||this.value>this.datePicker.endDate){
				this.inputNode.value = "";
			}
			this._synchValueNode();
			this.onValueChanged(this.getDate());
			this.hideContainer();
		},

		onValueChanged: function(/*Date*/dateObj){
		//summary: triggered when this.value is changed
		},
		
		onInputChange: function(){
			var input = dojo.string.trim(this.inputNode.value);
			if(input){
				var inputDate = dojo.date.parse(input,
						{formatLength:this.formatLength, datePattern:this.displayFormat, selector:'dateOnly', locale:this.lang});			
				if(inputDate){
					this.setDate(inputDate);
				}
			} else {
				this.valueNode.value = input;
			}
			//If the date can not be parsed, just duplicate user entry in valueNode... Wicket will validate that
			if(input){ this.valueNode.value = input }
		},

		_synchValueNode: function(){
			var date = this.datePicker.value;
			var value;
			switch(this.saveFormat.toLowerCase()){
				case "rfc": case "iso": case "":
					value = dojo.date.toRfc3339(date, 'dateOnly');
					break;
				case "posix": case "unix":
					value = Number(date);
					break;
				default:
					value = dojo.date.format(date, {datePattern:this.saveFormat, selector:'dateOnly', locale:this.lang});
			}
			this.valueNode.value = value;
		},
		
		enable: function() {
			//Can disable the input if inputNotAllowed = true
			this.inputNode.disabled = this.inputNotAllowed;
			this.datePicker.enable();
			dojo.widget.DropdownDatePicker.superclass.enable.apply(this, arguments);
		},
		
		disable: function() {
			this.inputNode.disabled = true;
			this.datePicker.disable();
			dojo.widget.DropdownDatePicker.superclass.disable.apply(this, arguments);
		}
	}
);
