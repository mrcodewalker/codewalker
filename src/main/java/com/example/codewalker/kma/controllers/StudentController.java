package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/students")
public class StudentController {
    private final StudentService studentService;
    @GetMapping("/names")
    public ResponseEntity<?> getStudentsByName(@RequestParam("name") String studentName){
        return ResponseEntity.ok(studentService.getStudentsByName(studentName));
    }
    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudentByStudentCode(@PathVariable("id") String studentCode){
        return ResponseEntity.ok(this.studentService.findByStudentCode(studentCode));
    }
}
