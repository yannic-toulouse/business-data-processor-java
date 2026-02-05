/**
 * Validator.java
 * 04.02.2026
 */
package validation;

import java.util.Locale;
import java.util.Locale.IsoCountryCode;

public class Validator
{
	/**
	 * Validates that country code is valid ISO3166-1 alpha-2 code
	 * @param code Country Code
	 * @return {@code true} if {@code code} is a valid ISO3166-1 alpha-2 country code and returns {@code false} if it is not
	 */
	public static boolean validateISOCountryCode(String code)
	{
		if(Locale.getISOCountries(IsoCountryCode.PART1_ALPHA2).contains(code))
			return true;
		return false;
	}
	
}
