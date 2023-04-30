package app

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jooby.Kooby
import io.jooby.body
import io.jooby.hikari.HikariModule
import io.jooby.jackson.JacksonModule
import io.jooby.jdbi.JdbiModule
import io.jooby.runApp
import org.jdbi.v3.core.Handle
import java.util.*


@JvmInline
value class Id(@JsonValue val value: String)

@JvmInline
value class Description(@JsonValue val value: String)

data class ItemView(
  val id: Id,
  val description: Description
)

data class ItemCreate(
  val description: String
)

class App : Kooby({
  install(HikariModule())
  install(JdbiModule())

  install(JacksonModule(jacksonObjectMapper()))

  require(Handle::class.java).use { handle ->
    handle.execute("CREATE TABLE IF NOT EXISTS items (id text primary key, description text)")
  }

  get("/item") {
    require(Handle::class.java).use { h ->
      h.createQuery("SELECT id, description FROM items")
        .map { row ->
          ItemView(
            id = Id(row.getColumn("id", String::class.java)),
            description = Description(row.getColumn("description", String::class.java))
          )
        }.list()
    }
  }

  post("/item") {
    val request = ctx.body<ItemCreate>()

    require(Handle::class.java).use { h ->
      h.prepareBatch("INSERT INTO items VALUES (:id, :description)")
        .bind("id", UUID.randomUUID())
        .bind("description", request.description)
        .execute()
    }
    true
  }
})

fun main(args: Array<String>) {
  runApp(args, App::class)
}
