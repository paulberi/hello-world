package com.example.api.demoapi.service;

import com.example.api.demoapi.entity.ExamResult;
import com.example.api.demoapi.entity.Question;
import com.example.api.demoapi.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private ExamRepository examRepository;

    public List<Question> getQuestions() {
        return examRepository.getQuestions();
    }

    public String getAnswer(String question) {
        for(Question q : examRepository.getQuestions()) {
            if(q.getQuestion().equals(question)) {
                return q.getAnswer();
            }
        }
        return "Not found!";
    }

    public String addQuestion(Question question) {
        return this.examRepository.addQuestion(question);
    }
}
