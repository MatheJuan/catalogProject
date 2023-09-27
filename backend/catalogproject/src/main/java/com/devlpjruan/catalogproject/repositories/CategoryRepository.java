package com.devlpjruan.catalogproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devlpjruan.catalogproject.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
