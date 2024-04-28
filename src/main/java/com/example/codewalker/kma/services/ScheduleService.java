package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Schedule;
import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.ScheduleRepository;
import com.example.codewalker.kma.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.internal.util.StringHelper;
import org.modelmapper.internal.Pair;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService implements IScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    @Override
    public Schedule createSchedule(Schedule schedule) {
        if (scheduleRepository.findAll().contains(schedule)){
            return null;
        }
        List<Subject> subjectList = this.subjectRepository.findAll();
        List<String> subjectNames = new ArrayList<>();
        for (Subject clone : subjectList){
            subjectNames.add(clone.getSubjectName());
        }
        String validSubjectName = schedule.getSubjectName();
        int index = schedule.getSubjectName().indexOf("-");
        if (index != -1) {
            int lastIndex = schedule.getSubjectName().indexOf("-", index + 1);
            if (lastIndex != -1 && lastIndex-index>=3) {
                validSubjectName = schedule.getSubjectName().substring(0, lastIndex);
            } else {
                if (lastIndex!=-1 && lastIndex-index<3){
                    validSubjectName = schedule.getSubjectName().substring(0, index);
                }
            }
        }

        for (String subjectName : subjectNames){
            if (subjectName.equals(validSubjectName)
            || subjectName.equalsIgnoreCase(validSubjectName)
                ||  Normalizer.normalize(subjectName, Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(validSubjectName, Normalizer.Form.NFD).replaceAll("\\p{M}", "")))   {
                Subject subject = this.subjectRepository.findFirstBySubjectName(subjectName);
                subject.setSubjectCredits(schedule.getSubjectCredits());
                subject.setId(subject.getId());
                this.subjectRepository.save(subject);
                return this.scheduleRepository.save(schedule);
            }
        }
        this.subjectRepository.save(
                Subject.builder()
                        .subjectName(validSubjectName)
                        .subjectCredits(schedule.getSubjectCredits())
                        .build()
        );
        return this.scheduleRepository.save(schedule);
    }
}
