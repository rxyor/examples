package com.github.rxyor.examples.hibernate.validator;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;

/**
 *<p>
 *
 *</p>
 *
 * @author liuyang
 * @since 2021-04-07 v1.0
 */
public class ValidatorsTest {

    @Test
    public void validate() {
        Validators.FailNotFastValidator.validate(new Bean());
    }

    public static class Bean {
        @NotBlank
        private String nickname;

        @NotBlank(message = "姓名不能为空")
        private String name;

        @Range(min = 0L, max = 150L, message = "年龄应该在{min}~{max}之间")
        private Integer age = 200;
    }
}