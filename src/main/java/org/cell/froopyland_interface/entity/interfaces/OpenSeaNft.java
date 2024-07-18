package org.cell.froopyland_interface.entity.interfaces;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yozora
 * description
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "identifier",
        "collection",
        "contract",
        "token_standard",
        "name",
        "description",
        "image_url",
        "metadata_url",
        "opensea_url",
        "updated_at",
        "is_disabled",
        "is_nsfw"
})
public class OpenSeaNft {

    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("collection")
    private String collection;
    @JsonProperty("contract")
    private String contract;
    @JsonProperty("token_standard")
    private String tokenStandard;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("metadata_url")
    private String metadataUrl;
    @JsonProperty("opensea_url")
    private String openseaUrl;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("is_disabled")
    private Boolean isDisabled;
    @JsonProperty("is_nsfw")
    private Boolean isNsfw;
    private String nonFungibleTokenCode;
    private String animationUrl;
    private String creator;
    private String userAddress;
    private String networkName;
    private String fromPlatform;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("collection")
    public String getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(String collection) {
        this.collection = collection;
    }

    @JsonProperty("contract")
    public String getContract() {
        return contract;
    }

    @JsonProperty("contract")
    public void setContract(String contract) {
        this.contract = contract;
    }

    @JsonProperty("token_standard")
    public String getTokenStandard() {
        return tokenStandard;
    }

    @JsonProperty("token_standard")
    public void setTokenStandard(String tokenStandard) {
        this.tokenStandard = tokenStandard;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("metadata_url")
    public String getMetadataUrl() {
        return metadataUrl;
    }

    @JsonProperty("metadata_url")
    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }

    @JsonProperty("opensea_url")
    public String getOpenseaUrl() {
        return openseaUrl;
    }

    @JsonProperty("opensea_url")
    public void setOpenseaUrl(String openseaUrl) {
        this.openseaUrl = openseaUrl;
    }

    @JsonProperty("updated_at")
    public String getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty("updated_at")
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonProperty("is_disabled")
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    @JsonProperty("is_disabled")
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    @JsonProperty("is_nsfw")
    public Boolean getIsNsfw() {
        return isNsfw;
    }

    @JsonProperty("is_nsfw")
    public void setIsNsfw(Boolean isNsfw) {
        this.isNsfw = isNsfw;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getFromPlatform() {
        return fromPlatform;
    }

    public void setFromPlatform(String fromPlatform) {
        this.fromPlatform = fromPlatform;
    }

    public String getNonFungibleTokenCode() {
        return nonFungibleTokenCode;
    }

    public void setNonFungibleTokenCode(String nonFungibleTokenCode) {
        this.nonFungibleTokenCode = nonFungibleTokenCode;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getAnimationUrl() {
        return animationUrl;
    }

    public void setAnimationUrl(String animationUrl) {
        this.animationUrl = animationUrl;
    }
}
