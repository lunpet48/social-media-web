package com.webapp.socialmedia.validattion.serviceValidation;

import com.webapp.socialmedia.entity.User;

public interface UserValidationService {

    boolean isUserMatchToken(String userId);

    boolean isUserMatchToken(User user);
}
