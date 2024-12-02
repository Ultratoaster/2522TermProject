package WordGame;

import java.nio.file.*;
import java.io.*;
import java.util.*;

/**
 * The World class represents a collection of countries and provides methods to
 * load country data from files, validate formats, and manage countries in a map.
 * It supports loading countries from a directory and storing them in a map
 * based on the country name.
 *
 * @author Ben Henry
 * @version 1.1
 */
public class World
{
    private static final String COUNTRY_CAPITAL_SEPARATOR = ":";
    private static final String FILE_EXTENSION = "*.txt";

    private static final int COUNTRY_NAME_INDEX = 0;
    private static final int CAPITAL_CITY_INDEX = 1;

    private final Map<String, Country> countriesMap;

    static final int EXPECTED_HEADER_PARTS = 2;
    static final int ZERO_ARRAY_SIZE = 0;
    static final int CAPITAL_CITY_NAME_INDEX = 1;


    /**
     * Creates a new World object with an empty map of countries.
     */
    public World()
    {
        countriesMap = new HashMap<>();
    }

    /**
     * Retrieves the map of countries.
     *
     * @return A map of country names to their respective Country objects.
     */
    Map<String, Country> getCountriesMap()
    {
        return countriesMap;
    }

    /**
     * Adds a Country to the map of countries.
     *
     * @param country The Country object to add.
     */
    void addCountry(final Country country)
    {
        countriesMap.put(country.name(), country);
    }

    /**
     * Retrieves a Country by its name.
     *
     * @param countryName The name of the country.
     * @return The Country object associated with the given name, or null if not found.
     */
    Country getCountry(final String countryName)
    {
        return countriesMap.get(countryName);
    }

    /**
     * Loads country data from all .txt files in the specified directory.
     * Each file is expected to contain country and capital data, followed by facts.
     *
     * @param dirPath The path to the directory containing the .txt files.
     */
    public void loadCountriesFromDirectory(final Path dirPath)
    {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, FILE_EXTENSION))
        {
            for(final Path file : stream)
            {
                List<Country> countriesFromFile = parseCountriesFromFile(file);
                for(final Country country : countriesFromFile)
                {
                    addCountry(country);
                }
            }
        } catch(final IOException e)
        {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }

    /**
     * Parses country data from a given file. The file should contain a series of country and
     * capital lines followed by facts. Each line containing a country and capital is validated
     * to ensure the correct format.
     *
     * @param filePath The path of the file to parse.
     * @return A list of Country objects parsed from the file.
     * @throws IOException If there is an error reading the file.
     */
    private List<Country> parseCountriesFromFile(final Path filePath) throws IOException
    {
        final List<Country> countries;
        final BufferedReader reader;

        countries = new ArrayList<>();
        reader = Files.newBufferedReader(filePath);


        String line;
        while((line = reader.readLine()) != null)
        {
            line = line.trim();

            if(line.isEmpty())
            {
                continue;
            }

            final String[] headerParts;
            headerParts = line.split(COUNTRY_CAPITAL_SEPARATOR);

            if(!isValidCountryFormat(headerParts))
            {
                System.err.println("Invalid country-capital format in file " + filePath + " at line: " + line);
                continue;
            }

            final List<String> factsList;
            final String countryName;
            final String capitalCityName;

            factsList = new ArrayList<>();
            countryName = headerParts[World.COUNTRY_NAME_INDEX].trim();
            capitalCityName = headerParts[CAPITAL_CITY_NAME_INDEX].trim();


            while((line = reader.readLine()) != null && !line.trim().isEmpty())
            {
                factsList.add(line.trim());
            }

            if(factsList.isEmpty())
            {
                System.err.println("No facts found for country: " + countryName);
                continue;
            }

            final String[] facts;
            facts = factsList.toArray(new String[ZERO_ARRAY_SIZE]);

            countries.add(new Country(countryName, capitalCityName, facts));
        }

        reader.close();
        return countries;
    }

    /**
     * Validates the format of the country and capital line.
     * The format should be "CountryName{@value COUNTRY_CAPITAL_SEPARATOR}CapitalCity".
     *
     * @param headerParts The parts of the line split by the colon.
     * @return true if the format is valid, false otherwise.
     */
    private boolean isValidCountryFormat(final String[] headerParts)
    {
        return headerParts.length == EXPECTED_HEADER_PARTS
                && !headerParts[COUNTRY_NAME_INDEX].trim().isEmpty()
                && !headerParts[CAPITAL_CITY_INDEX].trim().isEmpty();
    }

}
