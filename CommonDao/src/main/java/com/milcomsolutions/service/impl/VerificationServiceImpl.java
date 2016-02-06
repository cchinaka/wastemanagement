package com.milcomsolutions.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.milcomsolutions.commons.security.SecurityUtil;
import com.milcomsolutions.entity.core.User;
import com.milcomsolutions.entity.core.VerificationCode;
import com.milcomsolutions.entity.core.VerificationType;
import com.milcomsolutions.service.UserService;
import com.milcomsolutions.service.VerificationService;


@Service("verificationService")
public class VerificationServiceImpl extends GenericServiceImpl implements VerificationService {

	@Autowired
	private UserService userService;
	
	private static final Log LOG = LogFactory.getLog(VerificationServiceImpl.class);
	
	
	@Override
	public VerificationCode createAndSaveVerificationCode(User user) throws Exception {
		VerificationCode vCode=new VerificationCode();
		String code=SecurityUtil.MD5Encode(UUID.randomUUID().toString());
		vCode.setActive(true);
		vCode.setVerificationCode(code);
		vCode.setVerificationItemId(user.getId());
		vCode.setVerifivationType(VerificationType.USERREG);
		vCode.setOwner(user.getUsername());
		vCode=saveEntity(vCode);
		return vCode;
	}

	@Override
	public boolean verify(VerificationType userreg, String verificationCode) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("verifivationType", userreg);
        params.put("verificationCode", verificationCode);
        VerificationCode vCode = findEntityByParams(VerificationCode.class, params);
        boolean valid=true;
        if(vCode.getExpirationDate()!=null){
        	Date tday=new Date();
        	valid=tday.after(vCode.getExpirationDate());
        }
        if(valid){
	        switch(userreg){
	        	case USERREG:
	        		User user =userService.findUserByUsername(vCode.getOwner());
	        		LOG.info(String.format("verifing user registration %s", user.getUserDetail().getEmail()));
	        		if(!user.isVerified()){
	        			user.setVerified(true);
	        			user.setAccountNonLocked(true);
	        			userService.saveUser(user, false);
		        		//TODO add activity log
	        		}else{
	        			LOG.info(String.format("User email %s already verified", user.getUserDetail().getEmail()));
	        		}

	        		break;
	        }
        }
        
		return valid;
	}

	@Override
	public boolean cancelVerify(VerificationType userreg,String verificationCode) throws Exception{
		
		  Map<String, Object> params = new HashMap<String, Object>();
	        params.put("verifivationType", userreg);
	        params.put("verificationCode", verificationCode);
	        VerificationCode vCode = findEntityByParams(VerificationCode.class, params);
	        boolean valid=true;
	        if(vCode.getExpirationDate()!=null){
	        	Date tday=new Date();
	        	valid=tday.after(vCode.getExpirationDate());
	        }
	        if(valid){
		        switch(userreg){
		        	case USERREG:
		        		User user =userService.findUserByUsername(vCode.getOwner());
		        		LOG.info(String.format("Cancelling user registration %s", user.getUserDetail().getEmail()));
		        		if(!user.isVerified()){
		        			userService.removeEntity(user);
		        			//TODO add activity log
		        		}

		        		break;
		        }
	        }
	        
			return valid;
	}

   
}
