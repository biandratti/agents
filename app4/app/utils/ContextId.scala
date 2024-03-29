package utils

import play.api.Logging
import play.api.mvc.Headers
import utils.ContextId.cId

import java.util.UUID

object ContextId {
  lazy val cId = "context-id" // TODO: choose correlation-id in all the solution
}

trait ContextId extends Logging {

  def getCtxId(requestHeader: Headers): String = {
    requestHeader
      .get(cId)
      .getOrElse({
        logger.warn(s"$cId is missing")
        UUID.randomUUID().toString
      })
  }
}
