package com.vince.retailmanager.entity;

import javax.validation.groups.Default;

// validation group marker interfaces
public class Validation {
	public interface WebRequest {
	}

	public interface Entity extends Default {
	}
}