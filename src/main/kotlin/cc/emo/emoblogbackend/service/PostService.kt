package cc.emo.emoblogbackend.service

import cc.emo.emoblogbackend.data.adapter.toPostCommentResponse
import cc.emo.emoblogbackend.data.adapter.toPostResponse
import cc.emo.emoblogbackend.data.dao.PostCommentLikeRepository
import cc.emo.emoblogbackend.data.dao.PostCommentRepository
import cc.emo.emoblogbackend.data.dao.PostLikeRepository
import cc.emo.emoblogbackend.data.dao.PostRepository
import cc.emo.emoblogbackend.data.`do`.PostCommentDo
import cc.emo.emoblogbackend.data.`do`.PostCommentLikeDo
import cc.emo.emoblogbackend.data.`do`.PostDo
import cc.emo.emoblogbackend.data.`do`.PostLikeDo
import cc.emo.emoblogbackend.data.dto.*
import cc.emo.emoblogbackend.security.AppUserDetails
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@Service
class PostService(
    private val posts: PostRepository,
    private val comments: PostCommentRepository,
    private val commentLikes: PostCommentLikeRepository,
    private val postLikes: PostLikeRepository,
) {

    /**
     * Creates a new post for the authenticated user.
     */
    @Transactional
    fun createPost(request: PostCreateRequest): PostResponse {
        if (request.content.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content cannot be blank")
        }

        val currentUser = requireAuthenticatedUser()
        val entity = PostDo(
            author = currentUser.user,
            content = request.content.trim()
        )
        val saved = posts.saveAndFlush(entity)
        return saved.toPostResponse(commentCount = 0)
    }

    fun getPostsByAuthor(authorId: Long, page: Int, size: Int, sort: Sort): Page<PostResponse> {
        requireAuthenticatedUser()
        val pageable = PageRequest.of(page, size, sort.toSort())
        return posts.findAllByAuthorId(authorId, pageable)
            .map { it.toPostResponse() }
    }

    /**
     * Returns a paged feed across all authors for infinite-scroll style pagination.
     */
    fun getFeed(page: Int, size: Int, sort: Sort): Page<PostResponse> {
        requireAuthenticatedUser()
        val pageable = PageRequest.of(page, size, sort.toSort())
        return posts.findAll(pageable)
            .map { it.toPostResponse() }
    }

    fun getMyPosts(page: Int, size: Int, sort: Sort): Page<PostResponse> {
        val currentUser = requireAuthenticatedUser()
        val pageable = PageRequest.of(page, size, sort.toSort())
        return posts.findAllByAuthorId(currentUser.id(), pageable)
            .map { it.toPostResponse() }
    }

    fun getPost(postId: Long): PostResponse {
        requireAuthenticatedUser()
        val post = posts
            .findById(postId).orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
            }
        return post.toPostResponse()
    }

    @Transactional
    fun updatePost(postId: Long, request: PostUpdateRequest): PostResponse {
        if (request.content.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Post content cannot be blank")
        }

        val currentUser = requireAuthenticatedUser()
        val post = posts.findByIdAndAuthorId(postId, currentUser.id())
            .orElseThrow { AccessDeniedException("You are not allowed to update this post") }

        post.content = request.content.trim()
        post.updatedAt = LocalDateTime.now()

        return post.toPostResponse()
    }

    @Transactional
    fun deletePost(postId: Long) {
        val currentUser = requireAuthenticatedUser()
        val post = posts.findByIdAndAuthorId(postId, currentUser.id())
            .orElseThrow { AccessDeniedException("You are not allowed to delete this post") }

        posts.delete(post)
    }

    @Transactional
    fun likePost(postId: Long): PostResponse {
        val currentUser = requireAuthenticatedUser()
        val post = posts.findByIdForUpdate(postId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found") }

        if (postLikes.existsByPostIdAndUserId(postId, currentUser.id())) {
            return post.toPostResponse()
        }

        postLikes.saveAndFlush(
            PostLikeDo(
                post = post,
                user = currentUser.user
            )
        )
        post.likeCount += 1
        post.updatedAt = LocalDateTime.now()

        return post.toPostResponse()
    }

    @Transactional
    fun unlikePost(postId: Long): PostResponse {
        val currentUser = requireAuthenticatedUser()
        val post = posts.findByIdForUpdate(postId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found") }

        val removed = postLikes.deleteByPostIdAndUserId(postId, currentUser.id())
        if (removed > 0 && post.likeCount > 0) {
            post.likeCount -= 1
            post.updatedAt = LocalDateTime.now()
        }

        return post.toPostResponse()
    }

    fun getComments(postId: Long, page: Int, size: Int, sort: Sort): Page<PostCommentResponse> {
        requireAuthenticatedUser()
        ensurePostExists(postId)
        val pageable = PageRequest.of(page, size, sort.toSort())
        return comments.findAllByPostId(postId, pageable)
            .map { it.toPostCommentResponse() }
    }

    @Transactional
    fun addComment(postId: Long, request: PostCommentCreateRequest): PostCommentResponse {
        if (request.content.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment content cannot be blank")
        }

        val currentUser = requireAuthenticatedUser()
        val post = posts.findById(postId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        }

        val comment = PostCommentDo(
            post = post,
            author = currentUser.user,
            content = request.content.trim()
        )

        val saved = comments.saveAndFlush(comment)
        post.updatedAt = LocalDateTime.now()

        return saved.toPostCommentResponse(post.id)
    }

    @Transactional
    fun updateComment(commentId: Long, request: PostCommentUpdateRequest): PostCommentResponse {
        if (request.content.isBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment content cannot be blank")
        }

        val currentUser = requireAuthenticatedUser()
        val comment = comments.findByIdAndAuthorId(commentId, currentUser.id())
            .orElseThrow { AccessDeniedException("You are not allowed to update this comment") }

        comment.content = request.content.trim()
        comment.updatedAt = LocalDateTime.now()

        return comment.toPostCommentResponse(comment.post.id)
    }

    @Transactional
    fun deleteComment(commentId: Long) {
        val currentUser = requireAuthenticatedUser()
        val comment = comments.findByIdAndAuthorId(commentId, currentUser.id())
            .orElseThrow { AccessDeniedException("You are not allowed to delete this comment") }

        comments.delete(comment)
    }

    @Transactional
    fun likeComment(commentId: Long): PostCommentResponse {
        val currentUser = requireAuthenticatedUser()
        val comment = comments.findByIdForUpdate(commentId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found") }

        if (commentLikes.existsByCommentIdAndUserId(commentId, currentUser.id())) {
            return comment.toPostCommentResponse(comment.post.id)
        }

        commentLikes.saveAndFlush(
            PostCommentLikeDo(
                comment = comment,
                user = currentUser.user
            )
        )
        comment.likeCount += 1
        comment.updatedAt = LocalDateTime.now()

        return comment.toPostCommentResponse(comment.post.id)
    }

    @Transactional
    fun unlikeComment(commentId: Long): PostCommentResponse {
        val currentUser = requireAuthenticatedUser()
        val comment = comments.findByIdForUpdate(commentId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found") }

        val removed = commentLikes.deleteByCommentIdAndUserId(commentId, currentUser.id())
        if (removed > 0 && comment.likeCount > 0) {
            comment.likeCount -= 1
            comment.updatedAt = LocalDateTime.now()
        }

        return comment.toPostCommentResponse(comment.post.id)
    }

    private fun ensurePostExists(postId: Long) {
        if (!posts.existsById(postId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        }
    }

    private fun requireAuthenticatedUser(): AppUserDetails {
        val context = SecurityContextHolder.getContext()
        val authentication = context.authentication
        if (authentication?.isAuthenticated != true || authentication.principal !is AppUserDetails) {
            throw AccessDeniedException("Authentication is required")
        }
        return authentication.principal as AppUserDetails
    }
}
