package com.example.api.demoapi.controller;

import com.example.api.demoapi.entity.ExamResult;
import com.example.api.demoapi.entity.Question;
import com.example.api.demoapi.service.GradeService;
import com.example.api.demoapi.service.QuestionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {
	
	private Logger logger = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/questions")
    public List<Question> getQuestions() {
        return questionService.getQuestions();
    }

    @GetMapping("/answer")
    public String getSuccess(@RequestParam String question) {
        return questionService.getAnswer(question);
    }
    
    @GetMapping("/success/{name}")
    public String getSuccessByName(@PathVariable("name") String name) {
       String student= gradeService.getName(name);
        logger.info("Student is "+student);
        return "Success "+student;
    }
    @GetMapping("/examresult")
    public  List<ExamResult> getAllResults() {
        return gradeService.getExamResult();
    }
    
    @RequestMapping(path = "/addquestion", method = RequestMethod.POST, consumes = "application/json")
    public String addQuestion(@RequestBody 	Question question) {
    	questionService.addQuestion(question);
    	logger.info("Question created");
        return "Question added";
    }
    

}
