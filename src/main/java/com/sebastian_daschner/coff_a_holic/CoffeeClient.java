package com.sebastian_daschner.coff_a_holic;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class CoffeeClient {

    private final String baseUri = "http://localhost:8080/coffee-shop/resources";

    private final Client client;
    private final WebTarget target;

    public CoffeeClient() {
        client = ClientBuilder.newClient();
        target = client.target(baseUri);
    }

    public List<CoffeeType> getCoffeeTypes() {
        final JsonObject root = get(target, JsonObject.class);

        final URI typesUri = URI.create(root.getJsonObject("_links").getString("types"));
        final JsonArray array = get(typesUri, JsonArray.class);

        return array.getValuesAs(JsonObject.class).stream()
                .map(o -> {
                    final String type = o.getString("type");
                    final URI origins = URI.create(o.getJsonObject("_links").getString("origins"));
                    return new CoffeeType(type, origins);
                })
                .collect(Collectors.toList());
    }

    public List<String> getOrigins(final CoffeeType coffeeType) {
        final URI originsUri = coffeeType.getOrigins();
        final JsonArray array = get(originsUri, JsonArray.class);

        return array.getValuesAs(JsonObject.class).stream()
                .map(o -> o.getString("origin")).collect(Collectors.toList());
    }

    public CoffeeOrder orderCoffee(final CoffeeType coffeeType, final String origin) {
        final JsonObject root = get(target, JsonObject.class);

        final JsonObject properties = Json.createObjectBuilder()
                .add("type", coffeeType.getType())
                .add("origin", origin).build();

        final Response response = followAction(root, "order-coffee", properties);

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new RuntimeException("could not place order, status: " + response.getStatus() + ", headers:" + response.getHeaders());
        }

        System.out.println("order has been placed");
        final URI orderUri = response.getLocation();

        final CoffeeOrder order = get(orderUri, CoffeeOrder.class);
        order.setUri(orderUri);
        return order;
    }

    public CoffeeOrder fetchCoffee(final CoffeeOrder coffeeOrder) {
        final JsonObject jsonObject = get(coffeeOrder.getUri(), JsonObject.class);

        final JsonObject properties = Json.createObjectBuilder()
                .add("type", coffeeOrder.getType())
                .add("origin", coffeeOrder.getOrigin())
                .add("status", "collected")
                .build();

        final Response response = followAction(jsonObject, "collect-coffee", properties);

        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            throw new RuntimeException("could not place order, status: " + response.getStatus() + ", headers:" + response.getHeaders());
        }

        System.out.println("coffee has been collected");

        final CoffeeOrder order = get(coffeeOrder.getUri(), CoffeeOrder.class);
        order.setUri(coffeeOrder.getUri());
        order.setStatus("collected");
        return order;
    }

    public void tearDown() {
        client.close();
    }

    private <T> T get(final URI uri, final Class<T> responseClass) {
        return get(client.target(uri), responseClass);
    }

    private <T> T get(final WebTarget target, final Class<T> responseClass) {
        return target.request(MediaType.APPLICATION_JSON_TYPE).get(responseClass);
    }

    private Response followAction(final JsonObject entity, final String actionName, final JsonObject properties) {
        final JsonObject action = entity.getJsonObject("_actions").getJsonObject(actionName);

        final Entity<JsonObject> body = Entity.json(properties);

        return client.target(action.getString("href"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .method(action.getString("method"), body);
    }

}
