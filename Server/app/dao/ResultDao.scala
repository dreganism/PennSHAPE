package dao

import models._

import scala.collection.mutable.ListBuffer

/**
 * Created by zeli on 10/18/15.
 */
object ResultDao {


  /*
    val result:Result = Result(true, )

    status: Boolean,
    uid: String,
    group: List[User],
    data: List[Data],
    messages: List[Message],
    chat: List[Message],
    config: Config
  */

/*
  def getResultByUserId(uid: String): List[JsObject] = {
    val groupusers: List[User] = UserDao.getGroupUsers(uid)
    val groupusersjson = UserFormat.getGroupUserList(groupusers)

    val messages: List[Message] = MessageDao.getMessageByUserId(uid)
    val messagesjson = MessageFormat.getJsonMessages(messages)

    val groupmessages: List[GroupMessage] = MessageDao.getGroupMessageByUserId(uid)
    val groupmessagesjson = MessageFormat.getJsonGroupMessages(groupmessages)

    val groupdata: Map[String, ListBuffer[Data]] = DataDao.getGroupDataByUserId(uid).toMap
    val groupdatajson = DataFormat.getJsonDataMap(groupdata)

    val config: Config = ConfigDao.getConfig()
    val configjson = ConfigFormat.getJsonConfig(config)

    val user = Json.obj("user" -> uid)

    val status = Json.obj("status" -> "true")

    List(groupusersjson, messagesjson, groupmessagesjson, groupdatajson, configjson, user, status)

  }
*/

  def getResultByUserId(uid: String): Result = {
    val groupusers: List[User] = UserDao.getGroupUsers(uid)

    //val messages: List[Message] = MessageDao.getMessageByUserId(uid)

    //val groupmessages: List[GroupMessage] = MessageDao.getGroupMessageByUserId(uid)

    val groupdata: Map[String, ListBuffer[Data]] = DataDao.getGroupDataByUserId(uid).toMap

    val config: Config = ConfigDao.getConfig()

    //val user = Json.obj("user" -> uid)

    //val status = Json.obj("status" -> "true")
    if(groupusers.size>0) {
      //Result("true", uid, groupusers, groupdata, messages, groupmessages, config)
      Result("true", uid, groupusers, groupdata, config)
    } else {
      Result("false",null,null,null,null);
    }


  }


}
