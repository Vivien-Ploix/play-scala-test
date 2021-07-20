package utilities
import java.time.Clock
import pdi.jwt.{JwtJson, JwtAlgorithm}
import play.api.libs.json.Json

class JWTUtility {

    val key = ""

    def generateToken(claim: String): String = {
        val header = Json.obj(("typ", "JWT"), ("alg", "HS256"))
        val token = JwtJson.encode(algo, claim, key)
    }
}   