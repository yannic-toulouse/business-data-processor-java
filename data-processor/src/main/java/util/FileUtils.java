package util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import config.CsvColumnConfig;
import model.Order;
import model.Order.OrderStatus;
import validation.Validator;

public class FileUtils
{

	/**
	 * Processes a csv file containing order data. File must contain the fields orderID, customer, orderValue and country
	 * @param config Configuration of csv indexes for specified fields
	 * @param file File to be processed
	 * @return {@code ArrayList} of {@code Order} objects
	 */
	public static ArrayList<Order> processCSV(CsvColumnConfig config, File file)
	{
		
		
		ArrayList<Order> orderList = new ArrayList<Order>();
		List<String[]> allData = null;

		try (FileReader fReader = new FileReader(file);
				CSVReader csvReader = new CSVReader(fReader))
		{
			csvReader.readNext(); // Skip header row
			allData = csvReader.readAll();
			csvReader.close();
			
			for (String[] line : allData)
			{
				if (line.length < 5)
				{
					
					System.out.println("[Warning] Skipped line " + (allData.indexOf(line) + 1) + " because it was missing data");
					continue;
				}
				String orderID = line[config.getOrderIdIndex()];
				String customer = line[config.getCustomerIndex()];
				BigDecimal orderValue = new BigDecimal(line[config.getOrderValueIndex()]);
				String country = line[config.getCountryIndex()].toUpperCase();
				
				if (!Validator.validateISOCountryCode(country))
				{
					System.out.println("[Warning] Skipped line " + (allData.indexOf(line) + 1) + " because its country code (" + country + ") was invalid");
					continue;
				}
				
				OrderStatus status;

				try 
				{
					status = OrderStatus.valueOf(line[4].trim().replace(' ', '_').toUpperCase());
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("[Warning] Skipped line " + (allData.indexOf(line) + 1) + " because its order status (" + line[4].trim() + ") was invalid");
					continue;
				}
				
				try
				{
					orderList.add(new Order(
									orderID,
									customer,
									orderValue,
									country,
									status));
				}
				catch(ExceptionInInitializerError e)
				{
					System.out.println("[Warning] Skipped line " + (allData.indexOf(line) + 1) + " because its order value was negative");
					continue;
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("[Warning] Skipped line " + (allData.indexOf(line) + 1) + " because its order id (" + orderID + ") was not unique");
				}
			}
		}
		catch (IOException | IllegalArgumentException | CsvException e)
		{
			e.printStackTrace();
		}

		return orderList;
	}

}
