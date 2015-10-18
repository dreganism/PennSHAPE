package util

/**
 * Created by zeli on 10/17/15.
 */

import models.User
import play.api.libs.json._

object UserFormat {

  def getUserList(status: String, users: List[User]): JsObject = {
    implicit val userFormat = Json.format[User]
    Json.obj(status -> users)
  }

  def getGroupUserList(users: List[User]): JsObject = {
    implicit val userFormat = Json.format[User]
    Json.obj("group" -> users)
  }


  def getUser(status: String, user: User): JsObject = {
    implicit val userFormat = Json.format[User]

    Json.obj(status -> user)

  }

}

