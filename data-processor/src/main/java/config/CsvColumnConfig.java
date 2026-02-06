/**
 * Configuration.java
 * 04.02.2026
 */
package config;

import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import model.Order;

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
		this.orderIdIndex = ConfigLoader.readPropsInt(props, "csv.orderId");
		this.customerIndex = ConfigLoader.readPropsInt(props, "csv.customer");
		this.orderValueIndex = ConfigLoader.readPropsInt(props, "csv.orderValue");
		this.countryIndex = ConfigLoader.readPropsInt(props, "csv.country");
		this.statusIndex = ConfigLoader.readPropsInt(props, "csv.status");
		this.hasHeader = ConfigLoader.readPropsBool(props, "csv.hasHeader");
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