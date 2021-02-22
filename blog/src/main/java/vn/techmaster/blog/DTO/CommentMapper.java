package vn.techmaster.blog.DTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vn.techmaster.blog.model.Comment;
import vn.techmaster.blog.model.User;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target="userFullname", source="comment.user.fullname")
    CommentPOJO commentToCommentPOJO(Comment comment);
//    Comment commentPOJOToComment(CommentPOJO commentPOJO);
}
