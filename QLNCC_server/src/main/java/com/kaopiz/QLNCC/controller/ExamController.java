package com.kaopiz.QLNCC.controller;

import com.kaopiz.QLNCC.model.request.ExamSubmitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/exam")
public class ExamController {

    @PostMapping("/submit")
    public ResponseEntity<Void> submit(@RequestBody ExamSubmitRequest body) {
        log.info("Exam FE -> BE (no persist): title={}, note={}", body.title(), body.note());
        return ResponseEntity.noContent().build();
    }
}
