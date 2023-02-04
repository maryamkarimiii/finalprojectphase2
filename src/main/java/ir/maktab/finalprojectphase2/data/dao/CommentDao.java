package ir.maktab.finalprojectphase2.data.dao;

import ir.maktab.finalprojectphase2.data.model.Comment;
import ir.maktab.finalprojectphase2.data.model.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentDao extends JpaRepository<Comment, Long> {

    @Query("SELECT AVG(e.score) FROM Comment e WHERE e.expert =:expert")
    Double averageOfExpertScore(@Param("expert") Expert expert);

}
