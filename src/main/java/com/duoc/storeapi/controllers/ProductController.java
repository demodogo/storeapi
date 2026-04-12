package com.duoc.storeapi.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.storeapi.dto.ProductSearchRequest;
import com.duoc.storeapi.dto.ProductUpdateRequest;
import com.duoc.storeapi.models.Product;
import com.duoc.storeapi.services.ProductService;
import com.duoc.storeapi.utils.AuthUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAuthedUser(headerUserId, role);
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@Valid ProductSearchRequest params, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAuthedUser(headerUserId, role);
        return ResponseEntity.ok(productService.search(params));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAuthedUser(headerUserId, role);
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAdminRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest product, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAdminRole(role);
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader(value="X-User-Role", required=false) String role, @RequestHeader(value="X-User-Id", required=false) Long headerUserId) {
        AuthUtil.requireAdminRole(role);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}