package com.seller_details.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seller_details.model.Status_idModel;
import com.seller_details.repositary.Status_idRepositary;

@Service
public class status_idService {

	private final Status_idRepositary repo;

	@Autowired
	public status_idService(Status_idRepositary repo) {
		this.repo = repo;
	}

	public Status_idModel create(Status_idModel status) {
		status.setId(null);
		return repo.save(status);
	}

	public List<Status_idModel> findAll() {
		return repo.findAll();
	}

	public Status_idModel findById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found"));
	}

	public Status_idModel update(Long id, Status_idModel status) {
		Status_idModel existing = findById(id);
		if (status.getStatusName() != null) existing.setStatusName(status.getStatusName());
		if (status.getDescription() != null) existing.setDescription(status.getDescription());
		return repo.save(existing);
	}

	public void delete(Long id) {
		Status_idModel existing = findById(id);
		repo.delete(existing);
	}
}
