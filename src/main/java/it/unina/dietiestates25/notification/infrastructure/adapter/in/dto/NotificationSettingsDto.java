package it.unina.dietiestates25.notification.infrastructure.adapter.in.dto;

public record NotificationSettingsDto(
        Boolean starredListings,
        Boolean visit,
        Boolean recommendedListings
) {
}
