package com.back_end_TN.project_tn.services.auth;

import com.back_end_TN.project_tn.dtos.request.*;
import com.back_end_TN.project_tn.dtos.response.CommonResponse;
import com.back_end_TN.project_tn.dtos.response.TokenResponse;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    TokenResponse authenticate(SignInRequest signInRequest);
    TokenResponse refresh(HttpServletRequest request);
    CommonResponse registerUser(RegisterRequest registerRequest);
    CommonResponse forgotPassworrdUser(ForgotPasswordRequest forgotPasswordRequest);
    ResponseEntity<CommonResponse> verifyOtpForgotPassword(VerifyOtpRequest verifyOtpRequest);
    ResponseEntity<CommonResponse> verifyOtpRegisteredUser(VerifyOtpRequest verifyOtpRequest);
    FirebaseToken verifyToken(String token);
    ResponseEntity<?> handleLoginOauth(OauthFireBaseRequest oauthFireBaseRequest);
}
