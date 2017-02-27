/*
 * Copyright (c) Sky Schulz 2017.
 */

package models

// a scored resource
case class ResourceScore(resource: Resource, score: Float)

case class Resource(host: String, port: Int = 80, path: Option[String] = None) {
  override def toString: String = s"""$host:$port${path.getOrElse("/")}"""
}

object Resource {
  def apply(hostAndPort: String, path: Option[String]): Resource = {
    val (host, port) = hostAndPort.split(":") match {
      case Array(h, p) => (h, p.toInt)
      case Array(h) => (h, 80)
    }
    new Resource(host, port, path)
  }

}
