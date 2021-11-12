package com.ps.oms.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.UnexpectedTypeException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ps.oms.client.entities.BulkUploadData;
import com.ps.oms.client.exceptions.IncorrectCsvHeaderException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientValidator {

	@Autowired
	IClientService clientService;

	//checks if file is of CSV format
	public boolean hasCSVFormat(MultipartFile file) {

		log.info("Checking if file is CSV or Not");
		
		String fileName = file.getOriginalFilename();
		
		if (fileName != null && fileName.endsWith(".csv")) {
			return true;
		} else {
			throw new UnexpectedTypeException("The given file is not of CSV Format");
		}
	}

	//checks if csv has correct headers
	public boolean hasCorrectHeaders(List<String> headers) {

		log.info("Checking if CSV file has correct headers or not");

		String headerString = "[name, emailId, contactNumber, street, city, state, country]";
		return Arrays.toString(headers.toArray()).equals(headerString);
	}

	
	//parses the entire file to find errors
	public BulkUploadData csvFileValidation(MultipartFile file) throws IOException, IncorrectCsvHeaderException {
		
		log.info("csvFileValidation method executed");

		BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
		CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());
		
		log.info("CSV Parser created");

		if (!hasCorrectHeaders(csvParser.getHeaderNames())) {
			csvParser.close();
			throw new IncorrectCsvHeaderException("File not CSV or CSV Headers Do not match required format");
		}

		Iterator<CSVRecord> csvRecords = csvParser.iterator();

		List<Integer> csvErrors = new ArrayList<>();
		List<Integer> emailIdErrors = new ArrayList<>();
		List<Integer> contactNumberErrors = new ArrayList<>();
		List<Integer> emailIdExistsErrors = new ArrayList<>();
		Set<Integer> validRows = new HashSet<>();
		Map<String, List<Integer>> csvEmails = new HashMap<>();

		Integer rowNumber = 0;
		
		log.info("Starting file parsing loop in csvFileValidation method");
		
		while (csvRecords.hasNext()) {

			CSVRecord csvRecord = csvRecords.next();
			rowNumber += 1;
			boolean valid = true;

			String emailId = csvRecord.get("emailId");
			String contactNumber = csvRecord.get("contactNumber");

			if (!isValidClientCsvLine(csvRecord)) {
				csvErrors.add(rowNumber);
				valid = false;
			} else {

				if (!isEmailAddress(emailId)) {
					emailIdErrors.add(rowNumber);
					valid = false;
					
				} else if (clientService.emailAlreadyExists(emailId)) {
					emailIdExistsErrors.add(rowNumber);
					valid = false;
				}

				if (!isContactNumber(contactNumber)) {
					contactNumberErrors.add(rowNumber);
					valid = false;
				}

				if (csvEmails.containsKey(emailId)) {
					validRows.remove(csvEmails.get(emailId).get(0));
					csvEmails.get(emailId).add(rowNumber);
					valid = false;
					
				} else {
					List<Integer> tempEmailGroup = new ArrayList<>();
					tempEmailGroup.add(rowNumber);
					csvEmails.put(emailId, tempEmailGroup);
				}

			}

			if (valid) {
				validRows.add(rowNumber);
			}

		}
		
		log.info("Starting file parsing loop in csvFileValidation method");

		csvParser.close();
		
		log.info("CSV parser closed");

		BulkUploadData bulkUploadData = new BulkUploadData();

		if (validRows.size() == rowNumber) {
			log.info("File validated successfully, no errors found");
		}
		bulkUploadData.setValidCSV(validRows.size() == rowNumber);
		bulkUploadData.setTotal(rowNumber);
		bulkUploadData.setValidRows(validRows);
		bulkUploadData.setCsvErrors(csvErrors);
		bulkUploadData.setEmailIdErrors(emailIdErrors);
		bulkUploadData.setEmailIdExistsErrors(emailIdExistsErrors);
		bulkUploadData.setContactNumberErrors(contactNumberErrors);
		bulkUploadData.setDuplicateEmailIdErrors(csvEmails);
		
		log.info("Created BulkUploadData object");

		return bulkUploadData;
	}

	// function for pattern email verification
	public boolean isEmailAddress(String emailAddress) {
		String expression = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		return emailAddress.matches(expression);

	}

	// function for pattern contact number verification
	public boolean isContactNumber(String contactNumber) {
		String expression = "(^$|[0-9]{10})";
		return contactNumber.matches(expression);
	}

	// validates a given csv line
	public boolean isValidClientCsvLine(CSVRecord csvRecord) {

		if (csvRecord.size() != 7) {
			return false;
		}

		String name = csvRecord.get("name");
		String emailId = csvRecord.get("emailId");
		String contactNumber = csvRecord.get("contactNumber");
		String state = csvRecord.get("state");
		String city = csvRecord.get("city");
		String street = csvRecord.get("street");
		String country = csvRecord.get("country");

		return !(name.isBlank() || emailId.isBlank() || contactNumber.isBlank() || state.isBlank() || city.isBlank()
				|| street.isBlank() || country.isBlank());
	}

}
