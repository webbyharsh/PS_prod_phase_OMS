package com.ps.oms.client.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class ErrorGroup {
	
	private List<Integer> csvErrors;
	
	private List<Integer> emailIdErrors;
	
	private List<Integer> emailIdExistsErrors;
	
	private List<Integer> contactNumberErrors;
	
	private Map<String, List<Integer>> csvDuplicateEmailIdErrors;

}
