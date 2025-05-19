package com.talenttap.exception;

public class JobFetchException extends TalentTapException{
    public JobFetchException(String message) {
    	// Bad Gateway (the server asked for help dint respond to me properly)
        super(message, 502);
    }
}