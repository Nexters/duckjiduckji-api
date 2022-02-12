package com.nexters.duckji.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.bson.types.ObjectId;

public class ObjectIdValidator implements ConstraintValidator<ObjectIdFormat, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value != null) {
			return ObjectId.isValid(value);
		}
		return true;
	}
}
