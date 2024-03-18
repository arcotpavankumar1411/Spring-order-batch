package com.training.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
