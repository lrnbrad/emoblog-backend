package cc.emo.emoblogbackend.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO

@Configuration
/**
 * Serializing PageImpl instances as-is is not supported,
 * meaning that there is no guarantee about the stability of
 * the resulting JSON structure! For a stable JSON structure,
 * please use Spring Data's PagedModel (globally via @EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO))
 */
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
class WebConfig
