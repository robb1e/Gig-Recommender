package com.gu.music.recommendations

import org.scalatra._
import net.liftweb.json.{Serializer, Formats, TypeInfo, DefaultFormats}
import net.liftweb.json.JsonAST.{JString, JValue}
import net.liftweb.json.Extraction
import com.mongodb.DBObject
import com.mongodb.BasicDBList
import javax.servlet.ServletConfig

class Api extends ScalatraServlet {

    val bandSlurper = new BandSlurper

    override def init(servletConfig: ServletConfig) = {
        super.init(servletConfig)
        bandSlurper.init
    }

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

    get("/recommended/:lastfm"){
      val bands = bandSlurper.topArtists(params("lastfm"))
      val recommended = bands.map(b => bandSlurper.getSimilarBandsTo(b.mbid)).filter(res => res != None)
      val sb = new StringBuilder
      sb.append("""{"recommended":[""")
      recommended.foreach(band => {
        val likes = band.get.get("like")
        sb.append("""{"name":"%s", "mbid":"%s", "like":%s},""" format (band.get.get("name"), band.get.get("mbid"), likes))

      })
      sb.append("]}")
      val json = sb.toString.replace(",]}", "]}")
      if (params.keySet.contains("callback")){
         contentType = "application/javascript; charset=utf-8"
         params("callback") + "(" + json + ")"
      } else {
        json
      }
    }

    protected def contextPath = request.getContextPath

}