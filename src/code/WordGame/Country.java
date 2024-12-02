package WordGame;

/**
 * Represents a country with its name, capital city, and a set of interesting facts.
 *
 * @param name          the name of the country
 * @param capitalCityName the name of the country's capital city
 * @param facts         an array of interesting facts about the country
 *
 * @author Ben Henry
 * @version 1.0
 */
record Country(String name, String capitalCityName, String[] facts) {}
