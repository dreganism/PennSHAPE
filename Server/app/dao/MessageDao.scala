package dao

import anorm.SqlParser._
import anorm._
import models.{GroupMessage, Message}
import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current

import scala.util.Try

/**
 * Created by zeli on 10/17/15.
 */
object MessageDao {

  val message = {
      get[Long]("id") ~
      get[String]("uid") ~
      get[String]("to") ~
      get[DateTime]("time") ~
      get[String]("msg") map {
      case id ~ uid ~ to ~ time ~ msg
      => Message(id, uid, to, time, msg)
    }
  }

  val groupmessage = {
      get[Long]("id") ~
      get[String]("uid") ~
      get[DateTime]("time") ~
      get[String]("msg") ~
      get[Int]("groupid") map {
      case id ~ uid ~ time ~ msg ~ groupid
      => GroupMessage(id, uid, time, msg, groupid)
    }
  }


  def insertMessage(uid:String, time:String, to:String, message:String): Option[Long] = {
    val id:Option[Long]=DB.withConnection { implicit c =>
        Try(SQL("insert into message(uid, time, to, msg) values ({uid}, {time}, {to}, {message})")
          .on("uid" -> uid, "date" -> time, "to" -> to, "message" -> message).executeInsert()).getOrElse(return Some(-1))
    }
    id
  }

  def insertGroupMessage(uid:String, time:String, message:String, groupid:String): Option[Long] = {
    val id:Option[Long]=DB.withConnection { implicit c =>
      Try(SQL("insert into groupmessage(uid, time, groupid, msg) values ({uid}, {time}, {groupid}, {message})")
        .on("uid" -> uid, "time" -> time, "groupid" -> groupid, "message" -> message).executeInsert()).getOrElse(return Some(-1) )
    }
    id
  }


  def getMessageByUserId(uid: String): List[Message] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from message where uid = {uid} union select * from message where to= {uid}").on("uid" -> uid).as(message *)
    }
  }

// -2 denote admin message
  def getGroupMessageByGroupIdForGroup(groupid: Int, fromvalue : Int): List[GroupMessage] = {

    DB.withConnection {
      implicit connection =>
        SQL("select a.id, a.uid, a.time,a.msg,a.groupid from groupmessage a where groupid in ( {groupid}, 0) and id > {fromvalue} order by id ").on("groupid" -> groupid, "fromvalue" -> fromvalue).as(groupmessage *)
    }
  }

  def getGroupMessageByGroupIdForNonGroup(fromvalue : Int): List[GroupMessage] = {

    DB.withConnection {
      implicit connection =>
        SQL("select a.id, a.uid, a.time,a.msg,a.groupid from groupmessage a where groupid = 0 and id > {fromvalue} order by id ").on("fromvalue" -> fromvalue).as(groupmessage *)
    }
  }

}
