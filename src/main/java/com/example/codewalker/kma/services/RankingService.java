package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.RankingRepository;
import com.example.codewalker.kma.repositories.ScoreRepository;
import com.example.codewalker.kma.repositories.StudentRepository;
import com.example.codewalker.kma.responses.RankingResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService implements IRankingService{
    private final ScoreService scoreService;
    private final RankingRepository rankingRepository;
    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final SubjectService subjectService;
    private List<Ranking> list;

    @PostConstruct
    public void init() {
        this.list = this.rankingRepository.findAll();
    }
    @Override
    public void updateGPA() {
        Long MAX_ID = 9999L;
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
            List<Score> scoreList = this.scoreRepository.findByStudentCode(student.getStudentCode());
            if (scoreList.size()<=2&&this.rankingRepository.findByStudentId(student.getStudentId())==null){
                Ranking ranking = Ranking.builder()
                        .student(student)
                        .gpa(1.0F)
                        .ranking(1L)
                        .asiaGpa(2.25F)
                        .build();
                this.rankingRepository.save(ranking);
                continue;
            }
            for (Score score: scoreList){
//                if (score.getScoreText().equalsIgnoreCase("f")) continue;
                if (score.getSubject().getSubjectName().contains("Giáo dục thể chất")
                    || score.getSubject().getSubjectName().contains("Thực hành vật lý đại cương")) continue;
                Subject subject = this.subjectService.findSubjectByName(score.getSubject().getSubjectName());
                if (score.getScoreText().equals("A+")){
//                    asiaGpa+=10*subject.getSubjectCredits();
                    gpa+=4*subject.getSubjectCredits();
                    count+=subject.getSubjectCredits();

                } else {
                    if (score.getScoreText().equals("A")){
//                        asiaGpa+=(8.9f)*subject.getSubjectCredits();
                        gpa+=(3.8f)*subject.getSubjectCredits();
                        count+=subject.getSubjectCredits();
                    } else {
                        if (score.getScoreText().equals("B+")){
//                            asiaGpa+=(8.4f)*subject.getSubjectCredits();
                            gpa+=(3.5f)*subject.getSubjectCredits();
                            count+=subject.getSubjectCredits();
                        } else {
                            if (score.getScoreText().equals("B")){
//                                asiaGpa+=(7.7f)*subject.getSubjectCredits();
                                gpa+=(3.0f)*subject.getSubjectCredits();
                                count+=subject.getSubjectCredits();
                            } else {
                                if (score.getScoreText().equals("C+")){
//                                    asiaGpa+=(6.9f)*subject.getSubjectCredits();
                                    gpa+=(2.5f)*subject.getSubjectCredits();
                                    count+=subject.getSubjectCredits();
                                }
                                else {
                                    if (score.getScoreText().equals("C")){
//                                        asiaGpa+=(6.2f)*subject.getSubjectCredits();
                                        gpa+=(2.0f)*subject.getSubjectCredits();
                                        count+=subject.getSubjectCredits();
                                    }
                                    else {
                                        if (score.getScoreText().equals("D+")){
//                                            asiaGpa+=(5.4f)*subject.getSubjectCredits();
                                            gpa+=(1.5f)*subject.getSubjectCredits();
                                            count+=subject.getSubjectCredits();
                                        }
                                        else {
                                            if (score.getScoreText().equals("D")){
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
                for (Ranking ranking : this.list){
                    if (ranking.getStudent().getStudentId().equals(student.getStudentId())
                    || this.rankingRepository.findByStudentId(ranking.getStudent().getStudentId())!=null){
                        Ranking clone = Ranking.builder()
                                .student(student)
                                .gpa(roundedGPA)
                                .asiaGpa(roundedAsiaGPA)
                                .ranking(ranking.getRanking())
                                .id(this.rankingRepository.findByStudentId(student.getStudentId()).getId())
                                .build();
                        this.rankingRepository.save(clone);
                        checkExist = 1;
                    }
                }
                if (checkExist==0){
                    Ranking ranking = Ranking.builder()
                            .student(student)
                            .gpa(roundedGPA)
                            .ranking(1L)
                            .asiaGpa(roundedAsiaGPA)
                            .build();
                    this.rankingRepository.save(ranking);
                }
            }
        }
    }

    @Override
    public RankingResponse findByRanking(Long ranking) {
        Ranking clone = this.rankingRepository.findByRanking(ranking);
        return RankingResponse.builder()
                .asiaGpa(clone.getAsiaGpa())
                .gpa(clone.getGpa())
                .studentName(clone.getStudent().getStudentName())
                .ranking(ranking)
                .studentCode(clone.getStudent().getStudentCode())
                .studentClass(clone.getStudent().getStudentClass())
                .build();
    }

    @Override
    public List<RankingResponse> findBlockRanking(String studentCode) {
        String firstCode = "";
        String secondCode = "";
        if (studentCode.contains("CT07")){
            firstCode = "AT19";
            secondCode = "DT06";
        } else {
            if (studentCode.contains("AT19")){
                firstCode = "CT07";
                secondCode = "DT06";
            } else {
                if (studentCode.contains("DT06")){
                    firstCode = "CT07";
                    secondCode = "AT19";
                }
            }
        }
        if (studentCode.contains("CT06")){
            firstCode = "AT18";
            secondCode = "DT05";
        } else {
            if (studentCode.contains("AT18")){
                firstCode = "CT06";
                secondCode = "DT05";
            } else {
                if (studentCode.contains("DT05")){
                    firstCode = "CT06";
                    secondCode = "AT18";
                }
            }
        }
        if (studentCode.contains("CT08")){
            firstCode = "AT20";
            secondCode = "DT07";
        } else {
            if (studentCode.contains("AT20")){
                firstCode = "CT08";
                secondCode = "DT07";
            } else {
                if (studentCode.contains("DT07")){
                    firstCode = "CT08";
                    secondCode = "AT20";
                }
            }
        }
        if (studentCode.contains("CT05")){
            firstCode = "AT17";
            secondCode = "DT04";
        } else {
            if (studentCode.contains("AT17")){
                firstCode = "CT05";
                secondCode = "DT04";
            } else {
                if (studentCode.contains("DT04")){
                    firstCode = "CT05";
                    secondCode = "AT17";
                }
            }
        }
        if (studentCode.contains("CT04")){
            firstCode = "AT16";
            secondCode = "DT03";
        } else {
            if (studentCode.contains("AT16")){
                firstCode = "CT04";
                secondCode = "DT03";
            } else {
                if (studentCode.contains("DT03")){
                    firstCode = "CT04";
                    secondCode = "AT16";
                }
            }
        }
        if (studentCode.contains("CT03")){
            firstCode = "AT15";
            secondCode = "DT02";
        } else {
            if (studentCode.contains("AT15")){
                firstCode = "CT03";
                secondCode = "DT02";
            } else {
                if (studentCode.contains("DT02")){
                    firstCode = "CT03";
                    secondCode = "AT15";
                }
            }
        }
        List<Ranking> rankingList = new ArrayList<>();
        for (Ranking ranking : this.list){
            if (ranking.getStudent().getStudentCode().contains(studentCode.substring(0,4))
        || ranking.getStudent().getStudentCode().contains(firstCode)
                    || ranking.getStudent().getStudentCode().contains(secondCode)){
                rankingList.add(ranking);
            }
        }
        List<RankingResponse> responses = new ArrayList<>();
        Collections.sort(rankingList, Comparator.comparing(Ranking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            Ranking ranking = rankingList.get(i);
            cnt++;
            ranking.setRanking(Long.parseLong(cnt+""));
            if (ranking.getStudent().getStudentCode().equals(studentCode)
                    || ranking.getStudent().getStudentCode().equalsIgnoreCase(studentCode.toLowerCase())) {
                responses.add(new RankingResponse().formData(ranking));
                if (cnt >= 4) break;
            }
            if (responses.size()!=0&&cnt>=4) break;
        }
//        for (Ranking ranking : rankingList){
//            count++;
//            if (ranking.getStudent().getStudentCode().equals(studentCode)){
//                RankingResponse response = RankingResponse.builder()
//                        .studentClass(ranking.getStudent().getStudentClass())
//                        .asiaGpa(ranking.getAsiaGpa())
//                        .studentCode(studentCode)
//                        .ranking(ranking.getRanking())
//                        .studentName(ranking.getStudent().getStudentName())
//                        .gpa(ranking.getGpa())
//                        .build();
//                responses.add(response);
//                if (count>=4) break;
//            }
//            if (responses.size()!=0&&count>=4) break;
//        }
        responses.add(new RankingResponse().formData(rankingList.get(1)));
        responses.add(new RankingResponse().formData(rankingList.get(0)));
        responses.add(new RankingResponse().formData(rankingList.get(2)));

        return responses;
    }

    @Override
    public List<RankingResponse> findClassRanking(String studentCode) {
        List<Ranking> rankingList = new ArrayList<>();
        List<RankingResponse> responses = new ArrayList<>();
        for (Ranking ranking: this.list){
            if (ranking.getStudent().getStudentCode().toLowerCase().contains(studentCode.substring(0,6).toLowerCase())){
                rankingList.add(ranking);
            }
        }
        Collections.sort(rankingList, Comparator.comparing(Ranking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            Ranking ranking = rankingList.get(i);
            cnt++;
            ranking.setRanking(Long.parseLong(cnt+""));
            if (ranking.getStudent().getStudentCode().equals(studentCode)
            || ranking.getStudent().getStudentCode().equalsIgnoreCase(studentCode.toLowerCase())) {
                responses.add(new RankingResponse().formData(ranking));
                if (cnt >= 4) break;
            }
            if (responses.size()!=0&&cnt>=4) break;
        }
        responses.add(new RankingResponse().formData(rankingList.get(1)));
        responses.add(new RankingResponse().formData(rankingList.get(0)));
        responses.add(new RankingResponse().formData(rankingList.get(2)));

        return responses;
    }

    @Override
    public void updateRanking() {
        List<Ranking> rankingList = this.rankingRepository.findAll();

        Collections.sort(rankingList, Comparator.comparing(Ranking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            Ranking ranking = rankingList.get(i);
            cnt++;
            ranking.setRanking(Long.parseLong(cnt+""));
        }
        this.rankingRepository.saveAll(rankingList);
    }

    @Override
    public List<RankingResponse> findSchoolRanking(String studentCode) {
        List<RankingResponse> responses = new ArrayList<>();
        List<Ranking> rankingList = this.list;
        Collections.sort(rankingList, Comparator.comparing(Ranking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            Ranking ranking = rankingList.get(i);
            cnt++;
            ranking.setRanking(Long.parseLong(cnt+""));
            if (ranking.getStudent().getStudentCode().equals(studentCode)
                    || ranking.getStudent().getStudentCode().equalsIgnoreCase(studentCode.toLowerCase())) {
                responses.add(new RankingResponse().formData(ranking));
                if (cnt >= 4) break;
            }
            if (responses.size()!=0&&cnt>=4) break;
        }
        responses.add(new RankingResponse().formData(rankingList.get(1)));
        responses.add(new RankingResponse().formData(rankingList.get(0)));
        responses.add(new RankingResponse().formData(rankingList.get(2)));
        return responses;
    }
    @Override
    public List<RankingResponse> findMajorRanking(String studentCode) {
        List<Ranking> rankings = this.list;
        List<Ranking> rankingList = new ArrayList<>();
        for (Ranking ranking : rankings){
            if (ranking.getStudent().getStudentCode().contains(studentCode.substring(0,2))){
                rankingList.add(ranking);
            }
        }
        List<RankingResponse> responses = new ArrayList<>();
        Collections.sort(rankingList, Comparator.comparing(Ranking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            Ranking ranking = rankingList.get(i);
            cnt++;
            ranking.setRanking(Long.parseLong(cnt+""));
            if (ranking.getStudent().getStudentCode().equals(studentCode)
                    || ranking.getStudent().getStudentCode().equalsIgnoreCase(studentCode.toLowerCase())) {
                responses.add(new RankingResponse().formData(ranking));
                if (cnt >= 4) break;
            }
            if (responses.size()!=0&&cnt>=4) break;
        }
//        for (Ranking ranking : rankingList){
//            count++;
//            if (ranking.getStudent().getStudentCode().equals(studentCode)){
//                RankingResponse response = RankingResponse.builder()
//                        .studentClass(ranking.getStudent().getStudentClass())
//                        .asiaGpa(ranking.getAsiaGpa())
//                        .studentCode(studentCode)
//                        .ranking(ranking.getRanking())
//                        .studentName(ranking.getStudent().getStudentName())
//                        .gpa(ranking.getGpa())
//                        .build();
//                responses.add(response);
//                if (count>=4) break;
//            }
//            if (responses.size()!=0&&count>=4) break;
//        }
        responses.add(new RankingResponse().formData(rankingList.get(1)));
        responses.add(new RankingResponse().formData(rankingList.get(0)));
        responses.add(new RankingResponse().formData(rankingList.get(2)));

        return responses;
    }

    @Override
    public List<RankingResponse> findBlockDetailRanking(String studentCode) {
        String firstCode = "";
        String secondCode = "";
        List<Ranking> rankingList = new ArrayList<>();
        for (Ranking ranking : this.list){
            if (ranking.getStudent().getStudentCode().contains(studentCode.substring(0,4))){
                rankingList.add(ranking);
            }
        }
        List<RankingResponse> responses = new ArrayList<>();
        Collections.sort(rankingList, Comparator.comparing(Ranking::getGpa).reversed());
        int cnt = 0;
        for (int i = 0; i<rankingList.size() ; i++) {
            Ranking ranking = rankingList.get(i);
            cnt++;
            ranking.setRanking(Long.parseLong(cnt+""));
            if (ranking.getStudent().getStudentCode().equals(studentCode)
                    || ranking.getStudent().getStudentCode().equalsIgnoreCase(studentCode.toLowerCase())) {
                responses.add(new RankingResponse().formData(ranking));
                if (cnt >= 4) break;
            }
            if (responses.size()!=0&&cnt>=4) break;
        }
        responses.add(new RankingResponse().formData(rankingList.get(1)));
        responses.add(new RankingResponse().formData(rankingList.get(0)));
        responses.add(new RankingResponse().formData(rankingList.get(2)));

        return responses;
    }


}
