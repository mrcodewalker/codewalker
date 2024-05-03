package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Schedule;
import com.example.codewalker.kma.responses.ScheduleResponse;

import java.util.List;

public interface IScheduleService {
    Schedule createSchedule(Schedule schedule);
    List<ScheduleResponse> findByStudentCourse(String studentCourse);
}
