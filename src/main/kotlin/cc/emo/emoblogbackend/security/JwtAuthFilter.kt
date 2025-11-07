package cc.emo.emoblogbackend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthFilter(
    private val jwt: JwtService,
    private val uds: UserDetailsService
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        req: HttpServletRequest, res: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = req.getHeader("Authorization")
        if (header?.startsWith("Bearer ") == true &&
            SecurityContextHolder.getContext().authentication == null
        ) {
            val token = header.removePrefix("Bearer ").trim()
            val claims = runCatching { jwt.parseClaims(token) }
                .onFailure { log.debug("Failed to parse JWT: {}", it.message) }
                .getOrNull()

            if (claims != null && !jwt.isExpired(claims)) {
                runCatching { uds.loadUserByUsername(claims.subject) }
                    .onSuccess { userDetails ->
                        if (jwt.hasSubject(claims, userDetails.username)) {
                            val auth = UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.authorities
                            ).apply {
                                details = WebAuthenticationDetailsSource().buildDetails(req)
                            }
                            SecurityContextHolder.getContext().authentication = auth
                        }
                    }
                    .onFailure { log.debug("User lookup failed for JWT subject {}: {}", claims.subject, it.message) }
            }
        }
        chain.doFilter(req, res)
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.servletPath.startsWith("/auth/") || request.servletPath.startsWith("/actuator/health")
}
