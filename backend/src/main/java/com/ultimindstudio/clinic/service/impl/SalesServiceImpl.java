package com.ultimindstudio.clinic.service.impl;

import com.ultimindstudio.clinic.dto.request.SalesRequest;
import com.ultimindstudio.clinic.dto.response.SalesResponse;
import com.ultimindstudio.clinic.entity.*;
import com.ultimindstudio.clinic.exception.ResourceNotFoundException;
import com.ultimindstudio.clinic.repository.*;
import com.ultimindstudio.clinic.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final StaffRepository staffRepository;
    private final TypePaymentRepository typePaymentRepository;
    private final TypeIncomeRepository typeIncomeRepository;
    private final ClientRepository clientRepository;

    @Override @Transactional(readOnly = true)
    public Page<SalesResponse> findAll(Pageable pageable) {
        return salesRepository.findAll(pageable).map(this::toResponse);
    }

    @Override @Transactional(readOnly = true)
    public SalesResponse findById(Long id) { return toResponse(getById(id)); }

    @Override
    public SalesResponse create(SalesRequest req) {
        Sales sales = Sales.builder()
                .staff(findStaff(req.staffId())).date(req.date())
                .typePayment(findTypePayment(req.typePaymentId()))
                .typeIncome(findTypeIncome(req.typeIncomeId()))
                .client(findClient(req.clientId()))
                .price(req.price()).description(req.description()).build();
        return toResponse(salesRepository.save(sales));
    }

    @Override
    public SalesResponse update(Long id, SalesRequest req) {
        Sales s = getById(id);
        s.setStaff(findStaff(req.staffId())); s.setDate(req.date());
        s.setTypePayment(findTypePayment(req.typePaymentId()));
        s.setTypeIncome(findTypeIncome(req.typeIncomeId()));
        s.setClient(findClient(req.clientId()));
        s.setPrice(req.price()); s.setDescription(req.description());
        return toResponse(salesRepository.save(s));
    }

    @Override
    public void delete(Long id) { getById(id); salesRepository.deleteById(id); }

    private Sales getById(Long id) {
        return salesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sales", "id", id));
    }
    private Staff findStaff(Long id) {
        return staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff", "id", id));
    }
    private TypePayment findTypePayment(Long id) {
        return typePaymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TypePayment", "id", id));
    }
    private TypeIncome findTypeIncome(Long id) {
        return typeIncomeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("TypeIncome", "id", id));
    }
    private Client findClient(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }

    private SalesResponse toResponse(Sales s) {
        return new SalesResponse(s.getId(),
                s.getStaff().getId(), s.getStaff().getName(), s.getDate(),
                s.getTypePayment().getId(), s.getTypePayment().getName(),
                s.getTypeIncome().getId(), s.getTypeIncome().getName(),
                s.getClient().getId(), s.getClient().getName(),
                s.getPrice(), s.getDescription());
    }
}
