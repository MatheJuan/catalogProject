package com.devlpjruan.catalogproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devlpjruan.catalogproject.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
