package com.duoc.storeapi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.storeapi.dto.ProductUpdateRequest;
import com.duoc.storeapi.exceptions.ResourceNotFoundException;
import com.duoc.storeapi.models.Product;
import com.duoc.storeapi.repositories.ProductRepository;

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