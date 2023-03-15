import _root_.controllers.AssetsComponents
import com.softwaremill.macwire.*
import kamon.Kamon
import play.api.ApplicationLoader.Context
import play.api.*
import play.api.i18n.*
import play.api.libs.ws.WSClient
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import router.Routes

/** Application loader that wires up the application dependencies using Macwire
  */
class TraceApplicationLoader extends ApplicationLoader {
  def load(context: Context): Application = {
    Kamon.initWithoutAttaching(context.initialConfiguration.underlying)
    context.lifecycle.addStopHook { () =>
      Kamon.stop()
    }
    new TraceComponents(
      context
    ).application
  }
}

class TraceComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with AhcWSComponents
    with TraceModule
    with AssetsComponents
    with I18nComponents
    with play.filters.HttpFiltersComponents {

  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }

  override def ws: WSClient = wsClient
}