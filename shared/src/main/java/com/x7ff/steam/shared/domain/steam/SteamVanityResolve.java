package com.x7ff.steam.shared.domain.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SteamVanityResolve {

	@JsonProperty("response")
	private SteamVanityResponse response;

	@JsonProperty("response")
	public SteamVanityResponse getResponse() {
		return response;
	}

	@JsonProperty("response")
	public void setResponse(SteamVanityResponse response) {
		this.response = response;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SteamVanityResponse {

		@JsonProperty("success")
		private long code;

		@JsonProperty("message")
		private String message;

		@JsonProperty("steamid")
		private String steamId;

		@JsonProperty("success")
		public long getCode() {
			return code;
		}

		@JsonProperty("success")
		public void setCode(long code) {
			this.code = code;
		}

		@JsonProperty("message")
		public String getMessage() {
			return message;
		}

		@JsonProperty("message")
		public void setMessage(String message) {
			this.message = message;
		}

		@JsonProperty("steamid")
		public void setSteamId(String steamId) {
			this.steamId = steamId;
		}

		@JsonProperty("steamid")
		public String getSteamId() {
			return steamId;
		}

		public boolean isSuccessful() {
			return code == 1 && steamId != null && !steamId.isEmpty();
		}

	}

}