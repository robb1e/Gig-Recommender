package com.gu

import org.apache.commons.httpclient._
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.params.HttpConnectionManagerParams

object WebClient {

  val connectionManager = new MultiThreadedHttpConnectionManager()
  val params = new HttpConnectionManagerParams()
  params.setMaxTotalConnections(1000)
  params.setDefaultMaxConnectionsPerHost(1000)
  params.setSoTimeout(10000)
  connectionManager.setParams(params)
  val client = new HttpClient(connectionManager)

  def get(uri: String) = {
     val method = new GetMethod(uri)
     method.getParams().setContentCharset("UTF-8")
     client.executeMethod(method)
     method.getResponseBodyAsString()
  }
}