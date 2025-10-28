package cc.emo.emoblogbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class Beans {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
