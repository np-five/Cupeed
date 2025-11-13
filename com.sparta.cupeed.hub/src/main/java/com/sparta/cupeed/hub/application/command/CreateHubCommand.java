package com.sparta.cupeed.hub.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateHubCommand {
	@NotBlank(message = "Hub name is required")
	@Size(max = 100, message = "Name cannot exceed 100 characters")
	String name;

	@NotBlank(message = "Address is required")
	@Size(max = 255, message = "Address cannot exceed 255 characters")
	String address;

	// @NotNull(message = "Latitude is required")
	// @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
	// @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
	// Double latitude;
	//
	// @NotNull(message = "Longitude is required")
	// @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
	// @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
	// Double longitude;
}
