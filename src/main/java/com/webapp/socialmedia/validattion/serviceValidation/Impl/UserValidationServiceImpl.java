package com.webapp.socialmedia.validattion.serviceValidation.Impl;

import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.validattion.serviceValidation.UserValidationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidationServiceImpl implements UserValidationService {
    @Override
    public boolean isUserMatchToken(String userId){
        User userInToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Objects.equals(userInToken.getId(), userId);
    }

    @Override
    public boolean isUserMatchToken(User user){
        User userInToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Objects.equals(userInToken.getId(), user.getId());
    }
}
