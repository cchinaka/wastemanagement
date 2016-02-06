package com.milcomsolutions.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milcomsolutions.entity.core.License;
import com.milcomsolutions.service.LicenseService;


@Service
@Transactional
public class LicenseServiceImpl extends GenericServiceImpl implements LicenseService {

    private static final Log LOG = LogFactory.getLog(LicenseServiceImpl.class);

    @Value("company.code")
    private String companyCode;


    public boolean verifyLicense() {
        boolean ok = false;
        File licenseFile = new File(getConfigurationManager().getProperty("REMITA_HOME") + File.separator + "config" + File.separator + "remita.lic");
        if (licenseFile.exists()) {
        	LicenseServiceImpl.LOG.info("License file found");
        	Map<String,Object> params=new HashMap<String,Object>();
    		params.put("companyId", companyCode);
    		params.put("active", Boolean.TRUE);
    		License linces = findEntityByParams(License.class, params);
    		LicenseServiceImpl.LOG.info("Verify licences in progress");
    		if(linces!=null){
    			
    		}else{
    			//TODO try to load the lic file to the DB
    		}
        }
        return ok;
    }



	@Override
	public boolean isInstalled() {
		
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("companyId", companyCode);
		params.put("active", Boolean.TRUE);
		License linces = findEntityByParams(License.class, params);
		return linces!=null;
	}
}
