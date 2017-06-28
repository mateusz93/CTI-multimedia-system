var widgetAPI = new Common.API.Widget();
var tvKey = new Common.API.TVKeyValue();

var exitPINCode = "28423646";
var exitPINIndex = 0;

var Main =
{

};

Main.onLoad = function()
{
	// Enable key event processing
	this.enableKeys();
	widgetAPI.sendReadyEvent();
	/*
	try
	{
		console.info("Building gallery");
		//var gallery = new Gallery('gallery', [new ImageGalleryObject('app/images/img_01.jpg'), new ImageGalleryObject('app/images/img_02.jpg'), new ImageGalleryObject('app/images/img_03.jpg'), new VideoGalleryObject('app/images/vid_01.mp4'), new ImageGalleryObject('app/images/img_04.jpg')], {displayBubbles: true});
		console.info("Building gallery finished, starting...");
		//gallery.start();
		console.info("Started");
	} catch (e) {
		console.info(e);
		console.info(e.stack);
	}
	*/
};

Main.onUnload = function()
{

};

Main.enableKeys = function()
{
	document.getElementById("anchor").focus();
};

Main.keyDown = function()
{
	var keyCode = event.keyCode;
	alert("Key pressed: " + keyCode);

	switch(keyCode)
	{
    	case tvKey.KEY_EXIT:
		case tvKey.KEY_RETURN:
		case tvKey.KEY_PANEL_RETURN:
			alert("RETURN");
            widgetAPI.blockNavigation(event);
			break;
		case tvKey.KEY_LEFT:
			alert("LEFT");
			break;
		case tvKey.KEY_RIGHT:
			alert("RIGHT");
			break;
		case tvKey.KEY_UP:
			alert("UP");
			break;
		case tvKey.KEY_DOWN:
			alert("DOWN");
			break;
		case tvKey.KEY_ENTER:
		case tvKey.KEY_PANEL_ENTER:
			alert("ENTER");
			break;
		case tvKey.KEY_0:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '0') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_1:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '1') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_2:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '2') exitPINIndex++;
			else exitPINIndex = 1;
			break;
		case tvKey.KEY_3:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '3') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_4:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '4') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_5:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '5') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_6:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '6') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_7:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '7') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_8:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '8') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		case tvKey.KEY_9:
			if (exitPINIndex >= 0 && exitPINIndex < exitPINCode.length && exitPINCode.charAt(exitPINIndex) == '9') exitPINIndex++;
			else exitPINIndex = 0;
			break;
		default:
			alert("Unhandled key");
        	widgetAPI.blockNavigation(event);
			break;
	}
	if (exitPINIndex >= exitPINCode.length) widgetAPI.sendReturnEvent();
};
