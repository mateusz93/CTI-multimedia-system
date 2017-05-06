console.info($('#gallery'));

//GalleryException
function GalleryException(msg)
{
    var message = msg;
    this.getMessage = function(){
        return message;
    }
}

//Gallery
function Gallery(elementID, objects)
{
    var element = $('#' + elementID);
    var galleryObjects = objects || [];
    var currentObjectIndex = 0;
    //TODO: Preload all objects for faster response times
    var loadNextImage = function(){
        var obj = galleryObjects[currentObjectIndex];
        obj.addEventListener('stop', loadNextImage);
        element.html(obj.getLayout());
        obj.start();
        currentObjectIndex++;
        if (currentObjectIndex >= galleryObjects.length) currentObjectIndex -= galleryObjects.length;
    };
    this.start = function(){
        loadNextImage();
    };
}

//GalleryObject
function GalleryObject(url)
{
    this.listeners = [];
    this.url = url;
}
GalleryObject.prototype.getLayout = function() {
    throw new GalleryException('Method getLayout must be redefined in inheriting class!');
};
GalleryObject.prototype.start = function() {
    throw new GalleryException('Method start must be redefined in inheriting class!');
};
GalleryObject.prototype.addEventListener = function(event, handler) {
    if (this.listeners.filter(function(e, i, a){ return e.event == event && e.handler == handler; }).length < 1)
    {
        this.listeners.push({event: event, handler: handler});
    }
};
GalleryObject.prototype.removeEventListener = function(event, handler) {
    var listener = this.listeners.filter(function(e, i, a){ return e.event == event && e.handler == handler; });
    if (listener.length > 0)
    {
        this.listeners.splice(this.listeners.indexOf(listener[0]), 1);
    }
};
GalleryObject.prototype.dispatchEvent = function(event, data) {
    var listener = this.listeners.filter(function(e, i, a){ return e.event == event; });
    for (var i = 0; i < listener.length; i++)
    {
        window.setTimeout(listener[i].handler, 0);
    }
};

//ImageGalleryObject
function ImageGalleryObject(url)
{
    GalleryObject.call(this, url);
}
ImageGalleryObject.prototype = Object.create(GalleryObject.prototype);
ImageGalleryObject.prototype.constructor = ImageGalleryObject;
ImageGalleryObject.prototype.getLayout = function() {
    // return '<img src="' + this.url + '" style="width: 100%;" />';
    return '<div class="image-object" style="background-image:url(\'' + this.url + '\');"></div>';
};
ImageGalleryObject.prototype.start = function() {
    window.setTimeout((function(){ this.dispatchEvent('stop'); }).bind(this), 5000);
};

//VideoGalleryObject
function VideoGalleryObject(url)
{
    GalleryObject.call(this, url);
    this.video = $('<video controls class="video-object"><source src="' + this.url + '" type="video/mp4">Your browser doesn\'t support HTML5 video tag.</video>');
}
VideoGalleryObject.prototype = Object.create(GalleryObject.prototype);
VideoGalleryObject.prototype.constructor = VideoGalleryObject;
VideoGalleryObject.prototype.getLayout = function() {
    return this.video;
};
VideoGalleryObject.prototype.start = function() {
    this.video.on('ended', this.dispatchEvent.bind(this, 'stop'));
    this.video.get(0).play();
};

var gallery = new Gallery('gallery', [new ImageGalleryObject('./img/img_01.jpg'), new ImageGalleryObject('./img/img_02.jpg'), new ImageGalleryObject('./img/img_03.jpg'), new VideoGalleryObject('./img/vid_01.mp4'), new VideoGalleryObject('./img/vid_02.mp4')]);
gallery.start();

//TODO:
/**
 * Dynamiczna aktualizacja tablicy galleryObjects
 * Responsywność obrazów    -> DONE
 * Kółka
 * Animacje przy przechodzeniu
 * Preloading obiektów aby uniknąć pustego ekranu podczas wczytywania
 * Postery dla video?
 */