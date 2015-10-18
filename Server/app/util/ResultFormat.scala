package util

import models._
import play.api.libs.json.{Json, JsObject}

/**
 * Created by zeli on 10/17/15.
 */
object ResultFormat {

/*
  // convert from Stock object to JSON (serializing to JSON)
  def failFormat(fail: Fail): JsObject = {
    implicit val failFormat = Json.format[Fail]

    Json.obj("Failure" -> fail)

  }
*/


  def getResultJson(result:Result):JsObject = {
    implicit val configFormat = Json.format[Config]
    implicit val userFormat = Json.format[User]
    implicit val messageFormat = Json.format[GroupMessage]
    implicit val groupmessageFormat = Json.format[Message]
    implicit val dataFormat = Json.format[Data]
    implicit val resultFormat = Json.format[Result]

    Json.obj("Success" -> result)
  }


}
