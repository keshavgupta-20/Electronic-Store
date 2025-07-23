package com.org.Electronic.store.repository;

import com.org.Electronic.store.entites.ContactDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactDetailRepo  extends JpaRepository<ContactDetail, String> {
    List<ContactDetail> findByUserUserId(String userId);
}
