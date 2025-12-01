package services.comment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import entity.Comment;

import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) {
        List<Comment> existingComments = entityManager.createNamedQuery("Comment.checkComment", Comment.class)
                .setParameter("game", comment.getGame())
                .setParameter("player", comment.getPlayer())
                .getResultList();
        if (!existingComments.isEmpty()) {
            Comment existing = existingComments.get(0);
            existing.setComment(comment.getComment());
            existing.setCommented_at(comment.getCommented_at());
            entityManager.merge(existing);
        } else {
            entityManager.persist(comment);
        }
    }

    @Override
    public void updatePlayerName(String oldPlayer, String newPlayer) {
        entityManager.createNamedQuery("Comment.updatePlayer")
                .setParameter("newName", newPlayer)
                .setParameter("oldName", oldPlayer)
                .executeUpdate();
    }

    @Override
    public List<Comment> getLastComments(String game) {
        return entityManager.createNamedQuery("Comment.getComments")
                .setParameter("game", game).getResultList();
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Comment.reset").executeUpdate();
    }
}
