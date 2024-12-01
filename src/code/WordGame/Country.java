package WordGame;

public class Country
{
    final String name;
    final String capitalCityName;
    final String[] facts;

    public Country (final String name, final String capitalCityName, final String[] facts)
    {
        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    public String getName ()
    {
        return name;
    }

    public String getCapitalCityName ()
    {
        return capitalCityName;
    }

    public String[] getFacts ()
    {
        return facts;
    }

    // Method to display country information
    public void displayCountryInfo() {
        System.out.println("Country: " + name);
        System.out.println("Capital: " + capitalCityName);
        System.out.println("Facts:");
        for (String fact : facts) {
            System.out.println("  - " + fact);
        }
    }

}
