package com.example.{{package}};

import com.example.shared.infrastructure.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface {{EntityName}}Repository extends JpaRepository<{{EntityName}}Entity, UUID>,
        JpaSpecificationExecutor<{{EntityName}}Entity> {
}
