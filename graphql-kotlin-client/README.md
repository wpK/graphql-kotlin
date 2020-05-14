# GraphQL Kotlin Client
[![Maven Central](https://img.shields.io/maven-central/v/com.expediagroup/graphql-kotlin-client.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.expediagroup%22%20AND%20a:%22graphql-kotlin-client%22)
[![Javadocs](https://img.shields.io/maven-central/v/com.expediagroup/graphql-kotlin-client.svg?label=javadoc&colorB=brightgreen)](https://www.javadoc.io/doc/com.expediagroup/graphql-kotlin-client)

A lightweight, typesafe GraphQL HTTP client.

NOTE: GraphQL client can be invoked directly by manually creating your corresponding data model but since at its core it
ends up with simple POST request this mode of operation is not that useful. Instead, GraphQL Kotlin client should be used
together with one of the GraphQL Kotlin build plugins to auto-generate type safe data models based on the specified queries.

## Features

* Supports query and mutation operations
* Automatic generation of type-safe Kotlin models
* Custom scalar support - defaults to String but can be configured to deserialize to specific types
* Supports default enum values to gracefully handle new/unknown server values
* Native support for coroutines
* Easily configurable Ktor HTTP Client

## Install it

Using a JVM dependency manager, link `graphql-kotlin-client` to your project.

With Maven:

```xml
<dependency>
  <groupId>com.expediagroup</groupId>
  <artifactId>graphql-kotlin-client</artifactId>
  <version>${latestVersion}</version>
</dependency>
```

With Gradle (example using kts):

```kotlin
implementation("com.expediagroup:graphql-kotlin-client:$latestVersion")
```

## Use it

### Download Schema

Before we can auto-generate the data model for our GraphQL queries we first need to obtain a copy of a corresponding
GraphQL schema. Do this by running one of the tasks supplied by the GraphQL Kotlin Gradle plugin through your
CLI:

```bash
./gradlew graphqlIntrospectSchema --endpoint="http://localhost:8080/graphql"
```

This runs an introspection query against the target GraphQL server and saves the resulting schema in the `schema.graphql` file
under the build directory. GraphQL schemas can change over time so you should configure this task to run
as part of your build/release process.

```kotlin
// build.gradle.kts
import com.expediagroup.graphql.plugin.gradle.tasks.GraphQLGenerateClientTask

val graphqlIntrospectSchema by tasks.getting(GraphQLIntrospectSchemaTask::class) {
    endpoint.set("http://localhost:8080/graphql")
}
```

### Write Queries

When creating your GraphQL queries make sure to always specify an operation name, and name the files accordingly. Each one
of your query files will result in a generation of a corresponding Kotlin file with a class matching your operation name.
Generated top level class will contain all your data classes. For example, given `MyAwesomeGraphQLQuery.graphql` with
`myAwesomeQuery` as the operation name, GraphQL Kotlin plugins will generate a corresponding `MyAwesomeGraphQLQuery.kt`
file with a `MyAwesomeQuery` class under the configured package.

Refer to our [documentation](https://expediagroup.github.io/graphql-kotlin) for additional details and considerations
while writing your GraphQL queries.

### Generate Client

GraphQL Kotlin build plugins will auto-generate your data classes based on your queries and the underlying GraphQL schema.
In order to generate your client you will need to specify the target package name, schema file, and queries. If the queries
parameter is omitted, it will default to using `*.graphql` files under your resources directory.

```kotlin
val graphqlGenerateClient by tasks.getting(GraphQLGenerateClientTask::class) {
    packageName.set("com.expediagroup.graphql.generated")
    // use schema file generated by the introspection task
    schemaFile.set(graphqlIntrospectSchema.outputFile)
    // make sure to run client generation after introspection task
    dependsOn("graphqlIntrospectSchema")
}
```

Additional information about Gradle and Maven plugins as well as their respective tasks/mojos can be found in our
[documentation](https://expediagroup.github.io/graphql-kotlin/docs/plugins/gradle-plugin).

### Execute Queries

Your auto generated classes accept an instance of `GraphQLClient` which requires the target URL to be specified. `GraphQLClient`
uses the Ktor HTTP client to execute the underlying queries and allows you to specify different engines (defaults to CIO) and
HTTP client features. Please refer to [Ktor HTTP client documentation](https://ktor.io/clients/index.html) for additional
details.

```kotlin
val client = GraphQLClient(url = URL("http://localhost:8080/graphql"))
val query = MyAwesomeQuery(client)
val result = query.myAwesomeQuery()
```

The result of your query is a type safe object that corresponds to your GraphQL query.

Additional information about Gradle and Maven plugins as well as their respective tasks/mojos can be found in our
[documentation](https://expediagroup.github.io/graphql-kotlin/docs/plugins/gradle-plugin).

## Documentation

Additional information can be found in our [documentation](https://expediagroup.github.io/graphql-kotlin) and the
[Javadocs](https://www.javadoc.io/doc/com.expediagroup/graphql-kotlin-client) of all published versions.

If you have a question about something you can not find in our documentation or Javadocs, feel free to
[create an issue](https://github.com/ExpediaGroup/graphql-kotlin/issues) and tag it with the question label.