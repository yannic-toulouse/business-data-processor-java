/**
 * Configuration.java
 * 04.02.2026
 */
package config;

import model.Order;

import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public class CsvColumnConfig
{
	private final int orderIdIndex;
	private final int customerIndex;
	private final int orderValueIndex;
	private final int countryIndex;
	private final int statusIndex;
	private final boolean hasHeader;
	private final Map<Integer, String> indexMap;
	private final Map<Integer, Function<Order, Object>> valueMap;



	/**
	 * Standard {@code CsvColumnConfig} default constructor.
	 * Defines {@code orderIdIndex = 0}, {@code customerIndex = 1}, {@code orderValueIndex = 2}, {@code countryIndex = 3}
	 */
	public CsvColumnConfig()
	{
		this.orderIdIndex = 0;
		this.customerIndex = 1;
		this.orderValueIndex = 2;
		this.countryIndex = 3;
		this.statusIndex = 4;
		this.hasHeader = true;
		indexMap = Map.of(
				orderIdIndex, "Order ID",
				customerIndex, "Customer",
				orderValueIndex, "Order Value",
				countryIndex, "Country",
				statusIndex, "Status"
		);
		valueMap = Map.of(
				orderIdIndex, Order::getOrderID,
				customerIndex, Order::getCustomer,
				orderValueIndex, Order::getOrderValue,
				countryIndex, Order::getCountry,
				statusIndex, Order::getStatus
		);
	}

	/**
	 * Overloaded {@code CsvColumnConfig} constructor. Loads index values from Properties object
	 * @param props Properties object to load index values from
	 */
	public CsvColumnConfig(Properties props)
	{
		this.orderIdIndex = readPropsInt(props, "csv.orderId");
		this.customerIndex = readPropsInt(props, "csv.customer");
		this.orderValueIndex = readPropsInt(props, "csv.orderValue");
		this.countryIndex = readPropsInt(props, "csv.country");
		this.statusIndex = readPropsInt(props, "csv.status");
		this.hasHeader = readPropsBool(props, "csv.hasHeader");
		indexMap = Map.of(
				orderIdIndex, "Order ID",
				customerIndex, "Customer",
				orderValueIndex, "Order Value",
				countryIndex, "Country",
				statusIndex, "Status"
		);
		valueMap = Map.of(
				orderIdIndex, Order::getOrderID,
				customerIndex, Order::getCustomer,
				orderValueIndex, Order::getOrderValue,
				countryIndex, Order::getCountry,
				statusIndex, Order::getStatus
		);
	}

	/**
	 * Reads and validates values from Properties object
	 * @param props Properties object to read from
	 * @param key Value to be read from {@code props}
	 * @return Value from {@code props} as an int
	 */
	private static int readPropsInt(Properties props, String key)
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

	private static boolean readPropsBool(Properties props, String key)
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

	public int getOrderIdIndex()
	{
		return orderIdIndex;
	}

	public int getCustomerIndex()
	{
		return customerIndex;
	}

	public int getOrderValueIndex()
	{
		return orderValueIndex;
	}

	public int getCountryIndex()
	{
		return countryIndex;
	}

	public boolean hasHeader() {
		return hasHeader;
	}

	public Map<Integer, String> getIndexMap() {
		return indexMap;
	}

	public int getStatusIndex() {
		return statusIndex;
	}

	public Map<Integer, Function<Order, Object>> getValueMap() {
		return valueMap;
	}
}