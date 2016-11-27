package com.sebastian_daschner.coff_a_holic;

import java.net.URI;

public class CoffeeType {

    private String type;
    private URI origins;

    public CoffeeType() {
    }

    public CoffeeType(final String type, final URI origins) {
        this.type = type;
        this.origins = origins;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public URI getOrigins() {
        return origins;
    }

    public void setOrigins(final URI origins) {
        this.origins = origins;
    }

    @Override
    public String toString() {
        return "CoffeeType{" +
                "type='" + type + '\'' +
                ", origins=" + origins +
                '}';
    }
}
