package com.gu.music.recommendations

import org.scalatra._

class Api extends ScalatraServlet {

    val bandSlurper = new BandSlurper

    before {
        contentType = "application/json"
    }

    get("/") {
        bandSlurper.getBands
    }

    protected def contextPath = request.getContextPath

}