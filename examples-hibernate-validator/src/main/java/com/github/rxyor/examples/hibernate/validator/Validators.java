package com.github.rxyor.examples.hibernate.validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

/**
 *<p>
 *  校验工具
 *</p>
 *
 * @author liuyang
 * @since 2021-04-07 v1.0
 */
public class Validators {

    private final static String MSG_JOINER = ";\n";

    private final Validator validator;

    public Validators() {
        this.validator = validator(true);
    }

    public Validators(boolean failFast) {
        this.validator = validator(failFast);
    }

    public Validators(Validator validator) {
        Preconditions.checkNotNull(validator, "validator不能为空");
        this.validator = validator;
    }

    private Validator getValidator() {
        return validator;
    }

    private Validator validator(boolean failFast) {
        ValidatorFactory validatorFactory = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .failFast(failFast)
            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    /**
     *<p>
     *  校验Bean
     *</p>
     *
     * @author liuyang
     * @since 2021-04-07 12:54:56 v1.0
     * @param object 校验对象
     * @param groups 分组
     */
    public void validate(Object object, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = this.getValidator().validate(object, groups);
        ifFailThrowException(constraintViolations);
    }

    /**
     *<p>
     *  校验Bean
     *</p>
     *
     * @author liuyang
     * @since 2021-04-07 12:55:38 v1.0
     * @param object 校验对象
     * @param propertyName 属性名
     * @param groups 分组
     */
    public void validate(Object object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = this.getValidator()
            .validateProperty(object, propertyName, groups);
        ifFailThrowException(constraintViolations);
    }

    /**
     *<p>
     *  校验Bean
     *</p>
     *
     * @author liuyang
     * @since 2021-04-07 12:56:19 v1.0
     * @param beanType beanType
     * @param propertyName 属性名
     * @param value value
     * @param groups 分组
     */
    public void validate(Class<Object> beanType, String propertyName, Object value, Class<?>... groups) {
        Set<ConstraintViolation<Object>> constraintViolations = this.getValidator()
            .validateValue(beanType, propertyName, value, groups);
        ifFailThrowException(constraintViolations);
    }

    /**
     * 解析校验异常信息
     *
     * @param constraintViolations 校验异常
     * @return [List<String>]
     */
    private List<String> parseMsg(Set<ConstraintViolation<Object>> constraintViolations) {
        return constraintViolations.stream()
            .map(ConstraintViolation::getMessage)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
    }

    /**
     * 抛出IllegalArgumentException
     *
     * @param constraintViolations 校验异常
     */
    private void ifFailThrowException(Set<ConstraintViolation<Object>> constraintViolations) {
        if (constraintViolations == null || constraintViolations.isEmpty()) {
            return;
        }
        List<String> msgList = this.parseMsg(constraintViolations);
        this.ifFailThrowException(msgList);
    }

    /**
     * 抛出IllegalArgumentException
     *
     * @param msgList 错误信息
     */
    private void ifFailThrowException(List<String> msgList) {
        if (msgList == null || msgList.isEmpty()) {
            return;
        }
        throw new IllegalArgumentException(Joiner.on(MSG_JOINER).join(msgList));
    }

    public static class FailFastValidator {
        private final static Validators VALIDATORS = new Validators(true);

        /**
         *<p>
         *  校验Bean
         *</p>
         *
         * @author liuyang
         * @since 2021-04-07 12:54:56 v1.0
         * @param object 校验对象
         * @param groups 分组
         */
        public static void validate(Object object, Class<?>... groups) {
            VALIDATORS.validate(object, groups);
        }

        /**
         *<p>
         *  校验Bean
         *</p>
         *
         * @author liuyang
         * @since 2021-04-07 12:55:38 v1.0
         * @param object 校验对象
         * @param propertyName 属性名
         * @param groups 分组
         */
        public void validate(Object object, String propertyName, Class<?>... groups) {
            VALIDATORS.validate(object, propertyName, groups);
        }

        /**
         *<p>
         *  校验Bean
         *</p>
         *
         * @author liuyang
         * @since 2021-04-07 12:56:19 v1.0
         * @param beanType beanType
         * @param propertyName 属性名
         * @param value value
         * @param groups 分组
         */
        public void validate(Class<Object> beanType, String propertyName, Object value, Class<?>... groups) {
            VALIDATORS.validate(beanType, propertyName, value, groups);
        }
    }

    public static class FailNotFastValidator {
        private final static Validators VALIDATORS = new Validators(false);

        /**
         *<p>
         *  校验Bean
         *</p>
         *
         * @author liuyang
         * @since 2021-04-07 12:54:56 v1.0
         * @param object 校验对象
         * @param groups 分组
         */
        public static void validate(Object object, Class<?>... groups) {
            VALIDATORS.validate(object, groups);
        }

        /**
         *<p>
         *  校验Bean
         *</p>
         *
         * @author liuyang
         * @since 2021-04-07 12:55:38 v1.0
         * @param object 校验对象
         * @param propertyName 属性名
         * @param groups 分组
         */
        public void validate(Object object, String propertyName, Class<?>... groups) {
            VALIDATORS.validate(object, propertyName, groups);
        }

        /**
         *<p>
         *  校验Bean
         *</p>
         *
         * @author liuyang
         * @since 2021-04-07 12:56:19 v1.0
         * @param beanType beanType
         * @param propertyName 属性名
         * @param value value
         * @param groups 分组
         */
        public void validate(Class<Object> beanType, String propertyName, Object value, Class<?>... groups) {
            VALIDATORS.validate(beanType, propertyName, value, groups);
        }
    }
}
