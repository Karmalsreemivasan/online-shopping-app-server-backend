package com.seller_details.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seller_details.model.Status_idModel;
import com.seller_details.service.status_idService;

@RestController
@RequestMapping("/api/status")
public class Status_idContoller {

	private final status_idService service;

	@Autowired
	public Status_idContoller(status_idService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Status_idModel> create(@RequestBody Status_idModel status) {
		Status_idModel created = service.create(status);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public List<Status_idModel> all() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Status_idModel get(@PathVariable Long id) {
		return service.findById(id);
	}

	@PutMapping("/{id}")
	public Status_idModel update(@PathVariable Long id, @RequestBody Status_idModel status) {
		return service.update(id, status);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
