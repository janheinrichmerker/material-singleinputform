package com.heinrichreimersoftware.singleinputform.steps;

/**
 * Created by user on 6/3/2015.
 */
public interface StepCheckerCallback {
    public void onInputValid();
    public void onInputInvalid();
    public void onInputInvalid(String error);
}
