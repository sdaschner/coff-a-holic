package com.sebastian_daschner.coff_a_holic;

import java.util.List;

public class CoffeeConsumer {

    public static void main(String[] args) {
        final CoffeeClient coffeeClient = new CoffeeClient();

        final List<CoffeeType> coffeeTypes = coffeeClient.getCoffeeTypes();
        System.out.println("coffeeTypes = " + coffeeTypes);

        final CoffeeType coffeeType = coffeeTypes.get(0);

        final List<String> origins = coffeeClient.getOrigins(coffeeType);
        System.out.println("origins = " + origins);

        final String origin = origins.get(0);

        final CoffeeOrder coffeeOrder = coffeeClient.orderCoffee(coffeeType, origin);
        System.out.println("created coffeeOrder = " + coffeeOrder);

        final CoffeeOrder collectedCoffee = coffeeClient.fetchCoffee(coffeeOrder);
        System.out.println("collected coffee = " + collectedCoffee);

        coffeeClient.tearDown();
    }

}
