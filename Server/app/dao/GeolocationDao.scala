package dao

import java.text.SimpleDateFormat
import java.util.Calendar

import anorm._
import play.api.db.DB
import play.api.Play.current

import scala.util.Try

/**
  * Created by zeli on 11/7/15.
  */
object GeolocationDao {

  def insertGeolocation(uid: String, geotime: String, lat: String, lon: String): Boolean = {

    val today = Calendar.getInstance().getTime()
    val minuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentTime = minuteFormat.format(today)

    DB.withConnection { implicit c =>
      val id =
        Try(SQL("insert into geolocation(uid, geotime, lat, lon, updatets) values ({uid}, {geotime}, {lat}, {lon}, {updatets})")
          .on("uid" -> uid, "geotime" -> geotime, "lat" -> lat.toDouble, "lon" -> lon.toDouble, "updatets" -> currentTime).executeInsert()).getOrElse(return false)
    }

    return true;
  }
}