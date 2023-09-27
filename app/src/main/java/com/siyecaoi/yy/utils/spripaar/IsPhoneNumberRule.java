package com.siyecaoi.yy.utils.spripaar;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.lang.annotation.Annotation;

/**
 * 创建日期：2018/5/14 1:39
 *
 * @author howchange
 */
public class IsPhoneNumberRule extends AnnotationRule<IsPhoneNumber, String> {
    String expression = "(^(1[3-9])[0-9]{9}$)";
//    String expression = "^((13[0-9])|(14[4,5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199)\\d{8}$";

    /**
     * Constructor. It is mandatory that all subclasses MUST have a constructor with the same
     * signature.
     *
     * @param isPhoneNumber The rule {@link Annotation} instance to which
     *                      this rule is paired.
     */
    protected IsPhoneNumberRule(IsPhoneNumber isPhoneNumber) {

        super(isPhoneNumber);
    }

    @Override
    public boolean isValid(String data) {
        return data.matches(expression);
    }
}
