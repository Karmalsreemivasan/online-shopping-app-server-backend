package com.seller_details.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import com.seller_details.model.Seller_detailsModel;

public interface Seller_detailsRepositary extends JpaRepository<Seller_detailsModel, Long> {
}
