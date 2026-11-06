package com.vanavil.itinerary.dto;

public class ItineraryRequest {
    private String clientName;
    private String travelDates;
    private String rawItinerary;

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getTravelDates() {
        return travelDates;
    }
    public void setTravelDates(String travelDates) {
        this.travelDates = travelDates;
    }
    public String getRawItinerary() {
        return rawItinerary;
    }
    public void setRawItinerary(String rawItinerary) {
        this.rawItinerary = rawItinerary;
    }

    
}
