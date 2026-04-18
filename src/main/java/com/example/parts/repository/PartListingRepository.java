package com.example.parts.repository;

import com.example.parts.domain.AppUser;
import com.example.parts.domain.PartListing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PartListingRepository extends JpaRepository<PartListing, Long>, JpaSpecificationExecutor<PartListing> {

    List<PartListing> findByOrderByCreatedAtDesc();

    List<PartListing> findBySellerOrderByCreatedAtDesc(AppUser seller);
}
