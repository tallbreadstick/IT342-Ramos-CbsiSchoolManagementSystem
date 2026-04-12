package edu.cit.ramos.controller;

import edu.cit.ramos.dto.response.UserRecordResponse;
import edu.cit.ramos.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "*")
public class UserRecordController {

    private final UserRecordService userRecordService;

    @Autowired
    public UserRecordController(UserRecordService userRecordService) {
        this.userRecordService = userRecordService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserRecordResponse>> listAll() {
        List<UserRecordResponse> users = userRecordService.listAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserRecordResponse>> listPending() {
        List<UserRecordResponse> users = userRecordService.listPending();
        return ResponseEntity.ok(users);
    }
}
