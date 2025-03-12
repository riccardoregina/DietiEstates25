package it.unina.dietiestates25.agency.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.agency.model.Admin;
import it.unina.dietiestates25.agency.model.Agency;

public record SignUpAgencyResponse(
        Admin admin,
        Agency agency
) {
}
