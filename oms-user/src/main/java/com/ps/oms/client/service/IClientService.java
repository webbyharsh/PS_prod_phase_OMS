package com.ps.oms.client.service;

import java.io.IOException;

import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import com.ps.oms.client.dto.BulkUploadResponse;
import com.ps.oms.client.entities.BulkUploadData;
import com.ps.oms.client.entities.Client;


public interface IClientService {
	
	boolean emailAlreadyExists(String emailId);
	
	BulkUploadResponse saveCsvClients(BulkUploadData bulkUploadData, MultipartFile file, Long userId) throws IOException;
	
	Client saveNewClient(CSVRecord csvRecord, Long userId);
	
	Integer getClientCount();
	

}
