package engine.controller;

import engine.dto.AnswerInput;
import engine.model.AnsweredQuiz;
import engine.model.Quiz;
import engine.model.QuizResponse;
import engine.service.AuthService;
import engine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final AuthService authService;

    @Autowired
    public QuizController(QuizService quizService, AuthService authService) {
        this.quizService = quizService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<Page<Quiz>> getQuizzes(Pageable pageable) {
        return ResponseEntity.ok(quizService.getQuizzes(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable int id) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quiz);
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@RequestBody @Valid Quiz quiz) {
        System.out.println("createQuiz::This user is authenticated: " + authService.getAuthenticatedUserId());
        Quiz createdQuiz = quizService.addQuiz(quiz);
        return ResponseEntity.ok(createdQuiz);
    }

    @PostMapping("/{id}/solve")
    public ResponseEntity<QuizResponse> solveQuiz(@PathVariable int id, @RequestBody AnswerInput answer) {
        System.out.println("solveQuiz::Quiz: " + id + " userId" + authService.getAuthenticatedUserId());
        Quiz quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }

        QuizResponse quizResponse = quizService.solveQuiz(id, answer.getAnswer());
        if (quizResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quizResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable int id) {
        System.out.println("deleteQuiz::This user is authenticated: " + authService.getAuthenticatedUserId());
        Quiz quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return ResponseEntity.notFound().build();
        }

        if (!Objects.equals(quiz.getCreatedByUserId(), authService.getAuthenticatedUserId())) {
            return ResponseEntity.status(403).build();
        }

        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completed")
    public ResponseEntity<Page<AnsweredQuiz>> getCompletedQuizzes(Pageable pageable) {
        System.out.println("getCompletedQuizzes::This user is authenticated: " + authService.getAuthenticatedUserId());
        return ResponseEntity.ok(quizService.getCompletedQuizzes(pageable));
    }
}

