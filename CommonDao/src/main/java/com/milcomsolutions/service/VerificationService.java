package com.milcomsolutions.service;

import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.VerificationCode;
import com.milcomsolutions.entity.core.VerificationType;

public interface VerificationService {

	VerificationCode createAndSaveVerificationCode(User user) throws Exception;

	boolean verify(VerificationType userreg, String verificationCode) throws Exception;

	boolean cancelVerify(VerificationType userreg, String verificationCode) throws Exception;

}
