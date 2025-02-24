package it.unina.dietiestates25.agentreview.infrastructure.adapter.in.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AgentReviewDto(

        @NotNull
        @NotBlank
        String agentId,
        @NotNull
        @Min(1) @Max(5)
        Integer value,
        String comment

) {
}
