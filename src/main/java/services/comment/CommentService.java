package services.comment;

import entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);

    void updatePlayerName(String oldPlayer, String newPlayer);

    List<Comment> getLastComments(String game);

    void reset();
}
