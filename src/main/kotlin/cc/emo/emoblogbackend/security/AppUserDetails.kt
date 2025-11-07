package cc.emo.emoblogbackend.security

import cc.emo.emoblogbackend.data.`do`.UserDo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserDetails(
    val user: UserDo,
    private val authorities: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
) : UserDetails {
    override fun getUsername() = user.username
    override fun getPassword() = user.passwordHash
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    fun id(): Long = user.id

    companion object {
        fun from(user: UserDo, roles: Collection<String> = listOf("ROLE_USER")): AppUserDetails {
            val granted = roles.map(::SimpleGrantedAuthority)
            return AppUserDetails(user, granted)
        }
    }
}
