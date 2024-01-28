package engine.service;

import engine.model.AnsweredQuiz;
import engine.model.Quiz;
import engine.model.QuizResponse;
import engine.repository.AnswerRepository;
import engine.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;
    private final AuthService authService;

    @Autowired
    public QuizService(QuizRepository quizRepository, AnswerRepository answerRepository, AuthService authService) {
        this.quizRepository = quizRepository;
        this.answerRepository = answerRepository;
        this.authService = authService;
    }

    public Page<Quiz> getQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Quiz getQuizById(int id) {
        return quizRepository.findById(id).orElse(null);
    }

    public Quiz addQuiz(Quiz quiz) {
        quiz.setCreatedByUserId(authService.getAuthenticatedUserId());
        quizRepository.save(quiz);
        return quiz;
    }


    public QuizResponse solveQuiz(int id, int[] answers) {
        Quiz quiz = this.getQuizById(id);
        if (quiz == null) {
            return null;
        }

        // answer the quiz and save
        AnsweredQuiz answeredQuiz = new AnsweredQuiz();
        answeredQuiz.setUserId(authService.getAuthenticatedUserId());
        answeredQuiz.setId(id);
        answeredQuiz.setCompletedAt(new Date().toString());


        boolean success = isAnswersCorrect(quiz.getAnswer(), answers);
        if (success) {
            answerRepository.save(answeredQuiz);
        }

        if (success) {
            return new QuizResponse(true, "Congratulations, you're right!");
        } else {
            return new QuizResponse(false, "Wrong answer! Please, try again.");
        }
    }

    private boolean isAnswersCorrect(List<Integer> correctAnswers, int[] givenAnswers) {
        if (givenAnswers == null) {
            return false; // Both lists are null, so they can't be compared
        } else if (correctAnswers == null && givenAnswers.length == 0) {
            return true; // No correct answers - so no answer is correct
        } else if (correctAnswers == null) {
            return false; // Correct answers are null, but given answers are not
        }

        List<Integer> copyOfCorrectAnswers = new ArrayList<>(correctAnswers);
        List<Integer> copyOfGivenAnswers = new ArrayList<>();
        for (int answer : givenAnswers) {
            copyOfGivenAnswers.add(answer);
        }

        Collections.sort(copyOfCorrectAnswers);
        Collections.sort(copyOfGivenAnswers);

        return copyOfCorrectAnswers.equals(copyOfGivenAnswers);
    }

    public void deleteQuiz(int id) {
        quizRepository.deleteById(id);
    }

    public Page<AnsweredQuiz> getCompletedQuizzes(Pageable pageable) {
        Page<AnsweredQuiz> hehe = answerRepository.findAnsweredQuizByUserIdOrderByCompletedAtDesc(authService.getAuthenticatedUserId(), pageable);
        System.out.println("getCompletedQuizzes::This user is authenticated: " + authService.getAuthenticatedUserId() + " hehe: " + hehe.getContent());
        return hehe;
    }
}
