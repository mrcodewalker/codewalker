package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.services.RankingService;
import com.example.codewalker.kma.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/ranking")
@CrossOrigin(origins = "http://localhost:4200")
public class RankingController {
    private final RankingService rankingService;
    private final StudentService studentService;
    @PostMapping("/gpa_update")
    public ResponseEntity<?> updateGPA(){
        this.rankingService.updateGPA();
        return ResponseEntity.ok("Update successfully!");
    }
    @PostMapping("/ranking_update")
    public ResponseEntity<?> updateRanking(){
        this.rankingService.updateRanking();
        return ResponseEntity.ok("Update successfully!");
    }
    @GetMapping("/school")
    public ResponseEntity<?> getRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findSchoolRanking(studentCode)
        );
    }
    @GetMapping("/top/{ranking}")
    public  ResponseEntity<?> findTopRanking(@PathVariable("ranking") Long ranking){
        return ResponseEntity.ok(this.rankingService.findByRanking(ranking));
    }
    @GetMapping("/block")
    public ResponseEntity<?> getBlockRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findBlockRanking(studentCode)
        );
    }
    @GetMapping("/class")
    public ResponseEntity<?> getClassRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findClassRanking(studentCode)
        );
    }
    @GetMapping("/major")
    public ResponseEntity<?> getMajorRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findMajorRanking(studentCode)
        );
    }
    @GetMapping("/block_details")
    public ResponseEntity<?> getBlockDetailRanking(@RequestParam("student_code") String studentCode){
        return ResponseEntity.ok(
                this.rankingService.findBlockDetailRanking(studentCode)
        );
    }
}
