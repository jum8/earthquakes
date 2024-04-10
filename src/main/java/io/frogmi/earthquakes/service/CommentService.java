package io.frogmi.earthquakes.service;

import io.frogmi.earthquakes.domain.Comment;
import io.frogmi.earthquakes.domain.Feature;
import io.frogmi.earthquakes.model.CommentDTO;
import io.frogmi.earthquakes.repos.CommentRepository;
import io.frogmi.earthquakes.repos.FeatureRepository;
import io.frogmi.earthquakes.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeatureRepository featureRepository;

    public CommentService(final CommentRepository commentRepository,
                          final FeatureRepository featureRepository) {
        this.commentRepository = commentRepository;
        this.featureRepository = featureRepository;
    }

    public List<CommentDTO> findAll() {
        final List<Comment> comments = commentRepository.findAll(Sort.by("id"));
        return comments.stream()
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .toList();
    }

    public CommentDTO get(final Long id) {
        return commentRepository.findById(id)
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        mapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getId();
    }

    public void update(final Long id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setBody(comment.getComment());
        commentDTO.setFeatureId(comment.getFeature() == null ? null : comment.getFeature().getId());
        return commentDTO;
    }

    private Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setComment(commentDTO.getBody());
        final Feature feature = commentDTO.getFeatureId() == null ? null : featureRepository.findById(commentDTO.getFeatureId())
                .orElseThrow(() -> new NotFoundException("feature not found"));
        comment.setFeature(feature);
        return comment;
    }

}