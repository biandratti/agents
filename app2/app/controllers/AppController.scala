package controllers

import kamon.Kamon
import models.TraceResponse
import play.api.libs.json.Json
import play.api.mvc.{
  AbstractController,
  Action,
  AnyContent,
  ControllerComponents
}
import play.api.{Logging, MarkerContext}
import play.twirl.api.Html
import utils.RequestMarkerContext.requestHeaderToMarkerContext

import scala.concurrent.{ExecutionContext, Future}

class AppController(
    cc: ControllerComponents
)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with Logging {

  def trace(): Action[AnyContent] =
    Action.async { implicit request =>
      implicit val mc: MarkerContext =
        requestHeaderToMarkerContext(request.headers)
      logger.info("trace request")
      Future(
        Ok(Json.toJson(TraceResponse(Kamon.currentSpan().trace.id.string)))
      )
    }

  def index: Action[AnyContent] = Action {
    Ok(Html("<h1>Welcome</h1><p>APP2 is ready.</p>"))
  }

}
