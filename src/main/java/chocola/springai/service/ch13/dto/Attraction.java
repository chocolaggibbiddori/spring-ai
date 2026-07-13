package chocola.springai.service.ch13.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record Attraction(
        String name,
        String address,
        String description,
        Integer entranceFee) {
}
