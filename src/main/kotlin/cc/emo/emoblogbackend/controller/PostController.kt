package cc.emo.emoblogbackend.controller

import cc.emo.emoblogbackend.data.dto.*
import cc.emo.emoblogbackend.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as OpenApiRequestBody

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a post")
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Post created",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Post content cannot be blank"
        )
    )
    @OpenApiRequestBody(
        required = true,
        description = "Post content",
        content = [Content(
            mediaType = "application/json",
        )]
    )
    fun createPost(@RequestBody @Valid request: PostCreateRequest): PostResponse =
        postService.createPost(request)

    @GetMapping("/{postId}")
    @Operation(summary = "Get single post")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Post found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found"
        )
    )
    fun getPost(@PathVariable postId: Long): PostResponse =
        postService.getPost(postId)

    @GetMapping("author/{authorId}")
    @Operation(summary = "List posts by author")
    fun getPostsByAuthor(
        @PathVariable authorId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "CREATED_DESC") sort: Sort,
    ): Page<PostResponse> =
        postService.getPostsByAuthor(authorId, page, size, sort)

    @GetMapping("/feed")
    @Operation(summary = "Feed across all authors")
    fun getFeed(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "CREATED_DESC") sort: Sort,
    ): Page<PostResponse> = postService.getFeed(page, size, sort)

    @GetMapping("/me")
    @Operation(summary = "List my posts")
    fun getMyPosts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "CREATED_DESC") sort: Sort,
    ): Page<PostResponse> =
        postService.getMyPosts(page, size, sort)

    @PutMapping("/{postId}")
    @Operation(summary = "Update a post")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Post updated",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Post content cannot be blank"
        )
    )
    @OpenApiRequestBody(
        required = true,
        description = "Updated content",
        content = [Content(
            mediaType = "application/json",
        )]
    )
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody @Valid request: PostUpdateRequest
    ): PostResponse = postService.updatePost(postId, request)

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a post")
    fun deletePost(@PathVariable postId: Long) {
        postService.deletePost(postId)
    }

    @PostMapping("/{postId}/like")
    @Operation(summary = "Like a post")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Post liked",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found"
        )
    )
    fun likePost(@PathVariable postId: Long): PostResponse =
        postService.likePost(postId)

    @PostMapping("/{postId}/unlike")
    @Operation(summary = "Unlike a post")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Post unliked",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found"
        )
    )
    fun unlikePost(@PathVariable postId: Long): PostResponse =
        postService.unlikePost(postId)

    @GetMapping("/{postId}/comments")
    @Operation(summary = "List comments for a post")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Comments returned",
            content = [Content(
                mediaType = "application/json",
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found"
        )
    )
    fun getComments(
        @PathVariable postId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "CREATED_DESC") sort: Sort,
    ): Page<PostCommentResponse> =
        postService.getComments(postId, page, size, sort)

    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a comment to a post")
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Comment created",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostCommentResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Comment content cannot be blank"
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found"
        )
    )
    @OpenApiRequestBody(
        required = true,
        description = "Comment content",
        content = [Content(
            mediaType = "application/json",
        )]
    )
    fun addComment(
        @PathVariable postId: Long,
        @RequestBody @Valid request: PostCommentCreateRequest
    ): PostCommentResponse = postService.addComment(postId, request)

    @PutMapping("/comments/{commentId}")
    @Operation(summary = "Update a comment")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Comment updated",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostCommentResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Comment content cannot be blank"
        )
    )
    @OpenApiRequestBody(
        required = true,
        description = "Updated comment content",
        content = [Content(
            mediaType = "application/json",
        )]
    )
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody @Valid request: PostCommentUpdateRequest
    ): PostCommentResponse = postService.updateComment(commentId, request)

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a comment")
    fun deleteComment(@PathVariable commentId: Long) {
        postService.deleteComment(commentId)
    }

    @PostMapping("/comments/{commentId}/like")
    @Operation(summary = "Like a comment")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Comment liked",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostCommentResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Comment not found"
        )
    )
    fun likeComment(@PathVariable commentId: Long): PostCommentResponse =
        postService.likeComment(commentId)

    @PostMapping("/comments/{commentId}/unlike")
    @Operation(summary = "Unlike a comment")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Comment unliked",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = PostCommentResponse::class),
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Comment not found"
        )
    )
    fun unlikeComment(@PathVariable commentId: Long): PostCommentResponse =
        postService.unlikeComment(commentId)
}
