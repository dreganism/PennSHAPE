package dao

import anorm.SqlParser._
import anorm._
import models.{Data, UserData}
import org.joda.time.DateTime
import play.api.db.DB

import scala.collection.mutable.ListBuffer
import play.api.Play.current

/**
 * Created by zeli on 10/17/15.
 */
object DataDao {
  val userdata = {
    get[String]("uid") ~
      get[DateTime]("date") ~
      get[String]("c1") ~
      get[String]("c2") ~
      get[String]("c3") ~
      get[Int]("steps") ~
      get[Int]("cal") map {
      case uid ~ date ~ c1 ~ c2 ~ c3 ~ steps ~ cal
      => UserData(uid, date, c1, c2, c3, steps, cal)
    }
  }


  def getGroupUserDataByUserId(uid: String): List[UserData] = {
    DB.withConnection {
      implicit connection =>
        SQL(
          "select a.uid, a.date, a.c1, a.c2,a.c3,a.steps, a.calories as cal from activities a where uid in (" +
            "select uid from penngroup where groupid =  (select groupid from penngroup where uid={uid}))").on("uid" -> uid).as(userdata *)
    }
  }

  def getGroupDataByUserId(uid: String): scala.collection.mutable.Map[String, ListBuffer[Data]] = {

    val userdata: List[UserData] = getGroupUserDataByUserId(uid);

    var hashmap: scala.collection.mutable.Map[String, ListBuffer[Data]] = scala.collection.mutable.Map()

    for (u <- userdata) {
      if (hashmap.contains(u.uid)) {
        var datalist = hashmap(u.uid)
        datalist += Data(u.date, u.c1, u.c2, u.c3, u.steps, u.cal)
      } else {
        var newdatalist = ListBuffer[Data]()
        newdatalist += Data(u.date, u.c1, u.c2, u.c3, u.steps, u.cal)
        hashmap(u.uid) = newdatalist
      }
    }
    hashmap
  }

  def insertActivities (uid:String, date:String, c1:String, c2: String, c3:String):Boolean ={

    DB.withConnection { implicit c =>
      val count = SQL("""Select count(*) as code from activities c where c.uid = {uid} and c.date = {date};""")
        .on("uid"->uid, "date"->date).as(SqlParser.long("code").single)

      if (count ==0){
        val id =
          SQL("insert into activities(uid, date, c1, c2,c3) values ({uid}, {date}, {c1}, {c2}, {c3})")
            .on("uid" -> uid, "date"-> date, "c1"-> c1, "c2"->c2, "c3"-> c3).executeInsert()
      } else if(count ==1){
        val result = SQL("update activities set c1={c1}, c2={c2}, c3={c3} where uid = {uid} and date = {date}").
          on("c1"-> c1, "c2"->c2, "c3"-> c3,"uid" -> uid, "date"-> date)
          .executeUpdate()
      }
return true
    }



    return true

  }

}
