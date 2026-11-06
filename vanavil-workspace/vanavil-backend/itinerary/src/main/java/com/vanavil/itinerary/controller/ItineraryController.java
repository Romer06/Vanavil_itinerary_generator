package com.vanavil.itinerary.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vanavil.itinerary.dto.ItineraryRequest;
import com.vanavil.itinerary.service.PdfGeneratorService;

@RestController 
@RequestMapping("/api/itinerary")
@CrossOrigin(origins = {"http://localhost:5173" , "https://vanavil-itinerary-generator.vercel.app"} )
public class ItineraryController {
    
private final PdfGeneratorService pdfService;

public ItineraryController(PdfGeneratorService pdfService){
    this.pdfService = pdfService;
}

    @PostMapping ("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestBody ItineraryRequest request){
       byte[] pdfBytes = pdfService.generateItineraryPdf(request);

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_PDF);
       headers.setContentDispositionFormData("attachment", request.getClientName());

        return ResponseEntity.ok()
               .headers(headers)
               .body(pdfBytes);
    }
}
