package com.froedevrolijk.api.db.queries

import com.froedevrolijk.api.db.datamodels.{ CityName, Country }
import skunk.Query
import skunk.codec.all.{ bpchar, text, varchar }

object DBQueries {

  val cities: Query[String, CityName] =
    sql"""
    SELECT  name
    FROM    city
    WHERE   countrycode like $text
    LIMIT   10
    """.query(varchar).gmap[CityName]

  val countries: Query[String, Country] =
    sql"""
      SELECT name, code, population
      FROM   country
      WHERE  name like $text
    """
      .query(varchar ~ bpchar(3) ~ int4)
      .gmap[Country]
}
