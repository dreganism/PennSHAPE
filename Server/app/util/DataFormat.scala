package util

import models.Data
import play.api.libs.json.{JsObject, Json}

import scala.collection.mutable.ListBuffer

/**
 * Created by zeli on 10/18/15.
 */
object DataFormat {

  def getJsonDataMap(data: Map[String, ListBuffer[Data]]): JsObject = {
    implicit val dataFormat = Json.format[Data]

    Json.obj("data" -> data)
  
  }


}
