package models


/**
 * Created by zeli on 10/17/15.
 */

case class User(val uid: String,
                var email: String,
                var phone: String,
                var displayname: String,
                var age: Int,
                var height: String,
                var weight: String,
                var favorite: String,
                var pic: String
                 )

case class UserandGroup(val uid: String,
                var groupid: Int
               )


