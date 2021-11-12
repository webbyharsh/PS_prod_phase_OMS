package com.ps.oms.client.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ps.oms.client.dto.BulkUploadErrorResponse;
import com.ps.oms.client.dto.ErrorGroup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BulkUploadData {
	
	private boolean isValidCSV;
	
	private Integer total;
	
	private Set<Integer> validRows;
	
	private List<Integer> csvErrors;
	
	private List<Integer> emailIdErrors;
	
	private List<Integer> emailIdExistsErrors;
	
	private List<Integer> contactNumberErrors;
	
	private Map<String, List<Integer>> csvDuplicateEmailIdErrors;

	
	public void setDuplicateEmailIdErrors(Map<String, List<Integer>> emailMap) {
		csvDuplicateEmailIdErrors = new HashMap<>();

		for (Map.Entry<String, List<Integer>> entry : emailMap.entrySet()) {
			if (entry.getValue().size() > 1) {
				csvDuplicateEmailIdErrors.put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public BulkUploadErrorResponse getBulkUploadErrorResponse() {
		BulkUploadErrorResponse bulkUploadErrorResponse = new BulkUploadErrorResponse();
		ErrorGroup errorGroup = new ErrorGroup();
		
		errorGroup.setContactNumberErrors(contactNumberErrors);
		errorGroup.setCsvDuplicateEmailIdErrors(csvDuplicateEmailIdErrors);
		errorGroup.setCsvErrors(csvErrors);
		errorGroup.setEmailIdExistsErrors(emailIdExistsErrors);
		errorGroup.setEmailIdErrors(emailIdErrors);
		
		bulkUploadErrorResponse.setErrors(errorGroup);
		bulkUploadErrorResponse.setTotal(total);
		bulkUploadErrorResponse.setValidRowCount(validRows.size());
		bulkUploadErrorResponse.setErrorRowCount(total - validRows.size());
		bulkUploadErrorResponse.setMessage("The Given CSV File Contains Errors");
		
		return bulkUploadErrorResponse;
		
		
	}

}
