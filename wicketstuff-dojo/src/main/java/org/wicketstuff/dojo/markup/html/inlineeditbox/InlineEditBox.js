dojo.hostenv.writeIncludes();

dojo.addOnLoad(function() {
		var editTable = dojo.widget.byId("${markupId}");
		dojo.event.connect(editTable, "onSave", function(newValue) {
			${callbackScript}
		});
		connectEnterKey(editTable);
	}
);

function initInlineEditBox(markupId, callbackScript) {
	var editTable = dojo.widget.byId(markupId);
	dojo.event.connect(editTable, "onSave", function(newValue) {
		eval(callbackScript);
	});
	connectEnterKey(editTable);
}

function connectEnterKey(editTable) {
	dojo.event.connect(editTable.text, "onkey", function(event) {
		if(event.keyCode == event.KEY_ENTER && !editTable.submitButton.disabled) {
			editTable.saveEdit(event);
		}
	});
}