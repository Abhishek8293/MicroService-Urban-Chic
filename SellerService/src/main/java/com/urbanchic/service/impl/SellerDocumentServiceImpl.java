package com.urbanchic.service.impl;

import com.urbanchic.dto.ImageUploadResponseDto;
import com.urbanchic.dto.SellerDocumentDto;
import com.urbanchic.dto.UpdateSellerDocumentDto;
import com.urbanchic.entity.SellerDocument;
import com.urbanchic.event.SellerDocumentAndAddressDeleteEvent;
import com.urbanchic.event.UpdateSellerAccountStatusEvent;
import com.urbanchic.exception.EntityAlreadyExistException;
import com.urbanchic.exception.EntityNotFoundException;
import com.urbanchic.repository.SellerDocumentRepository;
import com.urbanchic.service.SellerAddressService;
import com.urbanchic.service.SellerDocumentService;
import com.urbanchic.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerDocumentServiceImpl implements SellerDocumentService {

    private final ApplicationEventPublisher eventPublisher;
    private final SellerDocumentRepository sellerDocumentRepository;
    private final SellerAddressService sellerAddressService;

    @Override
    public SellerDocument addSellerDocument(SellerDocumentDto sellerDocumentDto) {
        SellerDocument existingSellerDocument = sellerDocumentRepository
                .findByGstNumber(sellerDocumentDto.getGstNumber()).orElse(null);
        if (existingSellerDocument != null){
            throw new EntityAlreadyExistException("GST Number is already used");
        }

        SellerDocument newSellerDocument = SellerDocument.builder()
                .companyName(sellerDocumentDto.getCompanyName())
                .companyLogoUrl(sellerDocumentDto.getCompanyLogoUrl())
                .companyLogoPublicId(sellerDocumentDto.getCompanyLogoPublicId())
                .gstNumber(sellerDocumentDto.getGstNumber())
                .panNumber(sellerDocumentDto.getPanNumber())
                .accountNumber(sellerDocumentDto.getAccountNumber())
                .ifscCode(sellerDocumentDto.getIfscCode())
                .sellerId(sellerDocumentDto.getSellerId())
                .build();
        SellerDocument savedSellerDocument = sellerDocumentRepository.save(newSellerDocument);
        sellerAddressService.addSellerAddress(sellerDocumentDto.getSellerAddress());
        eventPublisher.publishEvent(new UpdateSellerAccountStatusEvent(this, savedSellerDocument.getSellerId()));
        return savedSellerDocument;
    }

    @Override
    public SellerDocument updateSellerDocument(String sellerId, UpdateSellerDocumentDto sellerDocumentDto) {
        SellerDocument existingSellerDocument = sellerDocumentRepository.findBySellerId(sellerId).orElseThrow(() ->
                new EntityNotFoundException("Seller Address Not Found"));

        existingSellerDocument.setCompanyName(sellerDocumentDto.getCompanyName());
        existingSellerDocument.setCompanyLogoUrl(sellerDocumentDto.getCompanyLogoUrl());
        existingSellerDocument.setGstNumber(sellerDocumentDto.getGstNumber());
        existingSellerDocument.setPanNumber(sellerDocumentDto.getPanNumber());
        existingSellerDocument.setAccountNumber(sellerDocumentDto.getAccountNumber());
        existingSellerDocument.setIfscCode(sellerDocumentDto.getIfscCode());
        SellerDocument updatedSellerDocument = sellerDocumentRepository.save(existingSellerDocument);
        return updatedSellerDocument;
    }

    @Override
    public SellerDocument getSellerDocumentBySellerId(String sellerId) {
        SellerDocument existingSellerDocument = sellerDocumentRepository.findBySellerId(sellerId).orElseThrow(() ->
                new EntityNotFoundException("Seller Address Not Found"));
        return existingSellerDocument;
    }

    @Override
    @Async
    @EventListener
    public String deleteSellerDocument(SellerDocumentAndAddressDeleteEvent event) {
        SellerDocument existingSellerDocument = sellerDocumentRepository.findBySellerId(event.getSellerId()).orElseThrow(() ->
                new EntityNotFoundException("Seller Address Not Found"));
        sellerDocumentRepository.delete(existingSellerDocument);
        return "Seller Id: "+event.getSellerId();
    }

    @Override
    public SellerDocument updateSellerImage(String sellerId, ImageUploadResponseDto imageUploadResponseDto) {
        SellerDocument existingSellerDocument = sellerDocumentRepository.findBySellerId(sellerId).orElseThrow(() ->
                new EntityNotFoundException("Seller Address Not Found"));

        existingSellerDocument.setCompanyLogoUrl(imageUploadResponseDto.getPublicUrl());
        existingSellerDocument.setCompanyLogoPublicId(imageUploadResponseDto.getPublicId());
        SellerDocument updatedSellerDocument = sellerDocumentRepository.save(existingSellerDocument);
        return updatedSellerDocument;
    }
}
