package com.siyecaoi.yy.utils.spripaar;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.lang.annotation.Annotation;

/**
 * 创建日期：2018/5/14 1:39
 *
 * @author howchange
 */
public class IsIdCardRule extends AnnotationRule<IsIdCard, String> {
    String expression = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * Constructor. It is mandatory that all subclasses MUST have a constructor with the same
     * signature.
     *
     * @param isIdCard The rule {@link Annotation} instance to which
     *                 this rule is paired.
     */
    protected IsIdCardRule(IsIdCard isIdCard) {
        super(isIdCard);
    }

    @Override
    public boolean isValid(String data) {
        return data.matches(expression);
    }
}
