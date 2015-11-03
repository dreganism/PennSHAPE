package dao

import anorm.SqlParser._
import anorm._
import models.Config
import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current

/**
 * Created by zeli on 10/17/15.
 */
object ConfigDao {

  val configdata = {
    get[DateTime]("start_date") ~
      get[DateTime]("end_date") map {
      case start_date ~ end_date
      => Config(start_date, start_date)
    }
  }


  def getConfig(): Config = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from config").as(configdata *).apply(0)
    }
  }


}
