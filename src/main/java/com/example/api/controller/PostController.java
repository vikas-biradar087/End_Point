package com.example.api.controller;

import com.example.api.payload.PostDto;
import com.example.api.payload.PostResponse;
import com.example.api.service.PostService;
import com.example.api.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //post data in database//
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){

            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    //Fetch All the record//
    @GetMapping
    public PostResponse fetchAll(

            @RequestParam(value="pageNo",defaultValue= AppConstants.DEFAULT_PAGE_NUMBER ,required = false) int pageNo,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORTBY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ){
        return postService.fetchAll(pageNo,pageSize,sortBy,sortDir);

    }
    //get record based on id//
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getpostById(@PathVariable("id") long id){
         return ResponseEntity.ok(postService.getpostById(id));

    }

    // update post by id rest api
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable(name = "id") long id){

        PostDto postResponse = postService.updatePost(postDto, id);

        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByPost(@PathVariable("id") long id){
        postService.deleteByPost(id);
        return new ResponseEntity<>("Post deleted successfully.", HttpStatus.OK);
    }

}
