package com.example.api.controller;

import com.example.api.payload.CommentDto;
import com.example.api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long postId,
                                                    @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    //localhost:8080/api/post/1/comments
    @GetMapping("posts/{postId}/comments")
    public List<CommentDto> getAllCoomentsByPostId(@PathVariable("postId") long postId){

        List<CommentDto> dto=commentService.getCommentByPostId(postId);
        return dto;
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "id") Long commentId,
                                                    @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") long postId,

                                               @PathVariable("id") long commentId

                                                ){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("comment is deleted" ,HttpStatus.OK);

    }


}
