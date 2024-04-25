package com.example.codewalker.kma.controllers;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.SubjectRepository;
import com.example.codewalker.kma.services.ScoreService;
import com.example.codewalker.kma.services.StudentService;
import com.example.codewalker.kma.services.SubjectService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.modelmapper.internal.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/students")
public class PDFReader {
    // Path =  "C:\Users\ADMIN\Downloads\test.pdf"
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final SubjectRepository subjectRepository;
    private final ScoreService scoreService;
    public List<String> errors = new ArrayList<>();
    private List<String> listSubjectsName = new ArrayList<>();
    private List<Pair<String,Integer>> specialCase = new ArrayList<>();

    @PostMapping("/score")
    public ResponseEntity<?> ReadPDFFile() throws Exception {
        File file = new File("C:\\Users\\ADMIN\\Downloads\\23_24_2.pdf");
        FileInputStream fileInputStream = new FileInputStream(file);
        Map<String, Integer> allSubjects = new LinkedHashMap<>();
        errors.add("N25");
        errors.add("N100");
        errors.add("TKD");

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
        int rows = -1;
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
                            int index = secondWord.indexOf("Học lại");
                            if (index >= 0) {
                                secondWord = secondWord.substring(0, index).trim();
                            }
                        }
                        if (secondWord.contains("CT")||secondWord.contains("DT")||secondWord.contains("AT")){
                            int indexCT = secondWord.indexOf("CT");
                            int indexDT = secondWord.indexOf("DT");
                            int indexAT = secondWord.indexOf("AT");

                            int minIndex = -1;
                            if (indexCT >= 0 || indexDT >= 0 || indexAT >= 0) {
                                minIndex = Math.min(indexCT >= 0 ? indexCT : Integer.MAX_VALUE,
                                        Math.min(indexDT >= 0 ? indexDT : Integer.MAX_VALUE,
                                                indexAT >= 0 ? indexAT : Integer.MAX_VALUE));
                            }

                            if (minIndex >= 0) {
                                secondWord = secondWord.substring(0, minIndex).trim();
                            }
                        }
                        if (allSubjects.get(secondWord.trim())!=null&&allSubjects.get(secondWord.trim())>=1) {
                            allSubjects.put(secondWord.trim(), allSubjects.get(secondWord.trim()) + 1);
                        } else {
                            if (allSubjects.get(secondWord.trim())==null||allSubjects.get(secondWord.trim())==0){
                                allSubjects.put(secondWord.trim(),1);
                            }
                        }
                        subjects.put(firstWord, secondWord.trim());
                    } else {
                        break;
                    };
                }
            }

        }
        for (Map.Entry<String, String> entry: subjects.entrySet()){ // All Subjects
            if (!this.checkContainsSubject(entry.getValue())){
                this.subjectService.createSubject(Subject.builder()
                                .subjectName(entry.getValue())
                        .build());
            }
        }
        for (Map.Entry<String, Integer> entry: allSubjects.entrySet()){
            if (entry.getValue()>1){
                this.specialCase.add(Pair.of(entry.getKey(), entry.getValue()));
            }
        }
//        System.out.println(this.specialCase);
        boolean passedSubjects = false;
        collectAllSubjects();

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
                        if (data[0].equals("0")) continue;
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
                        boolean checkError = false;
                        String studentClass = data[mark];
                        for (int i=mark+1;i<data.length;i++){
                            String entry = data[i];
                            if (entry.matches("^[-+]?[0-9]*\\.?[0-9]+$")){
                                cnt++;
                                if (cnt==4) break;
                            } else {
                                checkError=true;
                                break;
                            }
                        }
                        if (checkError){
                            checkError=false;
                            continue;
                        }
                        Float scoreFirst = 0F;
                        Float scoreSecond = 0F;
                        Float scoreFinal = 0F;
                        Float scoreOverRall = 0F;
                        String scoreText = "";
                        if (cnt == 4) {
                            scoreFirst = Float.parseFloat(data[mark+1]);
                            scoreSecond = Float.parseFloat(data[mark+2]);
                            scoreFinal = Float.parseFloat(data[mark+3]);
                            scoreOverRall = Float.parseFloat(data[mark+4]);
                            scoreText = data[data.length-1];
                            String[] invalidScores = {"A", "A+", "B+", "C+", "D+", "D", "B", "C", "F"};
                            if (!Arrays.asList(invalidScores).contains(scoreText.toUpperCase())) continue;
                        } else continue;
                        if (scoreFirst>=0&&scoreSecond>=0&&scoreFinal>=0&&scoreOverRall>=0) {

                                Student student = Student.builder()
                                        .studentClass(studentClass)
                                        .studentCode(studentCode)
//                                        .studentId(studentService.findByStudentCode(studentCode).getStudentId())
                                        .studentName(studentName)
                                        .build();

                                if (studentService.existByStudentCode(studentCode)){
                                    student.setStudentId(studentService.findByStudentCode(studentCode).getStudentId());
                                }

                                studentService.createStudent(student);

                                if (rows<0||this.listSubjectsName.size()==0) continue;
                            Subject subject = Subject.builder()
                                    .subjectName(this.listSubjectsName.get(rows))
                                    .id(subjectService.findSubjectByName(this.listSubjectsName.get(rows)).getId())
                                    .build();

//                            if (student.getStudentId()==1109){
//                                System.out.println(this.listSubjectsName.get(rows-1));
//                                return null;
//                            }


                                Score score = Score.builder()
                                        .scoreFirst(scoreFirst)
                                        .scoreFinal(scoreFinal)
                                        .scoreText(scoreText)
                                        .scoreSecond(scoreSecond)
                                        .scoreOverall(scoreOverRall)
                                        .student(student)
                                        .subject(subject)
                                        .build();
//                                System.out.println(score);
                                scoreService.createScore(score);

                        }
                    }
                }
            }
        }

        pdfDocument.close();
        return null;
    }
    public void collectAllSubjects() throws Exception {
        File file = new File("C:\\Users\\ADMIN\\Downloads\\23_24_2.pdf");
        FileInputStream fileInputStream = new FileInputStream(file);
        Map<String,String> list = new LinkedHashMap<>();
        errors.add("N25");
        errors.add("N100");
        errors.add("TKD");

        PDDocument pdfDocument = PDDocument.load(fileInputStream);
        System.out.println(pdfDocument.getPages().getCount());

        PDFTextStripper pdfTextStripper = new PDFTextStripper();

        pdfTextStripper.setStartPage(2);

        PDPage firstPage = pdfDocument.getPage(0);

        String docText = pdfTextStripper.getText(pdfDocument);

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
                    } else break;
                }
            }

        }
        List<Subject> subjectList = subjectService.findAll();
        List<String> subjectsName = new ArrayList<>();
        for (Subject subject : subjectList) {
            String subjectName = subject.getSubjectName();
            subjectsName.add(subjectName);
        }
//        for (Map.Entry<String, String> entry: subjects.entrySet()){ // All Subjects
//            System.out.println(entry.getKey()+" "+entry.getValue());
//        }
        boolean passedSubjects = false;
        for (String line : lines) {
            int spaceIndex = line.indexOf(" ");

            if (spaceIndex != -1) {
                String firstWord = line.substring(0, spaceIndex);

                String secondWord = line.substring(spaceIndex + 1);

                if (firstWord.length() <= 4 && !firstWord.isEmpty() && firstWord.matches("[1-9]\\d{0,3}")) {
                    if (firstWord.equals(String.valueOf(idSubjects.size()))) {
                        passedSubjects = true;
                        continue;
                    }
                }
            }
            if (passedSubjects) {
                for (String subjectName : subjectsName) {
                        int index = line.lastIndexOf("-");
                        if (index != -1) {
                            String subjectNameLine = line.substring(0, index).trim();
                            if (subjectNameLine.equals(subjectName)){
                               if (!this.listSubjectsName.contains(subjectName)) this.listSubjectsName.add(subjectName);
                               else {
                                   if (this.listSubjectsName.get(this.listSubjectsName.size()-1).equals(subjectName)){
                                       continue;
                                   } else {
                                       for (int i = 0; i < this.specialCase.size(); i++) {
                                           Pair<String, Integer> clone = this.specialCase.get(i);
                                           if (clone.getLeft().equals(subjectName) && clone.getRight() > 1) {
                                               Pair<String, Integer> suffix = Pair.of(clone.getLeft(), clone.getRight() - 1);
                                               this.listSubjectsName.add(subjectName);
                                               this.specialCase.set(i, suffix);
                                           }
                                       }
                                   }
                               }
                        } else {
                            // Xử lý trường hợp không tìm thấy dấu "-"
                            System.out.println("Không tìm thấy dấu '-' trong chuỗi.");
                        }
                    }
                }
//                System.out.println(line);
            }
            int cnt = 0;
         for (String clone : listSubjectsName){
             System.out.println(cnt+ " "+clone);
             cnt++;
         }
//            System.out.println(listSubjectsName.size());
        }
    }
    @GetMapping("/read/{path}")
    public ResponseEntity<?> readPDFFile(@PathVariable("path") String path) throws Exception{
        File file = new File("C:\\Users\\ADMIN\\Downloads\\23_24_2.pdf");
        FileInputStream fileInputStream = new FileInputStream(file);
        Map<String,String> list = new LinkedHashMap<>();
        errors.add("N25");
        errors.add("N100");
        errors.add("TKD");

        PDDocument pdfDocument = PDDocument.load(fileInputStream);
        System.out.println(pdfDocument.getPages().getCount());

        PDFTextStripper pdfTextStripper = new PDFTextStripper();

        pdfTextStripper.setStartPage(2);

        PDPage firstPage = pdfDocument.getPage(0);

        String docText = pdfTextStripper.getText(pdfDocument);

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
                    } else break;
                }
            }

        }
        this.collectAllSubjects();
        List<Subject> subjectList = subjectService.findAll();
        List<String> subjectsName = new ArrayList<>();
        for (Subject subject : subjectList) {
            String subjectName = subject.getSubjectName();
            subjectsName.add(subjectName);
        }
//        for (Map.Entry<String, String> entry: subjects.entrySet()){ // All Subjects
//            System.out.println(entry.getKey()+" "+entry.getValue());
//        }
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
                    }
                }
                if (passedSubjects){
//                    for (String subjectName : subjectsName){
//                        if (line.contains(subjectName)&&!listSubjectsName.contains(subjectName)){
//                            System.out.println(subjectName);
//                            listSubjectsName.add(subjectName);
//                        }
//                    }
//                    System.out.println(line);
                }
//            System.out.println(listSubjectsName);
            }
//            System.out.println(listSubjectsName);
//        System.out.println(listSubjectsName.size());
        return null;
    }
    boolean checkContainsSubject(String subjectName){
        List<Subject> subjectsData = subjectRepository.findAll();
        List<String> subjectsName = new ArrayList<>();
        for (Subject subject : subjectsData){
            if (subject.getSubjectName().equals(subjectName)){
                return true;
            }
        }
        return false;
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
