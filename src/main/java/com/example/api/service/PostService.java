package com.example.api.service;

import com.example.api.payload.PostDto;
import com.example.api.payload.PostResponse;

import java.util.List;

public interface PostService {

    public PostDto createPost(PostDto postDto);

    public PostResponse fetchAll(int pageNo, int pageSize, String sortBy, String sortDir);

    public PostDto getpostById(long id);



    PostDto updatePost(PostDto postDto, long id);

    void deleteByPost(long id);


//    List<PostDto> fetchAll(int pageNo, int pageSize, String sortBy, String sortDir);
}
