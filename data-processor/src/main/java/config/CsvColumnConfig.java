/**
 * Configuration.java
 * 04.02.2026
 */
package config;

import model.Order;

import java.util.HashMap;
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
		this.orderIdIndex = readProps(props, "csv.orderId");
		this.customerIndex = readProps(props, "csv.customer");
		this.orderValueIndex = readProps(props, "csv.orderValue");
		this.countryIndex = readProps(props, "csv.country");
		this.statusIndex = readProps(props, "csv.status");
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
	private static int readProps(Properties props, String key)
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