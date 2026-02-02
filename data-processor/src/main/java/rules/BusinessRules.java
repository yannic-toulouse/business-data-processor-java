package rules;

import java.math.BigDecimal;
import java.util.Arrays;

public class BusinessRules
{
	private static BigDecimal highValueLimit = new BigDecimal(10000);
	
	private static String[] euCountryCodes = {
		    "AT", // Austria
		    "BE", // Belgium
		    "BG", // Bulgaria
		    "HR", // Croatia
		    "CY", // Cyprus
		    "CZ", // Czech Republic
		    "DK", // Denmark
		    "EE", // Estonia
		    "FI", // Finland
		    "FR", // France
		    "DE", // Germany
		    "GR", // Greece
		    "HU", // Hungary
		    "IS", // Iceland (EEA)
		    "IE", // Ireland
		    "IT", // Italy
		    "LV", // Latvia
		    "LT", // Lithuania
		    "LU", // Luxembourg
		    "MT", // Malta
		    "NL", // Netherlands
		    "NO", // Norway (EEA)
		    "PL", // Poland
		    "PT", // Portugal
		    "RO", // Romania
		    "SK", // Slovakia
		    "SI", // Slovenia
		    "ES", // Spain
		    "SE", // Sweden
		    "CH"  // Switzerland (EEA)
		};

	
	public static BigDecimal getHighValueLimit()
	{
		return highValueLimit;
	}
	
	public static boolean isEUCounry(String code)
	{
		return Arrays.asList(euCountryCodes).contains(code);
	}
	
}
