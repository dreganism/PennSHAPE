package dao


import java.text.SimpleDateFormat
import java.util.Calendar

import anorm.SqlParser._
import anorm._
import models.User
import play.api.Play.current
import play.api.db._

/**
 * Created by zeli on 10/17/15.
 */
object UserDao {

  // a parser that will transform a JDBC ResultSet row to a usr value
  // uses the Parser API
  // <a href="http://www.playframework.org/documentation/2.0/ScalaAnorm" title="http://www.playframework.org/documentation/2.0/ScalaAnorm">http://www.playframework.org/documentation/2.0/ScalaAnorm</a>
  // these names need to match the field names in the 'user' database table
  val user = {
    get[String]("uid") ~
      get[String]("email") ~
      get[String]("phone") ~
      get[String]("displayname") ~
      get[Int]("age") ~
      get[String]("height") ~
      get[String]("weight") ~
      get[String]("favorite") ~
      get[String]("pic") map {
      case uid ~ email ~ phone ~ displayname ~ age ~ height ~ weight ~ favorite ~ pic
      => User(uid, email, phone, displayname, age, height, weight, favorite, pic)
    }
  }

  def selectAll(): List[User] = DB.withConnection { implicit c =>
    SQL("select * from User order by user_id asc").as(user *)
  }

  def getGroupUserById(uid: String): List[User] = {
    DB.withConnection {
      implicit connection =>
        SQL("select a.uid, a.email, a.phone, a.displayname,a.age,a.height, a.weight, a.favorite, a.pic from user a where uid in (" +
          "select uid from penngroup where groupid =  " +
          "(select groupid from penngroup where uid={uid}))").on("uid" -> uid).as(user *)
    }
  }

  def setFavorite(uid :String, favorite :String): Boolean = {

    val today = Calendar.getInstance().getTime()
    val minuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentTime = minuteFormat.format(today)

    DB.withConnection { implicit c =>try{
        SQL("update User set favorite={favorite}, update_ts={updatets} where uid = {uid}")
          .on("favorite" -> favorite, "updatets" -> currentTime, "uid" -> uid ).executeUpdate()} catch {
      case e: Exception =>
        e.printStackTrace()
        return false;
      }
    }
    return true;
  }


  def getGroupUsers(uid: String): List[User] = {

    val users: List[User] = getGroupUserById(uid);
    users
  }

  def getUseridByEmail(email: String): Option[String] = {
    DB.withConnection {
          println ("LLL:"+email)
      implicit connection =>
        SQL("select a.uid from user a where a.email={email}").on("email" -> email).as(SqlParser.str("uid").singleOpt)
    }
  }

}