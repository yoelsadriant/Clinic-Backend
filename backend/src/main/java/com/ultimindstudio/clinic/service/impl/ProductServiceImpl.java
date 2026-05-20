package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.ProductRequest;
import com.ultimindstudio.clinic.dto.response.ProductResponse;
import com.ultimindstudio.clinic.entity.Product;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.ProductRepository;
import com.ultimindstudio.clinic.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public ProductResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public ProductResponse create(ProductRequest req) {
        Product p = Product.builder().name(req.name()).quantity(req.quantity())
                .price(req.price()).weight(req.weight()).build();
        return toResponse(productRepository.save(p));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest req) {
        Product p = getById(id);
        p.setName(req.name()); p.setQuantity(req.quantity());
        p.setPrice(req.price()); p.setWeight(req.weight());
        return toResponse(productRepository.save(p));
    }

    @Override
    public void delete(Long id) { getById(id); productRepository.deleteById(id); }

    private Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getQuantity(), p.getPrice(), p.getWeight());
    }
}
