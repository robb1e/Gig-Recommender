package com.gu.music.recommendations

import com.gu.WebClient
import scala.util.parsing.json.JSON
import net.liftweb.json.JsonParser.parse
import net.liftweb.json.{DefaultFormats, Formats}
import scala.xml._
import java.io._
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.conversions.scala._

case class Band(name: String, mbid: String = "")
case class BandList(bands: List[Band])

class BandSlurper {

  val mongoConn = MongoConnection()
  val mongoDB = mongoConn("sxsw")
  val bandsCollection = mongoDB("bands")
  val similarCollection = mongoDB("similar")
  val configCollection = mongoDB("config")

  bandsCollection.ensureIndex(MongoDBObject("name" -> 1), "name", true)
  similarCollection.ensureIndex(MongoDBObject("name" -> 1), "name", true)

  configCollection.findOne(MongoDBObject("reload" -> true)).map(res => init)


  def init = {
     val bandData = Option(getClass.getResourceAsStream("/sxswbands.json"))
      .map(scala.io.Source.fromInputStream(_))
      .map(_.mkString)
      .getOrElse(throw new Exception("no band json file found"))

    val bandsJson = JSON.parseFull(bandData).map(b => b.asInstanceOf[List[AnyRef]])

    val bandsCase = bandsJson.get.map(b =>
      Band(b.asInstanceOf[Map[String, String]].getOrElse("band_name", ""), b.asInstanceOf[Map[String, String]].getOrElse("musicbrainz", "")))
      .filter(bd => bd.mbid != "")

    bandsCase.foreach(b => {
     bandsCollection += MongoDBObject("name" -> b.name.trim, "mbid" -> b.mbid.trim)
     println("looking for similar bands for '%s'" format b.name)
     val similar = WebClient.get("http://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&mbid=%s&api_key=b25b959554ed76058ac220b7b2e0a026" format b.mbid)
     val someXml = XML.loadString(similar)
     val similarBands = (someXml \ "similarartists" \ "artist").map(s => Band((s \ "name").text.trim, (s \ "mbid").text.trim))

      similarBands.foreach(s => similarCollection.findOne(MongoDBObject("mbid" -> s.mbid)) match {
        case Some(result) => {
          similarCollection.update(MongoDBObject("mbid" -> s.mbid), MongoDBObject("$addToSet" -> MongoDBObject("like" -> MongoDBObject("name" -> b.name.trim, "mbid" -> b.mbid.trim))))
        }
        case _ => {
          similarCollection += MongoDBObject("name" -> s.name, "mbid" -> s.mbid)
          similarCollection.update(MongoDBObject("mbid" -> s.mbid), MongoDBObject("$addToSet" -> MongoDBObject("like" -> MongoDBObject("name" -> b.name.trim, "mbid" -> b.mbid.trim))))
        }
      })


   })
  }


  def getBands = None

  def getSimilarBandsTo(mbid: String) = {
    similarCollection.findOne(MongoDBObject("mbid" -> mbid))
  }

  def topArtists(lastfm: String) = {
    val topArtists = WebClient.get("http://ws.audioscrobbler.com/2.0/?method=user.gettopartists&user=%s&api_key=b25b959554ed76058ac220b7b2e0a026" format lastfm)
    val someXml = XML.loadString(topArtists)
    (someXml \ "topartists" \ "artist").map(a => Band((a \ "name").text.trim, (a \ "mbid").text.trim))
  }

}
