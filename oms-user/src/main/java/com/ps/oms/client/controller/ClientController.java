package com.ps.oms.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ps.oms.client.dto.BulkUploadErrorResponse;
import com.ps.oms.client.dto.BulkUploadResponse;
import com.ps.oms.client.dto.IBulkUploadResponse;
import com.ps.oms.client.entities.BulkUploadData;
import com.ps.oms.client.entities.Client;
import com.ps.oms.client.entities.ClientDropdown;
import com.ps.oms.client.exceptions.IncorrectCsvHeaderException;
import com.ps.oms.client.service.ClientService;
import com.ps.oms.client.service.ClientValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(value = "*")
public class ClientController {

	@Autowired
	ClientService clientService;

	@Autowired
	ClientValidator clientValidator;

	// Post method to save csv data to table
	@PostMapping("/client/upload-csv")
	public ResponseEntity<IBulkUploadResponse> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestHeader("userId") Long userId) throws IOException, IncorrectCsvHeaderException {

		log.info("User submitted Multipart form with file");

		clientValidator.hasCSVFormat(file);

		BulkUploadData bulkUploadData = clientValidator.csvFileValidation(file);
		log.info("CSV file validated by ClientValidator");

		if (bulkUploadData.isValidCSV()) {
			BulkUploadResponse bulkUploadResponse = clientService.saveCsvClients(bulkUploadData, file, userId);
			log.info("Clients saved to table and returning Response");
			return new ResponseEntity<>(bulkUploadResponse, HttpStatus.CREATED);
		} else {
			BulkUploadErrorResponse bulkUploadErrorResponse = bulkUploadData.getBulkUploadErrorResponse();
			log.info("File is not a valid CSV and returning Error Response");
			return new ResponseEntity<>(bulkUploadErrorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	// Get method to get number of saved clients
	@GetMapping("/client-count")
	public Integer getClientCount() {
		log.info("User sent a get client count request");
		return clientService.getClientCount();
	}

	@GetMapping("/client")
	public ResponseEntity<List<ClientDropdown>> searchClientByName(@RequestParam("keyword") String keyword) {
		List<Client> clients = clientService.getallClientsWithName(keyword);

		List<ClientDropdown> clientlist = new ArrayList<>();

		for (int i = 0; i < clients.size(); i++) {
			ClientDropdown clientdropdown = new ClientDropdown();
			clientdropdown.setId(clients.get(i).getClientId());
			clientdropdown.setName(clients.get(i).getName());

			clientlist.add(clientdropdown);
		}

		if (clientlist.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.of(Optional.of(clientlist));
	}

}
