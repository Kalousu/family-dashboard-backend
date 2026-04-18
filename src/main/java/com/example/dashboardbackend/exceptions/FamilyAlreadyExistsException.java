package com.example.dashboardbackend.exceptions;

public class FamilyAlreadyExistsException extends RuntimeException {
    public FamilyAlreadyExistsException(String familyName) {
        super("Family with name " + familyName + " already exists");
    }
}
