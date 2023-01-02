package com.bankProject.tekanaeWallet.validation.uuidValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UuidValidator implements ConstraintValidator<Uuid, UUID> {
    private final String regex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"; // the regex

    @Override
    public void initialize(Uuid validUuid) { }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext cxt) {
        return uuid.toString().matches(this.regex);
    }
}
