package com.kaopiz.QLNCC.repository;

import com.kaopiz.QLNCC.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SupplierRepository
        extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    @EntityGraph(attributePaths = {"patterns"})
    Page<Supplier> findAll(Specification<Supplier> spec, Pageable pageable);
}
