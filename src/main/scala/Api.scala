package com.gu.music.recommendations

import org.scalatra._
import net.liftweb.json.{Serializer, Formats, TypeInfo, DefaultFormats}
import net.liftweb.json.JsonAST.{JString, JValue}
import net.liftweb.json.Extraction
import com.mongodb.DBObject
import com.mongodb.BasicDBList

class Api extends ScalatraServlet {

    //implicit val formats = new Formats {val dateFormat = DefaultFormats.lossless.dateFormat} + new JsonSerializer

    val bandSlurper = new BandSlurper

    before {
        contentType = "application/json; charset=utf-8"
    }

    get("/") {
        bandSlurper.getBands
    }


    get("/similar/:mbid"){
      bandSlurper.getSimilarBandsTo(params("mbid")) match {
        case Some(band) => {
          val likes = band.get("like")
          """{"name":"%s", "mbid":"%s", "like":[%s]}""" format (band.get("name"), band.get("mbid"), likes)
        }
        case _ => None
      }
    }

    get("/top/:lastfm"){
      val bands = bandSlurper.topArtists(params("lastfm"))
      val sb = new StringBuilder
      sb.append("""{"top":[""")
      bands.foreach(b => sb.append("""{"name":"%s", "mbid":"%s"},""" format (b.name, b.mbid)))
      sb.append("]}")
      sb.toString
    }

    protected def contextPath = request.getContextPath

//    class JsonSerializer extends Serializer[DBObject] {
//
//      def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
//        case list: BasicDBList => Extraction.decompose(List.empty ++ list)
//        case dbo: DBObject => Extraction.decompose(ListMap.empty ++ dbo.toMap)
//      }
//
//      def deserialze(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DBObject] = throw new UnsupportedOperationException("boom")
//
//    }

}