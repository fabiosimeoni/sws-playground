package org.fao.sws.model.configuration;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import lombok.experimental.UtilityClass;

import org.fao.sws.model.common.Group;

@UtilityClass
public class Validators {
	
	//global group
	public interface Global {}
	public interface Full {}
	
	
	@Constraint(validatedBy = EmptyGroupValidator.class)
	@Target(FIELD)
	@Retention(RUNTIME)
	public @interface NotEmptyGroup {
	    String message() default "group cannot be empty";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
	}
	
	
	@ApplicationScoped
	public class EmptyGroupValidator implements ConstraintValidator<NotEmptyGroup, Group<?>>
	{

		@Override
		public void initialize(NotEmptyGroup constraintAnnotation) {}

		@Override
		public boolean isValid(Group<?> value, ConstraintValidatorContext context) {
			return value!=null && !value.all().isEmpty();
		}
	  
	}

}
