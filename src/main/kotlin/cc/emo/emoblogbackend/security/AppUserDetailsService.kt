package cc.emo.emoblogbackend.security

import cc.emo.emoblogbackend.data.dao.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AppUserDetailsService(private val users: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        users.findByUsername(username)
            ?.let { AppUserDetails.from(it) }
            ?: throw UsernameNotFoundException("User not found: $username")
}
