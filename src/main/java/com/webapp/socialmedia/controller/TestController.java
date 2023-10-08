package com.webapp.socialmedia.controller;

import com.webapp.socialmedia.dto.WrappingResponse;
import com.webapp.socialmedia.entity.User;
import com.webapp.socialmedia.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final IUserService userService;

    @GetMapping
    @WrappingResponse(status = HttpStatus.CREATED)
    public Object getAllUser() {
        List<User> result = userService.findAll();
        //return ResponseEntity.status(200).body(new AuthenticationRespone("1111111111", "12222222"));
        //ResponseEntity<Object> o = new ResponseEntity<>(new AuthenticationRespone("1111", "222") , HttpStatus.OK);
        //return new AuthenticationRespone("1111", "2222");
        return result;
    }
}
