package cc.emo.emoblogbackend.security

import cc.emo.emoblogbackend.data.`do`.UserDo
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserDetails(val user: UserDo) : UserDetails {
    override fun getUsername() = user.username
    override fun getPassword() = user.passwordHash
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    fun id(): Long = user.id
}