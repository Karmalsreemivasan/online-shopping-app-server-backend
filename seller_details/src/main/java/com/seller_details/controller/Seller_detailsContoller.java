package com.seller_details.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.seller_details.model.Seller_detailsModel;
import com.seller_details.service.Seller_detailService;

@RestController
@RequestMapping("/api/seller_details")
@CrossOrigin(origins = "http://localhost:3000")
public class Seller_detailsContoller {

	private final Seller_detailService service;

	@Autowired
	public Seller_detailsContoller(Seller_detailService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Seller_detailsModel> create(@RequestBody Seller_detailsModel seller) {
		Seller_detailsModel created = service.create(seller);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@GetMapping
	public List<Seller_detailsModel> all() {
		return service.findAll();
	}

	@GetMapping("/recent")
	public List<Seller_detailsModel> recent(@RequestParam(name = "limit", defaultValue = "5") int limit) {
		return service.findRecent(limit);
	}

	@GetMapping("/{id}")
	public Seller_detailsModel get(@PathVariable Long id) {
		return service.findById(id);
	}

	@PutMapping("/{id}")
	public Seller_detailsModel update(@PathVariable Long id, @RequestBody Seller_detailsModel seller) {
		return service.update(id, seller);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
