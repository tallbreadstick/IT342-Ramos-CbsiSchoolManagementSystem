package edu.cit.ramos.service;

import edu.cit.ramos.dto.request.AccountCreationRequest;
import edu.cit.ramos.dto.response.AccountCreationResponse;
import edu.cit.ramos.entity.AccountStatusType;
import edu.cit.ramos.entity.User;
import edu.cit.ramos.repository.UserRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class AccountCreationService {

    private final UserRepository userRepository;
    private final Argon2 argon2;

    private static final Logger logger = LoggerFactory.getLogger(AccountCreationService.class);

    @Autowired
    public AccountCreationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.argon2 = Argon2Factory.create();
    }

    public AccountCreationResponse createSingle(AccountCreationRequest req) {
        logger.debug("createSingle() called with payload: {}", req);
        // basic validation
        if (req == null) throw new IllegalArgumentException("Request body is required");
        if (req.getEmail() == null || req.getEmail().isBlank()) throw new IllegalArgumentException("Email is required");
        if (req.getFirstName() == null || req.getFirstName().isBlank()) throw new IllegalArgumentException("First name is required");
        if (req.getLastName() == null || req.getLastName().isBlank()) throw new IllegalArgumentException("Last name is required");
        if (req.getSex() == null) throw new IllegalArgumentException("Sex is required");
        if (req.getDateOfBirth() == null) throw new IllegalArgumentException("Date of birth is required");

        AccountCreationResponse resp = new AccountCreationResponse();
        if (userRepository.existsByEmail(req.getEmail())) {
            logger.info("createSingle() - email already exists: {}", req.getEmail());
            resp.setSuccess(false);
            resp.setMessage("Email already exists");
            return resp;
        }

        logger.debug("Creating account for payload: {}", req);

        int year = (req.getYearFirstEnrolled() == null) ? Year.now().getValue() : req.getYearFirstEnrolled();
        String roleCode = toRoleCode(req.getRole());
        String prefix = String.format("%02d%s", year % 100, roleCode);
        long count = userRepository.countBySchoolIdStartingWith(prefix);
        long counter = count + 1;
        String schoolId = String.format("%s%04d", prefix, counter);

        // ensure uniqueness (unlikely collision but safe)
        while (userRepository.existsBySchoolId(schoolId)) {
            counter++;
            schoolId = String.format("%s%04d", prefix, counter);
        }

        logger.info("createSingle() - generated schoolId={} (prefix={}, counter={}) for email={}", schoolId, prefix, counter, req.getEmail());

        // generate password
        String generatedPassword = generateRandomPassword(12);
        logger.debug("createSingle() - generated temporary password length={} for email={}", generatedPassword.length(), req.getEmail());
        String hash = argon2.hash(3, 65536, 1, generatedPassword);

        User u = new User();
        u.setSchoolId(schoolId);
        u.setEmail(req.getEmail());
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setMiddleName(req.getMiddleName());
        u.setSex(req.getSex());
        u.setDateOfBirth(req.getDateOfBirth());
        u.setPermanentAddress(req.getPermanentAddress());
        u.setCurrentAddress(req.getCurrentAddress());
        u.setPasswordHash(hash);
        u.setPasswordChanged(false);
        u.setDateCreated(OffsetDateTime.now());
        u.setDateUpdated(OffsetDateTime.now());
        u.setAccountStatus(AccountStatusType.PENDING);

        User saved;
        try {
            logger.debug("createSingle() - attempting to save user with schoolId={} email={}", u.getSchoolId(), u.getEmail());
            if (u.getSex() != null) {
                logger.debug("createSingle() - sex enum name={}, dbValue={}, displayName={}", u.getSex().name(), u.getSex().getDbValue(), u.getSex().getDisplayName());
            } else {
                logger.debug("createSingle() - sex is null");
            }
            saved = userRepository.save(u);
            logger.info("createSingle() - saved user id={} schoolId={} email={}", saved.getId(), saved.getSchoolId(), saved.getEmail());
        } catch (DataIntegrityViolationException ex) {
            logger.error("createSingle() - DataIntegrityViolation when saving user: {}", ex.getMessage(), ex);
            // rethrow so controller can return appropriate status
            throw ex;
        } catch (Exception ex) {
            logger.error("createSingle() - unexpected error saving user: {}", ex.getMessage(), ex);
            throw ex;
        }

        // Email sending is disabled for now. Log credentials to backend console instead.
        try {
            logger.info("Account created for email={} schoolId={} tempPassword={}", saved.getEmail(), saved.getSchoolId(), generatedPassword);
            System.out.println(String.format("[ACCOUNT CREATION] email=%s schoolId=%s tempPassword=%s", saved.getEmail(), saved.getSchoolId(), generatedPassword));
        } catch (Exception ex) {
            // logging should not break creation
            System.err.println("Failed to log credentials: " + ex.getMessage());
        }

        resp.setId(saved.getId());
        resp.setEmail(saved.getEmail());
        resp.setSchoolId(saved.getSchoolId());
        resp.setGeneratedPassword(generatedPassword);
        resp.setSuccess(true);
        resp.setMessage("Account created (PENDING)");
        return resp;
    }

    public List<AccountCreationResponse> createBatch(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase(Locale.ROOT);
        if (name.endsWith(".csv")) {
            return parseCsvAndCreate(file);
        } else if (name.endsWith(".xlsx") || name.endsWith(".xls")) {
            return parseExcelAndCreate(file);
        } else {
            throw new IllegalArgumentException("Unsupported file type; expected CSV or XLSX");
        }
    }

    private List<AccountCreationResponse> parseCsvAndCreate(MultipartFile file) throws Exception {
        List<AccountCreationResponse> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String header = br.readLine();
            if (header == null) return results;
            String line;
            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",");
                AccountCreationRequest req = mapColsToRequest(cols);
                results.add(createSingle(req));
            }
        }
        return results;
    }

    private List<AccountCreationResponse> parseExcelAndCreate(MultipartFile file) throws Exception {
        List<AccountCreationResponse> results = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) return results; // empty
            rows.next(); // skip header
            while (rows.hasNext()) {
                Row r = rows.next();
                // Expect columns in order: email,firstName,lastName,middleName,sex,dateOfBirth,permanentAddress,currentAddress,role,yearFirstEnrolled
                String[] cols = new String[10];
                for (int i = 0; i < cols.length; i++) {
                    try {
                        if (r.getCell(i) != null)
                            cols[i] = r.getCell(i).toString();
                        else cols[i] = "";
                    } catch (Exception e) { cols[i] = ""; }
                }
                AccountCreationRequest req = mapColsToRequest(cols);
                results.add(createSingle(req));
            }
        }
        return results;
    }

    private AccountCreationRequest mapColsToRequest(String[] cols) {
        AccountCreationRequest req = new AccountCreationRequest();
        req.setEmail(cols.length > 0 ? cols[0].trim() : "");
        req.setFirstName(cols.length > 1 ? cols[1].trim() : "");
        req.setLastName(cols.length > 2 ? cols[2].trim() : "");
        req.setMiddleName(cols.length > 3 ? cols[3].trim() : null);
        // sex as 'Male' or 'Female'
        if (cols.length > 4) {
            String s = cols[4].trim();
            try { req.setSex(edu.cit.ramos.entity.SexType.fromString(s)); } catch (Exception e) { req.setSex(null); }
        }
        if (cols.length > 5) {
            try { req.setDateOfBirth(java.time.LocalDate.parse(cols[5].trim())); } catch (Exception e) { req.setDateOfBirth(null); }
        }
        req.setPermanentAddress(cols.length > 6 ? cols[6].trim() : null);
        req.setCurrentAddress(cols.length > 7 ? cols[7].trim() : null);
        req.setRole(cols.length > 8 ? cols[8].trim() : "S");
        if (cols.length > 9) {
            try { req.setYearFirstEnrolled(Integer.parseInt(cols[9].trim())); } catch (Exception e) { req.setYearFirstEnrolled(null); }
        }
        return req;
    }

    private String toRoleCode(String role) {
        if (role == null) return "S";
        role = role.trim().toUpperCase();
        if (role.equals("S") || role.startsWith("STUD")) return "S";
        if (role.equals("F") || role.startsWith("FAC")) return "F";
        if (role.equals("A") || role.startsWith("STA")) return "A";
        return "S";
    }

    private String generateRandomPassword(int length) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
