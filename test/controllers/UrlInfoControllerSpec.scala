/*
 * Copyright (c) Sky Schulz 2017.
 */

package controllers

import akka.stream.Materializer
import models.Score
import org.scalatestplus.play._
import play.api.Application
import play.api.cache.CacheApi
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._

class UrlInfoControllerSpec extends PlaySpec with OneAppPerSuite {

  implicit override lazy val app: Application = new GuiceApplicationBuilder().configure(Map("ehcacheplugin" -> "enabled")).build()
  implicit lazy val materializer: Materializer = app.materializer

  import controllers.UrlInfoController._

  "OneAppPerSuite trait" must {
    "provide an Application with caching enabled" in {
      app.configuration.getString("ehcacheplugin") mustBe Some("enabled")
    }
  }

  "UrlInfoController GET" should {

    "return a score of zero for unknown URL" in {
      val controller = new UrlInfoController(app.injector.instanceOf(classOf[CacheApi]))
      val response = controller.get("www.unknown.com:80", "/").apply(FakeRequest())

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(""""score":0""")
    }

    "store a score for a given URL" in {
      val controller = new UrlInfoController(app.injector.instanceOf(classOf[CacheApi]))
      val response = Helpers.call(controller.post("www.known.com:80", "/"), FakeRequest(POST, "/www.known.com:80/").withJsonBody(Json.toJson(Score(1.0f))))

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(""""score":1""")
    }

    "return a valid score for a known URL" in {
      val controller = new UrlInfoController(app.injector.instanceOf(classOf[CacheApi]))
      val response = controller.get("www.known.com:80", "/").apply(FakeRequest())

      status(response) mustBe OK
      contentType(response) mustBe Some("application/json")
      contentAsString(response) must include(""""score":1""")
    }

  }
}
