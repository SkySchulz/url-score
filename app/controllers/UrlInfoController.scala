/*
 * Copyright (c) 2017 Sky Schulz.
 */

package controllers

import javax.inject.{Inject, Singleton}

import io.swagger.annotations._
import models.{Resource, ResourceScore, Score}
import play.api.mvc.{Action, AnyContent, BodyParsers, Controller}
import play.api.cache.CacheApi
import play.api.libs.json._

@Singleton
@Api(value = "/urlinfo/1")
class UrlInfoController @Inject()(cacheApi: CacheApi) extends Controller {

  import UrlInfoController._
  private def urlDecode(url: String) = java.net.URLDecoder.decode(url, "UTF-8")

  /**
    * Retrieve a score for a URL (hostAndPort + originalPath). If the URL is unknown, the score is 0.
    *
    * @param hostAndPort  the host[:port] of the URL (will default to port 80)
    * @param originalPath the /path of the URL
    * @return a ResourceScore, as JSON
    */
  @ApiOperation(value = "Retrieve a score for the URL. Unknown URLs have a score of 0.", response = classOf[ResourceScore])
  def get(
           @ApiParam(value = "host:port of URL to score") hostAndPort: String,
           @ApiParam(value = "path of URL to score") originalPath: String
         ): Action[AnyContent] = Action { implicit request =>
    // naive implementation: one score per fully-qualified URL
    val resource = Resource(hostAndPort, Option(urlDecode(originalPath)))
    val score: Float = cacheApi.get[Float](resource.toString).getOrElse(0.0f)
    Ok(Json.toJson(ResourceScore(resource, score)))
  }

  /**
    * Store a score for a URL (hostAndPort + originalPath).
    *
    * @param hostAndPort  the host[:port] of the URL (will default to port 80)
    * @param originalPath the /path of the URL
    * @return
    */
  @ApiOperation(value = "Store a score for the URL.", response = classOf[ResourceScore])
  @ApiResponses(Array(new ApiResponse(code = 400, message = "Bad Request")))
  @ApiImplicitParams(Array(new ApiImplicitParam(name = "body", value = "the score to assign URL", required = true, dataType = "models.Score", paramType = "body",
    examples = new Example(value = Array(new ExampleProperty(mediaType = "application/json", value = """{"score":0.9}"""))))))
  def post(
            @ApiParam(value = "host:port of URL to score") hostAndPort: String,
            @ApiParam(value = "path of URL to score") originalPath: String
          ): Action[JsValue] = Action(BodyParsers.parse.json) { implicit request =>
    val scoreResult = request.body.validate[Score]
    scoreResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "error", "message" -> JsError.toJson(errors)))
      },
      score => {
        val resource = Resource(hostAndPort, Option(urlDecode(originalPath)))
        cacheApi.set(resource.toString, score.score)
        Ok(Json.toJson(ResourceScore(resource, score.score)))
      }
    )
  }
}

object UrlInfoController {
  implicit val scoreFormats: OFormat[Score] = Json.format[Score]
  implicit val resourceWrites: OWrites[Resource] = Json.writes[Resource]
  implicit val resourceScoreWrites: OWrites[ResourceScore] = Json.writes[ResourceScore]
}