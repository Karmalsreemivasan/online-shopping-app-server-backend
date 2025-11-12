package com.seller_details.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.seller_details.model.Status_idModel;

@Repository
public interface Status_idRepositary extends JpaRepository<Status_idModel, Long> {
}
