package dao

import anorm.SqlParser._
import anorm._
import models.{GroupMessage, Message}
import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current

/**
 * Created by zeli on 10/17/15.
 */
object MessageDao {

  val message = {
    get[Int]("mid") ~
      get[String]("uid") ~
      get[String]("uidto") ~
      get[DateTime]("time") ~
      get[String]("msg") ~
      get[Int]("mtype") map {
      case mid ~ uid ~ uidto ~ time ~ msg ~ mtype
      => Message(mid, uid, uidto, time, msg, mtype)
    }
  }

  val groupmessage = {
    get[Int]("mid") ~
      get[String]("uid") ~
      get[DateTime]("time") ~
      get[String]("msg") ~
      get[Int]("mtype") ~
      get[Int]("groupid") map {
      case mid ~ uid ~ time ~ msg ~ mtype ~ groupid
      => GroupMessage(mid, uid, time, msg, mtype, groupid)
    }
  }


  def getMessageByUserId(uid: String): List[Message] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from message where uid = {uid} union select * from message where uidto= {uid}").on("uid" -> uid).as(message *)
    }
  }


  def getGroupMessageByUserId(uid: String): List[GroupMessage] = {
    DB.withConnection {
      implicit connection =>
        SQL("select a.mid, a.uid, a.mtype, a.time,a.msg,a.groupid from groupmessage " +
          "a join penngroup b on a.groupid = b.groupid where b.uid = {uid}").on("uid" -> uid).as(groupmessage *)
    }
  }

}
