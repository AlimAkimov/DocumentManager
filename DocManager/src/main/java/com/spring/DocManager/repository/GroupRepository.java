package com.spring.DocManager.repository;

import com.spring.DocManager.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
