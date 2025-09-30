package org.example.auditservice.validator.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.auditservice.validator.Unique;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UniqueImpl implements ConstraintValidator<Unique, Object> {
    @PersistenceContext
    private EntityManager entityManager;

    private Class<?> entityClass;
    private String fieldName;

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.entityClass = constraintAnnotation.entity();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return true;
        String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName()
                + " e WHERE e." + fieldName + " = :value";

        Long count = (Long) entityManager.createQuery(jpql)
                .setParameter("value", value)
                .getSingleResult();

        return count == 0;
    }
}
