package com.ps.oms.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateResponse {
	private String responseBody;

	@Override
	public String toString() {
		return "UserUpdateResponse [responseBody=" + responseBody + "]";
	}
}
