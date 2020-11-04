package com.froedevrolijk.api.db.command

import cats.effect.{ IO, Resource }
import com.froedevrolijk.api.db.datamodels.City
import skunk.codec.all.{ varchar, _ }
import skunk.implicits._
import skunk.{ Command, Query, Session, Void }

object DBCommands {

  val insertOne: Command[City] =
    sql"INSERT INTO city VALUES ($int4, $varchar, $bpchar, $varchar, $int4)".command
      .gcontramap[City]

  def insertMultiple(ps: List[City]): Command[ps.type] = {
    val enc = (int4 ~ varchar ~ bpchar(3) ~ varchar ~ int4).gcontramap[City].values.list(ps)
    sql"INSERT INTO city VALUES $enc".command
  }

  val all: Query[Void, City] =
    sql"SELECT id, name, countrycode, district, population FROM city"
      .query(int4 ~ varchar ~ bpchar(3) ~ varchar ~ int4)
      .gmap[City]

}
