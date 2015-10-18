package controllers

import dao._
import models._
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
    Ok(UserFormat.getUserList("true", result))

  }

  def getGroupUsersById(id: String) = Action {
    val result: List[User] = UserDao.getGroupUsers(id)
    Ok(UserFormat.getGroupUserList(result))
  }


  def getMessagesByUserid(uid: String) = Action {
    val messages: List[Message] = MessageDao.getMessageByUserId(uid)
    Ok(MessageFormat.getJsonMessages(messages))

  }

  def getGroupMessagesByUserid(uid: String) = Action {
    val groupmessages: List[GroupMessage] = MessageDao.getGroupMessageByUserId(uid)
    Ok(MessageFormat.getJsonGroupMessages(groupmessages))

  }

  def getGroupDataByUserid(uid: String) = Action {
    val groupdata: Map[String, ListBuffer[Data]] = DataDao.getGroupDataByUserId(uid).toMap
    Ok(DataFormat.getJsonDataMap(groupdata))

  }

  def getConfig() = Action {
    val config: Config = ConfigDao.getConfig()
    Ok(ConfigFormat.getJsonConfig(config))

  }

  def getResult(uid:String) = Action {
    val res = ResultDao.getResultByUserId(uid)
    Ok(ResultFormat.getResultJson(res))
  }

}
