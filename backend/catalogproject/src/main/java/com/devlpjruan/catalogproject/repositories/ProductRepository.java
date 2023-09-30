package com.devlpjruan.catalogproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devlpjruan.catalogproject.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
