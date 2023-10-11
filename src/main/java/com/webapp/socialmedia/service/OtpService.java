package com.webapp.socialmedia.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class OtpService {
    public static final String REGISTER_KEY = "REGISTER_KEY";
    public static final String FORGOT_PASSWORD_KEY = "FORGOT_PASSWORD";
    private static final Integer EXPIRE_MINS = 5;

    private final LoadingCache<String, Integer> otpCache;

    @Autowired
    private MailService mailService;

    public OtpService(){
        otpCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void sendOtpRegister(String email){

        String OtpCode = String.valueOf(generateOTP(REGISTER_KEY+email));
        mailService.sendMail(email,"Mã OTP của bạn là " + OtpCode ,"Xác nhận tài khoản");
    }

    public void sendOtpForgotPassword(String email){

        String OtpCode = String.valueOf(generateOTP(FORGOT_PASSWORD_KEY+email));
        mailService.sendMail(email,"Mã OTP của bạn là " + OtpCode ,"Xác nhận tài khoản");
    }
    public int generateOTP(String key){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    public int getOtp(String key){
        try{
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }

    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}
