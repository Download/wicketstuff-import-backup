function initDrag${MarkupId}(){
	var dl = byId('${MarkupId}');
	var drag = new dojo.dnd.HtmlDragSource(dl, '${DragId}');
}
dojo.event.connect(dojo, 'loaded', 'initDrag${MarkupId}');