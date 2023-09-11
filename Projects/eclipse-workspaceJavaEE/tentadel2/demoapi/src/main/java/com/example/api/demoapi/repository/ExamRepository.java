package com.example.api.demoapi.repository;

import com.example.api.demoapi.entity.ExamResult;
import com.example.api.demoapi.entity.Question;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamRepository {

    private ArrayList<Question> questions;
    private ArrayList<ExamResult> examResults;

    public ExamRepository() {
        this.questions = new ArrayList<>();
        questions.add(new Question("What is Java","Programming language"));
        questions.add(new Question("What is JDK","Java Developer Kit"));
        questions.add(new Question("What is JVM","Java Virtual Machine"));

        this.examResults = new ArrayList<>();
        examResults.add(new ExamResult("Jane", "VG"));
        examResults.add(new ExamResult("John", "G"));
    }

    public Question SaveQuestion(Question question) {
        this.questions.add(question);
        return question;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public List<ExamResult> getExamResult() {
        return this.examResults;
    }

    public String addQuestion(Question question) {
        this.questions.add(question);
        return "Question added";
    }

}
