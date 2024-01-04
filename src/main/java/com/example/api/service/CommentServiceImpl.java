package com.example.api.service;
import com.example.api.entites.Comment;
import com.example.api.entites.Post;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.payload.CommentDto;
import com.example.api.respository.CommentRepository;
import com.example.api.respository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService{


    private CommentRepository commentRepo;
    private PostRepository postRepo;

    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepo, PostRepository postRepo,ModelMapper mapper) {
        super();
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
        this.mapper=mapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment =  commentRepo.save(comment);

        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        return comments.stream().map(x->mapToDTO(x)).collect(Collectors.toList());

    }

    @Override
    public CommentDto updateComment(long postId, long id, CommentDto commentDto) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post", "id", postId)
        );

        Comment comment = commentRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", id)
        );

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepo.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("post","id",postId)
        );

        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("comment", "id", commentId)
        );
    }

    private CommentDto mapToDTO(Comment comment){
//        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
//        CommentDto commentDto = new CommentDto();
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return  commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
//        Comment comment = mapper.map(commentDto, Comment.class);
        Comment comment = mapper.map(commentDto, Comment.class);
//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return  comment;
    }
}
