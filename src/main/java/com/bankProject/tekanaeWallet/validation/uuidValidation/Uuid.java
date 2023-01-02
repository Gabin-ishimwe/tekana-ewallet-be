package com.bankProject.tekanaeWallet.validation.uuidValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface Uuid {

    String message() default "{Invalid Uuid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
