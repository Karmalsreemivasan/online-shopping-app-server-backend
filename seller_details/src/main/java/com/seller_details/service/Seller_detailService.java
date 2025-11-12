package com.seller_details.service;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.seller_details.model.Seller_detailsModel;
import com.seller_details.repositary.Seller_detailsRepositary;

@Service
public class Seller_detailService {

	private final Seller_detailsRepositary repo;

	@Autowired
	public Seller_detailService(Seller_detailsRepositary repo) {
		this.repo = repo;
	}

	public Seller_detailsModel create(Seller_detailsModel seller) {
		seller.setId(null);
		return repo.save(seller);
	}

	public List<Seller_detailsModel> findAll() {
		return repo.findAll();
	}

	public List<Seller_detailsModel> findRecent(int limit) {
		if (limit <= 0) {
			limit = 5;
		}
		return repo.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"))).getContent();
	}

	public Seller_detailsModel findById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seller not found"));
	}

	public Seller_detailsModel update(Long id, Seller_detailsModel seller) {
		Seller_detailsModel existing = findById(id);
		if (seller.getName() != null) existing.setName(seller.getName());
		if (seller.getAddress() != null) existing.setAddress(seller.getAddress());
		if (seller.getEmail() != null) existing.setEmail(seller.getEmail());
		if (seller.getPassword() != null) existing.setPassword(seller.getPassword());
		if (seller.getPhone() != null) existing.setPhone(seller.getPhone());
		if (seller.getStatusId() != null) existing.setStatusId(seller.getStatusId());
		return repo.save(existing);
	}

	public void delete(Long id) {
		Seller_detailsModel existing = findById(id);
		repo.delete(existing);
	}

}
