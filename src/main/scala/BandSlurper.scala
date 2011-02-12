package com.gu.music.recommendations

import com.gu.WebClient
import scala.util.parsing.json.JSON
import net.liftweb.json.JsonParser.parse
import net.liftweb.json.{DefaultFormats, Formats}
import scala.xml._

case class Band(name: String, mbid: String = "")
case class BandList(bands: List[Band])

class BandSlurper {

  val bandData = Option(getClass.getResourceAsStream("/sxswbands.json"))
    .map(scala.io.Source.fromInputStream(_))
    .map(_.mkString)
    .getOrElse(throw new Exception("no band json file found"))

  val bandsJson = JSON.parseFull(bandData).map(b => b.asInstanceOf[List[AnyRef]])

  val bandsCase = bandsJson.get.map(b =>
    Band(b.asInstanceOf[Map[String, String]].getOrElse("band_name", ""), b.asInstanceOf[Map[String, String]].getOrElse("musicbrainz", "")))
    .filter(bd => bd.mbid != "")

  //println (WebClient.get("/?method=artist.getsimilar&artist=cher&api_key=b25b959554ed76058ac220b7b2e0a0"))

  val similar = WebClient.get("http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&artist=cher&api_key=b25b959554ed76058ac220b7b2e0a026")
  val someXml = XML.loadString(similar)
  //println ((someXml \ "similarartists" \ "artist" \ "name").map(b => Band(b.text)))
  val similarBands = (someXml \ "similarartists" \ "artist").map(b => Band((b \ "name").text, (b \ "mbid").text))
  println(similarBands)


  def getBands = bandsCase


}