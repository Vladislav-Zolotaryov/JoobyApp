package app

import io.jooby.StatusCode
import io.jooby.test.MockRouter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UnitTest {
  @Test
  fun welcome() {
    val router = MockRouter(App())
    router.get("/") { rsp ->
      assertEquals(StatusCode.NOT_FOUND, rsp.statusCode)
    }
  }
}
