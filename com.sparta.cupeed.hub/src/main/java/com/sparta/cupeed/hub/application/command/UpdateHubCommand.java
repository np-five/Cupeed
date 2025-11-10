package com.sparta.cupeed.hub.application.command;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateHubCommand {
	@Size(max = 100, message = "Name cannot exceed 100 characters")
	String name;

	@Size(max = 255, message = "Address cannot exceed 255 characters")
	String address;

	@DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
	@DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
	Double latitude;

	@DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
	@DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
	Double longitude;
}
