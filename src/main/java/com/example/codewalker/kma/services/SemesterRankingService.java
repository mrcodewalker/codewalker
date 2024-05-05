package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.repositories.*;
import com.example.codewalker.kma.responses.RankingResponse;
import com.example.codewalker.kma.responses.SemesterRankingResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterRankingService implements ISemesterRankingService{
    private final ScoreService scoreService;
    private final RankingRepository rankingRepository;
    private final SemesterRankingRepository semesterRankingRepository;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectService subjectService;
    private List<SemesterRanking> list;
    @PostConstruct
    public void init() {
        this.list = this.semesterRankingRepository.findAll();
    }

    @Override
    public void updateRanking() {
        List<SemesterRanking> rankingList = this.semesterRankingRepository.findAll();

        Collections.sort(rankingList, Comparator.comparing(SemesterRanking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            SemesterRanking semesterRanking = rankingList.get(i);
            cnt++;
            semesterRanking.setRanking(Long.parseLong(cnt+""));
        }
        this.semesterRankingRepository.saveAll(rankingList);
    }

    @Override
    public void updateGPA() {
        List<Student> studentList = this.studentRepository.findAll();
        Float gpa = 0.F;
        Float asiaGpa=0.F;
        int count = 0;
        int checkExist = 0;
        for (Student student : studentList){
            gpa = 0.F;
            asiaGpa=0.F;
            count = 0;
            checkExist =0;
            List<Semester> scoreList = this.semesterRepository.findByStudentCode(student.getStudentCode());
            if (scoreList.size()<=2&&this.rankingRepository.findByStudentId(student.getStudentId())==null){
                SemesterRanking semesterRanking = SemesterRanking.builder()
                        .student(student)
                        .gpa(1.0F)
                        .ranking(1L)
                        .asiaGpa(2.25F)
                        .build();
                this.semesterRankingRepository.save(semesterRanking);
                continue;
            }
            for (Semester semester: scoreList){
//                if (score.getScoreText().equalsIgnoreCase("f")) continue;
                if (semester.getSubject().getSubjectName().contains("Giáo dục thể chất")
                        || semester.getSubject().getSubjectName().contains("Thực hành vật lý đại cương")) continue;
                Subject subject = this.subjectService.findSubjectByName(semester.getSubject().getSubjectName());
                if (semester.getScoreText().equals("A+")){
//                    asiaGpa+=10*subject.getSubjectCredits();
                    gpa+=4*subject.getSubjectCredits();
                    count+=subject.getSubjectCredits();

                } else {
                    if (semester.getScoreText().equals("A")){
//                        asiaGpa+=(8.9f)*subject.getSubjectCredits();
                        gpa+=(3.8f)*subject.getSubjectCredits();
                        count+=subject.getSubjectCredits();
                    } else {
                        if (semester.getScoreText().equals("B+")){
//                            asiaGpa+=(8.4f)*subject.getSubjectCredits();
                            gpa+=(3.5f)*subject.getSubjectCredits();
                            count+=subject.getSubjectCredits();
                        } else {
                            if (semester.getScoreText().equals("B")){
//                                asiaGpa+=(7.7f)*subject.getSubjectCredits();
                                gpa+=(3.0f)*subject.getSubjectCredits();
                                count+=subject.getSubjectCredits();
                            } else {
                                if (semester.getScoreText().equals("C+")){
//                                    asiaGpa+=(6.9f)*subject.getSubjectCredits();
                                    gpa+=(2.5f)*subject.getSubjectCredits();
                                    count+=subject.getSubjectCredits();
                                }
                                else {
                                    if (semester.getScoreText().equals("C")){
//                                        asiaGpa+=(6.2f)*subject.getSubjectCredits();
                                        gpa+=(2.0f)*subject.getSubjectCredits();
                                        count+=subject.getSubjectCredits();
                                    }
                                    else {
                                        if (semester.getScoreText().equals("D+")){
//                                            asiaGpa+=(5.4f)*subject.getSubjectCredits();
                                            gpa+=(1.5f)*subject.getSubjectCredits();
                                            count+=subject.getSubjectCredits();
                                        }
                                        else {
                                            if (semester.getScoreText().equals("D")){
//                                                asiaGpa+=(4.7f)*subject.getSubjectCredits();
                                                gpa+=(1.0f)*subject.getSubjectCredits();
                                                count+=subject.getSubjectCredits();
                                            }
                                            else continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (count != 0) {
                checkExist = 0;
                Float roundedGPA = (float) Math.round((gpa/count) * 100) / 100;
                Float roundedAsiaGPA = (float) Math.round((gpa/count) *2.5f * 100)/100;
                for (SemesterRanking semesterRanking : this.list){
                    if (semesterRanking.getStudent().getStudentId().equals(student.getStudentId())
                            || this.semesterRankingRepository.findByStudentId(semesterRanking.getStudent().getStudentId())!=null){
                        SemesterRanking clone = SemesterRanking.builder()
                                .student(student)
                                .gpa(roundedGPA)
                                .asiaGpa(roundedAsiaGPA)
                                .ranking(semesterRanking.getRanking())
                                .id(this.rankingRepository.findByStudentId(student.getStudentId()).getId())
                                .build();
                        this.semesterRankingRepository.save(clone);
                        checkExist = 1;
                    }
                }
                if (checkExist==0){
                    SemesterRanking ranking = SemesterRanking.builder()
                            .student(student)
                            .gpa(roundedGPA)
                            .ranking(1L)
                            .asiaGpa(roundedAsiaGPA)
                            .build();
                    this.semesterRankingRepository.save(ranking);
                }
            }
        }
        this.updateRanking();
    }

    @Override
    public List<SemesterRankingResponse> findRanking(String studentCode) {
        String firstCode = "";
        String secondCode = "";
        List<SemesterRanking> rankingList = this.semesterRankingRepository.findAll();
        List<SemesterRanking> semesterRankingList = new ArrayList<>();
        for (SemesterRanking ranking : rankingList){
            if (ranking.getStudent().getStudentCode().contains(studentCode.substring(0,4))){
                semesterRankingList.add(ranking);
            }
        }
        List<SemesterRankingResponse> responses = new ArrayList<>();
        Collections.sort(semesterRankingList, Comparator.comparing(SemesterRanking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<semesterRankingList.size() ; i++) {
            SemesterRanking semesterRanking = semesterRankingList.get(i);
            cnt++;
            semesterRanking.setRanking(Long.parseLong(cnt+""));
            if (semesterRanking.getStudent().getStudentCode().equals(studentCode)
                    || semesterRanking.getStudent().getStudentCode().equalsIgnoreCase(studentCode.toLowerCase())) {
                responses.add(new SemesterRankingResponse().formData(semesterRanking));
                if (cnt >= 4) break;
            }
            if (responses.size()!=0&&cnt>=4) break;
        }
        responses.add(new SemesterRankingResponse().formData(semesterRankingList.get(1)));
        responses.add(new SemesterRankingResponse().formData(semesterRankingList.get(0)));
        responses.add(new SemesterRankingResponse().formData(semesterRankingList.get(2)));

        return responses;
    }
}
