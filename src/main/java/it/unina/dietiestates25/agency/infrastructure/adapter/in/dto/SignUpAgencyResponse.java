package it.unina.dietiestates25.agency.infrastructure.adapter.in.dto;

import it.unina.dietiestates25.model.Admin;
import it.unina.dietiestates25.model.Agency;

public record SignUpAgencyResponse(
        Admin admin,
        Agency agency
) {
}
