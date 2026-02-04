/**
 * ConfigLoader.java
 * 04.02.2026
 */
package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader
{
	public static Properties load(String path) throws IOException
	{
		FileInputStream fiStream;
		try
		{
			fiStream = new FileInputStream(new File(path));
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
}
