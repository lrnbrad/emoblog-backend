package cc.emo.emoblogbackend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
    override fun doFilterInternal(
        req: HttpServletRequest, res: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = req.getHeader("Authorization")
        if (header?.startsWith("Bearer ") == true &&
            SecurityContextHolder.getContext().authentication == null
        ) {
            val token = header.removePrefix("Bearer ").trim()
            runCatching { jwt.parseClaims(token) }
                .onSuccess { claims ->
                    val userDetails = uds.loadUserByUsername(claims.subject)
                    val auth = UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.authorities
                    )
                    auth.details = WebAuthenticationDetailsSource().buildDetails(req)
                    SecurityContextHolder.getContext().authentication = auth
                }
        }
        chain.doFilter(req, res)
    }

    override fun shouldNotFilter(request: HttpServletRequest) =
        request.servletPath.startsWith("/auth/") || request.servletPath.startsWith("/actuator/health")
}