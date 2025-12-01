package server.webservice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import entity.Comment;
import services.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@AllArgsConstructor
public class CommentServiceREST {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game) {
        return commentService.getLastComments(game);
    }

    @PostMapping
    public void addComment(@RequestBody Comment comment) {
        commentService.addComment(comment);
    }
}
