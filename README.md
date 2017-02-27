# url-score
Manage and report the "risk" score of public URLs. 

A risk score of 0 is considered a safe URL. Any non-zero risk score is considered an unsafe URL.

Unknown (or unregistered) URLs always have a default score of 0.

## Features
* GET to retrieve a URL risk score
* POST to update a URL risk score

## Dependencies

* [Java SE 8u121 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or later
* [sbt 0.13.13](http://www.scala-sbt.org) or later

## Building and Running

To build and run a local instance:

  ```$ sbt run```

Once running, Swagger documentation is at [http://localhost:9000/docs](http://localhost:9000/docs).

## Deployment

### AWS Elastic Beanstalk
To build and package a zip for AWS Elastic Beanstalk:

  ```$ sbt elastic-beanstalk:dist```
  
  See https://github.com/kipsigman/sbt-elastic-beanstalk for details.

### Docker
To build a docker instance:

```$ sbt docker:publishLocal```

See http://www.scala-sbt.org/sbt-native-packager/formats/docker.html for details.

## Planned
* Persistent back-end to store URL scores
* Horizontal scaling
