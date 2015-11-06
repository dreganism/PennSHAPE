package util

import models.Config
import play.api.libs.json.{JsObject, Json}

/**
 * Created by zeli on 10/18/15.
 */
object ConfigFormat {

  def getJsonConfig(config: Config): JsObject = {
    implicit val configFormat = Json.format[Config]

    Json.obj("config" -> config)

  }

}
