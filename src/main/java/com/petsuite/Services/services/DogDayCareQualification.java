package com.petsuite.Services.services;

import com.petsuite.Services.dto.DogDayCareInvoice_Dto;
import com.petsuite.basics.Cadena;

public interface DogDayCareQualification {

    Cadena qualifyDogDayCare(DogDayCareInvoice_Dto dogDayCareInvoice_dto);

}
