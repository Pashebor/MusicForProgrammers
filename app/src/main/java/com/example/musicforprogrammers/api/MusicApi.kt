package com.example.musicforprogrammers.api

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import retrofit2.Response
import retrofit2.http.GET

@Root(name = "rss", strict = false)
class MusicForProgramming (
    @field: Element(name = "channel")
    var channel: MusicDataResponse? = null
)

@Root(name = "channel", strict = false)
class MusicDataResponse (
    @field: ElementList(inline = true)
    var trekList: List<MusicTrack>? = null
)

@Root(name = "item", strict = false)
class MusicTrack (
    @field: Element(name = "title")
    var title: String = "",
    @field: Element(name = "description", required = false)
    var description: String = "",
    @field: Element(name = "link")
    var link: String = "",
    @field: Element(name = "guid")
    var guid: String = "",
    @field: Namespace(prefix = "itunes")
    @field: Element(name = "keywords")
    var keywords: String = "",
    @field: Namespace(prefix = "itunes")
    @field: Element(name = "duration")
    var duration: String = "",
    @field: Namespace(prefix = "itunes")
    @field: Element(name = "author")
    var author: String = "",
    @field: Namespace(prefix = "itunes")
    @field: Element(name = "image", required = false)
    var image: TrackImage? = null,
    @field:Element(name = "enclosure")
    var trackData: TrackData? = null
)

@Root(name = "image", strict = false)
class TrackImage (
    @field:Attribute(name = "href", required = true)
    var href: String = "",
)

@Root(name = "enclosure")
class TrackData(
    @field:Attribute(name = "url", required = true)
    var url: String = "",
    @field:Attribute(name = "length", required = true)
    var length: Int = 0,
    @field:Attribute(name = "type", required = true)
    var type: String = "",

)

interface MusicApi {
    @GET("rss.php")
    suspend fun getMusicList(): Response<MusicForProgramming>
}