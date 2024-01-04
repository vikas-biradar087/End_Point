package com.example.api.service;

import com.example.api.entites.Post;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.payload.PostDto;
import com.example.api.payload.PostResponse;
import com.example.api.respository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepo;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepo,ModelMapper mapper)
    {
        this.postRepo = postRepo;
        this.mapper=mapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        // convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepo.save(post);

        // convert entity to DTO
        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    @Override
    public PostResponse fetchAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable=PageRequest.of(pageNo, pageSize,sort);
        Page<Post> post = postRepo.findAll(pageable);
        List<Post> content = post.getContent();
        List<PostDto> contents = content.stream().map(p->mapToDto(p)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(contents);
        postResponse.setPageNo(post.getNumber());
        postResponse.setPageSize(post.getSize());
        postResponse.setTotalPages(post.getTotalPages());
        postResponse.setTotalElement(post.getTotalElements());
        postResponse.setLast(post.isLast());

        return postResponse;

    }

    @Override
    public PostDto getpostById(long id) {
        Optional<Post> byId = postRepo.findById(id);
        Post post = byId.get();
        PostDto postDto = mapToDto(post);//convert entity to dto
        return postDto;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {

        // get post by id from the database
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepo.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deleteByPost(long id) {
        Post post = postRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepo.delete(post);
    }


    //convert dto object to entity object
    // convert DTO to entity
    public Post mapToEntity(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;

    }
    //convert entity to dto object
    public PostDto mapToDto(Post post) {
        PostDto dto = mapper.map(post, PostDto.class);
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
        return dto;
    }
    
    
}
