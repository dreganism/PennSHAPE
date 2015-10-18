package dao

import anorm.SqlParser._
import anorm.~
import models.Group

/**
 * Created by zeli on 10/17/15.
 */
object GroupDao {

  val group = {
    get[Int]("id") ~
      get[Int]("groupid") ~
      get[String]("uid") map {
      case id ~ groupid ~ uid
      => Group(id, groupid, uid)
    }
  }
}
