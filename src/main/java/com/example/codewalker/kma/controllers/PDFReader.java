package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.services.ScoreService;
import com.example.codewalker.kma.services.StudentService;
import com.example.codewalker.kma.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/students")
public class PDFReader {
    // Path =  "C:\Users\ADMIN\Downloads\test.pdf"
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final ScoreService scoreService;

    @PostMapping("/score")
    public ResponseEntity<?> ReadPDFFile() throws Exception {
        File file = new File("C:\\Users\\ADMIN\\Downloads\\test.pdf");
        FileInputStream fileInputStream = new FileInputStream(file);

        PDDocument pdfDocument = PDDocument.load(fileInputStream);
        System.out.println(pdfDocument.getPages().getCount());

        PDFTextStripper pdfTextStripper = new PDFTextStripper();

        pdfTextStripper.setStartPage(2);

        PDPage firstPage = pdfDocument.getPage(0);

        String docText = pdfTextStripper.getText(pdfDocument);

        Map<String, String> subjects = new LinkedHashMap<>();
        Set<String> idSubjects = new HashSet<>();
// Tách văn bản thành các dòng
        String[] lines = docText.split("\\r?\\n");
        int rows = 0;
        int count = 0;
        for (String line : lines) {
            int spaceIndex = line.indexOf(" ");

            if (spaceIndex != -1) {
                String firstWord = line.substring(0, spaceIndex);

                String secondWord = line.substring(spaceIndex + 1);

                if (firstWord.length()<=2&&!firstWord.isEmpty()&&firstWord.matches("[1-9][0-9]?")){
                    if (!idSubjects.contains(firstWord)) {
                        idSubjects.add(firstWord);
                        if (secondWord.contains("Học lại")){
                            System.out.println(secondWord);
                            secondWord = secondWord.substring(0, secondWord.indexOf("Học lại")).trim();
                        }
                        subjects.put(firstWord, secondWord);
                    } else {
                        break;
                    };
                }
            }

        }
        for (Map.Entry<String, String> entry: subjects.entrySet()){ // All Subjects
            if (subjectService.findBySubjectName(entry.getValue())){
                continue;
            } else {
                subjectService.createSubject(Subject.builder()
                                .subjectName(entry.getValue())
                        .build());
            }
        }
        boolean passedSubjects = false;
        for (String line : lines) {
            int spaceIndex = line.indexOf(" ");

            if (spaceIndex != -1) {
                String firstWord = line.substring(0, spaceIndex);

                String secondWord = line.substring(spaceIndex + 1);

                if (firstWord.length()<=4&&!firstWord.isEmpty()&&firstWord.matches("[1-9]\\d{0,3}")){
                    if (firstWord.equals(String.valueOf(idSubjects.size()))){
                        passedSubjects = true;
                        continue;
                    }
                    if (passedSubjects){
                        if (firstWord.equals("1")){
                            rows++;
                        }
                        String data[] = secondWord.split(" ");
                        String studentCode = data[1];
                        String studentName = "";
                        int mark = 4;
                        for (int i=2;i<data.length;i++){
                            if (data[i].contains("CT")||data[i].contains("AT")||data[i].contains("DT")){
                                mark = i ;
                                for (int j=2;j<i;j++){
                                    studentName += data[j] + " ";
                                }
                                studentName = studentName.trim();
                                break;
                            }
                        }
                        int cnt = 0;
                        String studentClass = data[mark];
                        for (int i=mark+1;i<data.length;i++){
                            String entry = data[i];
                            if (entry.matches("^[-+]?[0-9]*\\.?[0-9]+$")){
                                cnt++;
                            }
                        }
                        Float scoreFirst = 0F;
                        Float scoreSecond = 0F;
                        Float scoreFinal = 0F;
                        Float scoreOverRall = 0F;
                        String scoreText = "";
                        if (cnt==4){
                             scoreFirst = Float.parseFloat(data[mark+1]);
                             scoreSecond = Float.parseFloat(data[mark+2]);
                             scoreFinal = Float.parseFloat(data[mark+3]);
                             scoreOverRall = Float.parseFloat(data[mark+4]);
                             scoreText = data[data.length-1];
                        } else continue;
                        if (scoreFirst>=0&&scoreSecond>=0&&scoreFinal>=0&&scoreOverRall>=0) {
                            Student student = Student.builder()
                                    .studentClass(studentClass)
                                    .studentCode(studentCode)
                                    .studentId(studentService.findByStudentCode(studentCode).getStudentId())
                                    .studentName(studentName)
                                    .build();

                            studentService.createStudent(student);

                            String subject = subjects.get(rows + "");
                            if (rows>idSubjects.size()) return null;
                            if (subject != null) {

                                Score score = Score.builder()
                                        .scoreFirst(scoreFirst)
                                        .scoreFinal(scoreFinal)
                                        .scoreText(scoreText)
                                        .scoreSecond(scoreSecond)
                                        .scoreOverall(scoreOverRall)
                                        .student(student)
                                        .subject(subjectService.findBySubjectId((long) rows))
                                        .build();

                                scoreService.createScore(score);

                            }
                        }
                    }
                }
            }

        }

        pdfDocument.close();
        return null;
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getScoresByStudentCode(@PathVariable("id") String studentCode){
//        List<Score> scores = scoreService.findAll();
//        List<Score> data = new ArrayList<>();
//        for (Score clone : scores){
//            if (clone.getStudent().getStudentCode().equals(studentCode)){
//                data.add(clone);
//            }
//        }
        return ResponseEntity.ok(scoreService.getScoreByStudentCode(studentCode));
    }
}
