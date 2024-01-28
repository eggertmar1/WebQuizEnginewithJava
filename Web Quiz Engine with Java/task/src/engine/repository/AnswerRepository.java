package engine.repository;

import engine.model.AnsweredQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<AnsweredQuiz, Integer> {
    //@Query("SELECT a FROM AnsweredQuiz a WHERE a.userId = :userId AND a.completedAt = (SELECT MAX(a2.completedAt) FROM AnsweredQuiz a2 WHERE a2.id = a.id AND a2.userId = :userId) ORDER BY a.completedAt DESC")
    @Query("SELECT q FROM AnsweredQuiz q WHERE q.userId = :userId ORDER BY q.completedAt DESC")
    Page<List<AnsweredQuiz>> findCompletedQuizzesByUserId(@Param("userId") Long userId, Pageable pageable);
    @Query("SELECT q FROM AnsweredQuiz q WHERE q.userId = :userId ORDER BY q.answerId DESC")
    Page<AnsweredQuiz> findAnsweredQuizByUserIdOrderByCompletedAtDesc(Long userId, Pageable of);
}
