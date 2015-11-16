package util

import models.{GroupMessage, Message}
import play.api.libs.json.{JsObject, Json}

/**
 * Created by zeli on 10/17/15.
 */
object MessageFormat {

  def getJsonMessages(messages: List[Message]): JsObject = {
    implicit val messageFormat = Json.format[Message]

  Json.obj("messages" -> messages)

  }

  def getJsonGroupMessages(messages: List[GroupMessage]): JsObject = {
    implicit val messageFormat = Json.format[GroupMessage]

    Json.obj("groupmessages" -> messages)

  }



}
