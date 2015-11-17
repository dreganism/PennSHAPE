package util

/**
 * Created by zeli on 10/17/15.
 */

import models.{UserandGroup, User}
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

  def getUserandGroup(userandgroup: List[UserandGroup]): JsObject = {
    implicit val userFormat = Json.format[UserandGroup]
    if(userandgroup.size>0) {
      Json.obj("202" -> userandgroup(0))
    } else {
      Json.obj("501" -> null)
    }

  }
}

