package com.duoc.storeapi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.storeapi.dto.ProductSearchRequest;
import com.duoc.storeapi.dto.ProductUpdateRequest;
import com.duoc.storeapi.exceptions.ResourceNotFoundException;
import com.duoc.storeapi.models.Product;
import com.duoc.storeapi.repositories.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        log.info("Obteniendo todos los productos");
        return productRepository.findAll();
    }

    public List<Product> search(ProductSearchRequest params) {
        log.info("Buscando productos con: {}", params);

        return productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getName() != null && !params.getName().isEmpty()) {
                String searchPattern = "%" + params.getName().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern)
                ));
            }

            if (params.getDescription() != null && !params.getDescription().isEmpty() && (params.getName() == null || params.getName().isEmpty())) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + params.getDescription().toLowerCase() + "%"));
            }

            if (params.getMinPrice() != null && params.getMaxPrice() != null) {
                if (params.getMinPrice().compareTo(params.getMaxPrice()) > 0) {
                    throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo");
                }
            }
            
            if (params.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), params.getMinPrice()));
            }
            if (params.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), params.getMaxPrice()));
            }

            if (params.getActive() != null) {
                predicates.add(cb.equal(root.get("active"), params.getActive()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Product findById(Long id) {
        log.info("Obteniendo producto con ID={}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + id));
    }

    public Product create(Product product) {
        log.info("Creando producto con nombre={}", product.getName());
        if (product.getActive() == null) {
            product.setActive(true);
        }
        Product savedProduct = productRepository.save(product);
        log.info("Producto creado con éxito con ID={}", savedProduct.getId());
        return savedProduct;
    }

    public Product update(Long id, ProductUpdateRequest product) {
        log.info("Actualizando producto con ID={}", id);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + id));
        existingProduct.setName(product.getName() != null ? product.getName() : existingProduct.getName());
        existingProduct.setDescription(product.getDescription() != null ? product.getDescription() : "");
        existingProduct.setPrice(product.getPrice() != null ? product.getPrice() : existingProduct.getPrice());
        existingProduct.setStock(product.getStock() != null ? product.getStock() : existingProduct.getStock());
        if (product.getActive() != null) {
            existingProduct.setActive(product.getActive());
        }
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Producto actualizado con éxito con ID={}", updatedProduct.getId());
        return updatedProduct;
    }

    public void delete(Long id) {
        log.info("Eliminando producto con ID={}", id);
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con el ID: " + id));
        productRepository.delete(existingProduct);
        log.info("Producto eliminado con éxito con ID={}", id);
    }
}