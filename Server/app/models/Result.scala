package models

import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer


/**
 * Created by zeli on 10/17/15.
 */


case class Result(
                   status: String,
                   uid: String,
                   group: List[User],
                   data: Map[String, ListBuffer[Data]],
                   messages: List[Message],
                   chat: List[GroupMessage],
                   config: Config
                   )



/*
case class Result(
                   status: JsObject,
                   uid: JsObject,
                   group: JsObject,
                   data: JsObject,
                   messages: JsObject,
                   chat: JsObject,
                   config: JsObject
                   )
*/
case class Data(
                 date: DateTime,
                 c1: String,
                 c2: String,
                 c3: String,
                 steps: Int,
                 cal: Int
                 )


case class UserData(
                     uid: String,
                     date: DateTime,
                     c1: String,
                     c2: String,
                     c3: String,
                     steps: Int,
                     cal: Int
                     )

case class Message(mid: Int,
                   uid: String,
                   uidto: String,
                   time: DateTime,
                   msg: String,
                   mtype: Int
                    )


case class GroupMessage(mid: Int,
                        uid: String,
                        time: DateTime,
                        msg: String,
                        mtype: Int,
                        groupid: Int
                         )

case class Config(
                   start_date: DateTime,
                   end_date: DateTime
                   )

case class Group(
                  id: Int,
                  groupid: Int,
                  uid: String
                  )


case class Fail(
                 status: String,
                 code: String,
                 error: String
                 )

