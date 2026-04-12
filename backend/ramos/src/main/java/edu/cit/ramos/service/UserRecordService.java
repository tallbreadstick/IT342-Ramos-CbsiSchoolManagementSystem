package edu.cit.ramos.service;

import edu.cit.ramos.dto.response.UserRecordResponse;
import edu.cit.ramos.entity.User;
import edu.cit.ramos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import edu.cit.ramos.entity.AccountStatusType;

@Service
public class UserRecordService {

    private final UserRepository userRepository;

    @Autowired
    public UserRecordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserRecordResponse> listAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<UserRecordResponse> listPending() {
        List<User> users = userRepository.findByAccountStatus(AccountStatusType.PENDING);
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    private UserRecordResponse toDto(User u) {
        UserRecordResponse r = new UserRecordResponse();
        r.setId(u.getId());
        r.setSchoolId(u.getSchoolId());
        r.setEmail(u.getEmail());
        r.setFirstName(u.getFirstName());
        r.setLastName(u.getLastName());
        r.setMiddleName(u.getMiddleName());
        r.setSex(u.getSex());
        r.setDateOfBirth(u.getDateOfBirth());
        r.setPermanentAddress(u.getPermanentAddress());
        r.setCurrentAddress(u.getCurrentAddress());
        r.setDateCreated(u.getDateCreated());
        r.setDateUpdated(u.getDateUpdated());
        r.setAccountStatus(u.getAccountStatus());
        r.setLastLogin(u.getLastLogin());
        return r;
    }
}
