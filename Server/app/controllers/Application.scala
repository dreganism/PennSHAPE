package controllers

import dao._
import models._
import play.api.libs.json.JsValue
import play.api.mvc._
import util._

import scala.collection.mutable.ListBuffer

class Application extends Controller {

  def index = Action {
    Ok("It works!")
  }

  def hello(name: String) = Action {
    Ok("Hello " + name)
  }

  def echo = Action { request =>
    Ok("Got request [" + request + "]")
  }

  def users = Action {

    val result: List[User] = UserDao.selectAll()
    Ok(UserFormat.getUserList("202", result))

  }

  def getGroupUsersById(id: String) = Action {
    val result: List[User] = UserDao.getGroupUsers(id)
    Ok(UserFormat.getGroupUserList(result))
  }


  def getMessagesByUserid(uid: String) = Action {
    val messages: List[Message] = MessageDao.getMessageByUserId(uid)
    Ok(MessageFormat.getJsonMessages(messages))

  }


  def getGroupDataByUserid(uid: String) = Action {
    val groupdata: Map[String, ListBuffer[Data]] = DataDao.getGroupDataByUserId(uid).toMap
    Ok("{\"202\":"+DataFormat.getJsonDataMap(groupdata)+"}")

  }

  def getConfig() = Action {
    val config: Config = ConfigDao.getConfig()
    Ok("{\"202\":"+ConfigFormat.getJsonConfig(config)+"}")

  }

  def getResult(uid:String) = Action {
    val res = ResultDao.getResultByUserId(uid)
    if(res.status == "true") {
      Ok(ResultFormat.getResultJson(res))
    }else {
      Ok("{\"501\":\"User is is not found\"}")
    }
  }

  def getUidbyEmail(email:String) = Action {
    val res = UserDao.getUseridByEmail(email)
    print(res)
    if (res.size >0) {
      Ok(UserFormat.getUserandGroup(res))
    }else {
      Ok("{\"501\":\"User is is not found\"}")
    }
  }


  def updateFavorite(uid:String) = Action {
      request =>
    val body: AnyContent = request.body

        var result :Boolean = true

    val jsonBody: Option[JsValue] = body.asJson

    jsonBody.map { jsValue =>

      val favorite:String = (jsValue \ "favorite").get.toString().replace("\"","") // 2015-10-26

      result = UserDao.setFavorite(uid, favorite)
    }
        if (result){
          Ok("{\"202\":\"Update successfully\"}")
        } else {
          Ok("{\"505\":\"Failed to update Favorite\"}");
        }
  }


  def setGroupMessage(uid:String) = Action {

    request =>
      val body: AnyContent = request.body
      var result: Option[Long] = Some(-1)

      val jsonBody: Option[JsValue] = body.asJson

      jsonBody.map { jsValue =>

        val time:String = (jsValue \ "time").get.toString().replace("\"","") // 2015-10-26
        val message = (jsValue \ "message").get.toString().replace("\"","")
        val groupid = (jsValue \ "groupid").get.toString().replace("\"","")
        result = MessageDao.insertGroupMessage(uid, time, message, groupid)
      }

      if(result.get != -1) {
        Ok("{\"202\":\"Update successfully\"}")
      }else {
        Ok("{\"505\":\"Failed to update geolocation\"}")
      }


  }


  def getGroupMessage(groupid:Int,from: Option[Int]) = Action {

    var fromvalue = 0
    if(from != None) {
      fromvalue = from.get
    }

    val res = MessageDao.getGroupMessageByGroupId(groupid, fromvalue)
    if(res.size > 0) {
      Ok(MessageFormat.getJsonGroupMessages(res))
    }else {
      Ok("{\"501\":\"Group is not found\"}")
    }


  }



  def insertGeoLocation(uid:String) = Action {

    request =>
      val body: AnyContent = request.body

      val jsonBody:Option[JsValue] = body.asJson

      jsonBody.map { jsValue =>
        val geotime:String = (jsValue \ "geotime").get.toString().replace("\"","") // 2015-10-26
        val lat = (jsValue \ "lat").get.toString().replace("\"","")
        val lon = (jsValue \ "lon").get.toString().replace("\"","")
        println("uid:"+uid+" date:"+geotime+" "+"lat:"+lat+"lon:"+lon)
        try {
          val success = GeolocationDao.insertGeolocation(uid, geotime, lat, lon)
          if (!success) {
            Ok("{\"505\":\"Failed to update geolocation\"}")
          }
        } catch {
          case e: Exception =>
          (Ok("{\"505\":\"Failed to update geolocation\"}"))
        }
      }
      Ok("{\"202\":\"Update successfully\"}")
  }


  def insertAction(uid:String) = Action {

    request =>
      val body: AnyContent = request.body
      var result :Boolean = true

      val jsonBody: Option[JsValue] = body.asJson

      jsonBody.map { jsValue =>

        val date:String = (jsValue \ "date").get.toString().replace("\"","") // 2015-10-26
        val c1 = (jsValue \ "c1").get.toString().replace("\"","")
        val c2 = (jsValue \ "c2").get.toString().replace("\"","")
        val c3 = (jsValue \ "c3").get.toString().replace("\"","")
        result = DataDao.insertActivities(uid, date, c1, c2, c3)
      }

      if(result) {
        Ok("{\"202\":\"Update successfully\"}")
      }else {
        Ok("{\"505\":\"Failed to update geolocation\"}")
      }
  }



}
