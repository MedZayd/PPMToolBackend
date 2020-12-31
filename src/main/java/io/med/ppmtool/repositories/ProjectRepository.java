package io.med.ppmtool.repositories;

import io.med.ppmtool.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByIdentifier(String identifier);

    @Query("SELECT p FROM Project p WHERE UPPER(p.name) LIKE %:keyword% or p.identifier LIKE %:keyword% ORDER BY p.createdAt DESC")
    List<Project> findByKeyword(String keyword);
}
