package com.sebastian_daschner.coff_a_holic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URI;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoffeeOrder {

    private URI uri;
    private String type;
    private String origin;
    private String status;

    public URI getUri() {
        return uri;
    }

    public void setUri(final URI uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CoffeeOrder{" +
                "uri=" + uri +
                ", type='" + type + '\'' +
                ", origin='" + origin + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
