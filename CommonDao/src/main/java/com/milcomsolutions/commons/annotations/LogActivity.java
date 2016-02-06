package com.milcomsolutions.commons.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

import com.milcomsolutions.entity.core.ActivityLog.ActivityResult;
 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogActivity {
	public String value();
	public boolean enabled() default true;
	public ActivityResult result() default ActivityResult.UNKNOWN;
	public String description() default StringUtils.EMPTY;
}