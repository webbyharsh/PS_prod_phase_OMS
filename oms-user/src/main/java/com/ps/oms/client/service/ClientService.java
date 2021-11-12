package com.ps.oms.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ps.oms.client.dto.BulkUploadResponse;
import com.ps.oms.client.entities.Address;
import com.ps.oms.client.entities.BulkUploadData;
import com.ps.oms.client.entities.Client;
import com.ps.oms.client.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientService implements IClientService {

	@Autowired
	ClientRepository clientRepository;

	// Checks if emailId exists already in the table
	public boolean emailAlreadyExists(String emailId) {
		boolean exists;
		try {
			exists = clientRepository.existsByEmailId(emailId);
		} catch (Exception ex) {
			log.error("Exception occurred while checking if emailId" + emailId + " exists");
			exists = true;
		}
		return exists;

	}

	// Parses csv and to save clients
	public BulkUploadResponse saveCsvClients(BulkUploadData bulkUploadData, MultipartFile file, Long userId)
			throws IOException {

		log.info("saveCsvClients method executed");
		BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
		CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

		Iterator<CSVRecord> csvRecords = csvParser.iterator();

		Integer successful = 0;
		Integer rowNumber = 0;
		Map<Integer, String> failures = new HashMap<>();

		log.info("File Parsing loop executed in saveCsvClients method");

		while (csvRecords.hasNext()) {

			rowNumber += 1;
			CSVRecord csvRecord = csvRecords.next();

			try {
				saveNewClient(csvRecord, userId);
				successful += 1;
			} catch (Exception ex) {
				log.info("excetion occured while saving record " + rowNumber + " in database");
				failures.put(rowNumber, ex.getLocalizedMessage());
			}
		}

		csvParser.close();

		BulkUploadResponse bulkUploadResponse = new BulkUploadResponse();
		bulkUploadResponse.setTotal(bulkUploadData.getTotal());
		bulkUploadResponse.setSuccessful(successful);
		if (failures.size() > 0) {
			bulkUploadResponse.setFailures(failures);
		}
		bulkUploadResponse.setMessage("" + successful + " Rows Saved And " + failures.size() + " Failures");

		return bulkUploadResponse;

	}

	// saves a single client to table
	public Client saveNewClient(CSVRecord csvRecord, Long userId) {

		String name = csvRecord.get("name");
		String emailId = csvRecord.get("emailId");
		String contactNumber = csvRecord.get("contactNumber");
		String state = csvRecord.get("state");
		String city = csvRecord.get("city");
		String street = csvRecord.get("street");
		String country = csvRecord.get("country");

		Address newAddress = new Address(street, city, state, country);
		Client newClient = new Client(name, emailId, contactNumber, newAddress, userId);
		return clientRepository.save(newClient);

	}

	// Gives to total number of clients in table
	public Integer getClientCount() {

		Long count = clientRepository.count();
		log.info("getClientCount method executed and returning number of entries in database");
		return count.intValue();
	}

	public List<Client> getallClientsWithName(String keyword) {
		if (keyword != null) {
			return clientRepository.search(keyword);
		}
		return clientRepository.findAll();

	}

}
