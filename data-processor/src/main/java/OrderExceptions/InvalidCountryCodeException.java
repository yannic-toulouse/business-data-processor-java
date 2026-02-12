package OrderExceptions;

public class InvalidCountryCodeException extends IllegalArgumentException
{
    public InvalidCountryCodeException(String countryCode)
    {
        super("Invalid country code: " + countryCode + ". Must be in ISO 3166-1 alpha-2 format!");
    }
}
