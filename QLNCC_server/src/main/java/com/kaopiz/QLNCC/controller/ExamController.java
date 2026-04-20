package com.kaopiz.QLNCC.controller;

import com.kaopiz.QLNCC.model.request.ExamSubmitRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {

    @PostMapping("/submit")
    public ResponseEntity<Void> submit(@RequestBody ExamSubmitRequest body) {
        log.info("Exam FE -> BE (no persist): title={}, note={}", body.title(), body.note());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public String getMe() {
        return "I AM DEV";
    }

    @GetMapping("/role")
    public String getRole() {
        return "USER 3";
    }
}
