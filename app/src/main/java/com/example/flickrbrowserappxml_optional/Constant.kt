package com.example.flickrbrowserappxml_optional




object Constant {
    fun url(tags:String):String{
//        var  url = "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=5cfbf3eb82179f031c7e1b5d82759cdb&tags=$tags&format=json&nojsoncallback=1"
        var  url = "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=5cfbf3eb82179f031c7e1b5d82759cdb&tags=$tags&format=rest"
        return url
    }

}
data class Item(val text:String?,val img:String?)
