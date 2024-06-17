package com.kodilla.books.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AddRecordsAirTableRequest(List<Record> records) {

    public record Record(Fields fields) {
    }

    public record Fields(
            @JsonProperty("Pomiar AQIUS") int pomiarAQIUS,
            @JsonProperty("Miasto") String miasto,
            @JsonProperty("Temperatura") int temperatura) {
    }
}