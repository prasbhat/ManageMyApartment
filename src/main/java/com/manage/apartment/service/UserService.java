package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.controller.UserController;
import com.manage.apartment.model.ResidentUsers;
import com.manage.apartment.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static com.manage.apartment.Util.ManageMyApartmentConstants.MODEL_LOGIN_USER;

@Service
@SessionAttributes(MODEL_LOGIN_USER)
public class UserService implements ManageMyApartmentConstants {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @ModelAttribute(value = MODEL_LOGIN_USER)
    private ResidentUsers createNewUserObj() {
        return new ResidentUsers();
    }

    public ResidentUsers findOneUser(int userId){
        return userRepository.findOne(userId);
    }

    public List<ResidentUsers> findAllUsers(){
        return (List<ResidentUsers>) userRepository.findAll();
    }

    public ResidentUsers findUsersByEmailAddress(String emailAddress){
        return userRepository.findByEmailAddr(emailAddress);
    }

    public List<ResidentUsers> findUsersByFlatNumber (int flatNumber) {
        return userRepository.findByFlatNumber(flatNumber);
    }

    public List<ResidentUsers> findByEmailAddrOrFlatNumberAndAdditionalUserDetails_IsActive (String emailAddress, int flatNumber){
        return userRepository.findByEmailAddrOrFlatNumberAndAdditionalUserDetails_IsActive(emailAddress, flatNumber, Boolean.TRUE);
    }

    public void createUser(ResidentUsers residentUsers) {
        userRepository.save(residentUsers);
    }

    public List<ResidentUsers> getAllUserList () {
        List<ResidentUsers> residentUsersList = findAllUsers();
        List<ResidentUsers> residentUsersList1 = new ArrayList<>();

        for(ResidentUsers residentUsers: residentUsersList) {
            if(residentUsers.getFlatNumber() != 0 ) {
                residentUsersList1.add(residentUsers);
            }
        }
        return residentUsersList1;
    }

    public ModelAndView callGetAllUsers(@ModelAttribute(value = MODEL_LOGIN_USER) ResidentUsers userSessObj){
        return userController.getAllUsers(Boolean.FALSE.toString(), userSessObj);
    }
}
