package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader
{
	/**
	 * Creates Properties object from String path to properties file
	 * @param path Path to properties file
	 * @return Properties object containing the properties defined in the specified properties file
	 * @throws IOException if file either cannot be found or couldn't be read
	 */
	public static Properties load(String path) throws IOException
	{
		FileInputStream fiStream;
		try
		{
			fiStream = new FileInputStream(path);
		}
		catch (FileNotFoundException e)
		{
			throw new IOException("Couldn't find config file " + new File(path).getAbsolutePath(), e);
		}
		
		Properties props = new Properties();
		try
		{
			props.load(fiStream);
			return props;
		}
		catch (IOException e)
		{
			throw new IOException("Couldn't load file " + new File(path).getAbsolutePath(), e);
		}
	}

	/**
	 * Reads and validates values from Properties object
	 * @param props Properties object to read from
	 * @param key Value to be read from {@code props}
	 * @return Value from {@code props} as an int
	 */
	public static int readPropsInt(Properties props, String key)
	{
		String valueString = props.getProperty(key);
		int value;
		if(valueString == null)
		{
			throw new IllegalArgumentException("Missing config key: " + key);
		}

		try
		{
			value = Integer.parseInt(valueString);
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException("Couldn't parse " + valueString + " from config file!", e);
		}
		return value;
	}

	public static boolean readPropsBool(Properties props, String key)
	{
		String valueString = props.getProperty(key);
		boolean value;
		if(valueString == null)
		{
			throw new IllegalArgumentException("Missing config key: " + key);
		}

		try
		{
			value = Boolean.parseBoolean(valueString);
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException("Couldn't parse " + valueString + " from config file!", e);
		}
		return value;
	}
}
