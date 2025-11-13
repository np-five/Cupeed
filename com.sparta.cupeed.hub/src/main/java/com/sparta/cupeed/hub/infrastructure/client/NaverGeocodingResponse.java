package com.sparta.cupeed.hub.infrastructure.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NaverGeocodingResponse {

	private String status;
	private Meta meta;
	private List<AddressInfo> addresses;
	private String errorMessage;

	@Data
	public static class Meta {
		private int totalCount;
		private int page;
		private int count;
	}

	@Data
	public static class AddressInfo {
		private String roadAddress;
		private String jibunAddress;
		private String englishAddress;
		private List<AddressElement> addressElements;

		@JsonProperty("x")
		private String x;  // 경도

		@JsonProperty("y")
		private String y;  // 위도

		private double distance;

		// Getter 메서드 (lombok @Data가 자동 생성하지만 명시적으로 추가)
		public String getLongitude() {
			return x;
		}

		public String getLatitude() {
			return y;
		}
	}

	@Data
	public static class AddressElement {
		private List<String> types;
		private String longName;
		private String shortName;
		private String code;
	}
}