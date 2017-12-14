package com.manage.apartment.service;

import com.manage.apartment.Util.ManageMyApartmentConstants;
import com.manage.apartment.model.Feedback;
import com.manage.apartment.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService implements ManageMyApartmentConstants {

    @Autowired
    FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedback(){
        return (List<Feedback>) feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbackByFlatNumber(int flatNumber){
        return feedbackRepository.findByFlatNumber(flatNumber);
    }

    public void createFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }
}
