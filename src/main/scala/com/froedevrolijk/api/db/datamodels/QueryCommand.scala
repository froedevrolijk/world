package com.froedevrolijk.api.db.datamodels

case class QueryCommand(req: Option[String])

case class QueryCommandList(req: Option[List[String]])
