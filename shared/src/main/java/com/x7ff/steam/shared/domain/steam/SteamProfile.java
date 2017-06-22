package com.x7ff.steam.shared.domain.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamProfile {

    @JsonProperty("personaname")
    private String name;

    @JsonProperty("steamid")
    private String identifier;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("avatarmedium")
    private String avatarMedium;

    @JsonProperty("avatarfull")
    private String avatarFull;

    @JsonProperty("primaryclanid")
    private String clanId;

    @JsonProperty("timecreated")
    private long timeCreated;

    @JsonProperty("loccountrycode")
    private String countryCode;

    @JsonProperty("personaname")
    public String getName() {
        return name;
    }

    @JsonProperty("personaname")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("steamid")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("steamid")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("avatarmedium")
    public String getAvatarMedium() {
        return avatarMedium;
    }

    @JsonProperty("avatarmedium")
    public void setAvatarMedium(String avatarMedium) {
        this.avatarMedium = avatarMedium;
    }

    @JsonProperty("avatarfull")
    public String getAvatarFull() {
        return avatarFull;
    }

    @JsonProperty("avatarfull")
    public void setAvatarFull(String avatarFull) {
        this.avatarFull = avatarFull;
    }

    @JsonProperty("primaryclanid")
    public String getClanId() {
        return clanId;
    }

    @JsonProperty("primaryclanid")
    public void setClanId(String clanId) {
        this.clanId = clanId;
    }

    @JsonProperty("timecreated")
    public long getTimeCreated() {
        return timeCreated;
    }

    @JsonProperty("timecreated")
    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    @JsonProperty("loccountrycode")
    public String getCountryCode() {
        return countryCode;
    }

    @JsonProperty("loccountrycode")
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}