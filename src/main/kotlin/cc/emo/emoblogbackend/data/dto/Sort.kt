package cc.emo.emoblogbackend.data.dto

import org.springframework.data.domain.Sort

/**
 *  Whitelisted sorting options for posts so the API never exposes raw column names.
 */
enum class Sort(private val orders: List<Sort.Order>) {
    CREATED_DESC(listOf(Sort.Order.desc("createdAt"))),
    CREATED_ASC(listOf(Sort.Order.asc("createdAt"))),
    LIKE_DESC(listOf(Sort.Order.desc("likeCount"), Sort.Order.desc("createdAt"))),
    LIKE_ASC(listOf(Sort.Order.asc("likeCount"), Sort.Order.asc("createdAt")));

    fun toSort(): Sort = Sort.by(orders)
}
