package com.example.api.demoapi.service;

import com.example.api.demoapi.entity.ExamResult;
import com.example.api.demoapi.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private ExamRepository examRepository;

    public List<ExamResult> getExamResult() {
        return examRepository.getExamResult();
    }
    
    public String getName(String student) {
        for(ExamResult results : examRepository.getExamResult()) {
            if(results.getStudent().equals(student)) {
                return results.getStudent();
            }
        }
        return "Not found!";
    }

}
