package app

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jooby.StatusCode
import io.jooby.test.JoobyTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

@JoobyTest(App::class)
class IntegrationTest {

  companion object {
    val client = OkHttpClient()
    val mapper = jacksonObjectMapper()
  }

  /**
   * Need to set up DB and cleanup
   */

  @Test
  @Order(1)
  fun createsItems(serverPort: Int) {
    val req = Request.Builder()
      .url("http://localhost:$serverPort/item")
      .post(mapper.writeValueAsString(ItemCreate("test_me")).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
      .build()

    client.newCall(req).execute().use { rsp ->
      assertEquals(StatusCode.OK.value(), rsp.code)
    }
  }

  @Test
  @Order(2)
  fun viewsItems(serverPort: Int) {
    val req = Request.Builder()
      .url("http://localhost:$serverPort/item")
      .build()

    client.newCall(req).execute().use { rsp ->
      assertEquals(StatusCode.OK.value(), rsp.code)
      assertEquals(listOf(ItemView(Id("1"), Description("test_me"))), mapper.readValue<List<ItemView>>(rsp.body!!.string()))
    }
  }
}
