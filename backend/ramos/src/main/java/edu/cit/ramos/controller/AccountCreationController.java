package edu.cit.ramos.controller;

import edu.cit.ramos.dto.request.AccountCreationRequest;
import edu.cit.ramos.dto.response.AccountCreationResponse;
import edu.cit.ramos.service.AccountCreationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
@CrossOrigin(origins = "*")
public class AccountCreationController {

    private static final Logger logger = LoggerFactory.getLogger(AccountCreationController.class);

    private final AccountCreationService accountCreationService;

    @Autowired
    public AccountCreationController(AccountCreationService accountCreationService) {
        this.accountCreationService = accountCreationService;
    }

    @PostMapping("/single")
    public ResponseEntity<AccountCreationResponse> createSingle(@RequestBody AccountCreationRequest req) {
        logger.info("Account creation request received: email={}", req.getEmail());
        AccountCreationResponse resp;
        try {
            resp = accountCreationService.createSingle(req);
        } catch (IllegalArgumentException ex) {
            logger.warn("Account creation validation failed: {}", ex.getMessage());
            AccountCreationResponse r = new AccountCreationResponse();
            r.setSuccess(false);
            r.setMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(r);
        } catch (Exception ex) {
            logger.error("Unexpected error during account creation", ex);
            AccountCreationResponse r = new AccountCreationResponse();
            r.setSuccess(false);
            r.setMessage("Internal server error");
            return ResponseEntity.status(500).body(r);
        }

        if (!resp.isSuccess()) return ResponseEntity.badRequest().body(resp);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<AccountCreationResponse>> createBatch(@RequestPart("file") MultipartFile file) {
        try {
            List<AccountCreationResponse> results = accountCreationService.createBatch(file);
            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException ex) {
            logger.warn("Batch upload failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            logger.error("Unexpected error in batch upload", ex);
            return ResponseEntity.status(500).build();
        }
    }
}
