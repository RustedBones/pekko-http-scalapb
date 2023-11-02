# pekko-http-scalapb

[![Continuous Integration](https://github.com/RustedBones/pekko-http-scalapb/actions/workflows/ci.yml/badge.svg)](https://github.com/RustedBones/pekko-http-scalapb/actions/workflows/ci.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/fr.davit/pekko-http-scalapb_3/badge.svg)](https://maven-badges.herokuapp.com/maven-central/fr.davit/pekko-http-scalapb_3)
[![Software License](https://img.shields.io/badge/license-Apache%202-brightgreen.svg?style=flat)](LICENSE)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

pekko-http protobuf and json marshalling/unmarshalling for ScalaPB messages

## Versions

| Version | Release date | Pekko Http version | ScalaPB version | Scala versions |
|---------|--------------|--------------------|-----------------|----------------|
| `x.x.x` | xxxx-xx-xx   | `x.x.x`            | `x.x.x`         | `x.x.x`        |

The complete list can be found in the [CHANGELOG](CHANGELOG.md) file.

## Getting pekko-http-scalapb

Libraries are published to Maven Central. Add to your `build.sbt`:

```sbt
libraryDependencies += "fr.davit" %% "pekko-http-scalapb" % <version> // binary & json support
```

## Quick start

For the examples, we are using the following proto domain model

```proto
message Item {
  string name = 1;
  int64 id    = 2;
}

message Order {
  reapeated Item items = 1;
}
```

The implicit marshallers and unmarshallers for your generated proto classes are defined in `ScalaPBSupport`. You
simply need to have them in scope.

```scala
import org.apache.pekko.http.scaladsl.server.Directives._
import fr.davit.pekko.http.scaladsl.marshallers.scalapb.ScalaPBSupport._

object MyProtoService {

  val route =
    get {
      pathSingleSlash {
        complete(Item("thing", 42))
      }
    } ~ post {
      entity(as[Order]) { order =>
        val itemsCount = order.items.size
        val itemNames = order.items.map(_.name).mkString(", ")
        complete(s"Ordered $itemsCount items: $itemNames")
      }
    }
}
```

Marshalling/Unmarshalling of the generated classes depends on the `Accept`/`Content-Type` header sent by the client:

- `Content-Type: application/json`: json
- `Content-Type: application/x-protobuf`: binary
- `Content-Type: application/x-protobuffer`: binary
- `Content-Type: application/protobuf`: binary
- `Content-Type: application/vnd.google.protobuf`: binary

No `Accept` header or matching several (eg `Accept: application/*`) will take the 1st matching type from the above list.

### Json only

If you are using scalaPB for json (un)marshalling only, you can use `ScalaPBJsonSupport` from the sub module

```sbt
libraryDependencies += "fr.davit" %% "pekko-http-scalapb-json4s" % <version> // json support only
```

### Binary only

If you are using scalaPB for binary (un)marshalling only, you can use `ScalaPBBinarySupport` form the sub module

```sbt
libraryDependencies += "fr.davit" %% "peko-http-scalapb-binary" % <version> // binary support only
```

## Limitation

Only json (un)marshallers are able to support collections of proto messages as root object.

Entity streaming (http chunked transfer) is at the moment not supported by the library.