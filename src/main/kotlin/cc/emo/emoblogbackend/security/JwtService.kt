package cc.emo.emoblogbackend.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class JwtService(
    @param:Value("\${security.jwt.secret}") private val secret: String,
    @param:Value("\${security.jwt.expMinutes}") private val expMinutes: Long
) {
    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun generate(username: String, claims: Map<String, Any>): String =
        Jwts.builder()
            .subject(username)
            .claims(claims)
            .issuedAt(Date.from(now()))
            .expiration(Date.from(now().plusSeconds(expMinutes * 60)))
            .signWith(key)
            .compact()

    fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

    fun isExpired(claims: Claims): Boolean =
        claims.expiration?.before(Date.from(now())) ?: true

    fun hasSubject(claims: Claims, expectedUsername: String): Boolean =
        claims.subject == expectedUsername

    private fun now(): Instant = Instant.now()
}
