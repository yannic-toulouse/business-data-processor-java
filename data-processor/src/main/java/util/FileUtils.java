package util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import model.Order;
import model.Order.OrderStatus;

public class FileUtils
{

	@SuppressWarnings("unused")
	public static ArrayList<Order> processCSV(File file)
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
					
					System.out.println("Skipped line " + (allData.indexOf(line) + 1) + " because it was missing data");
					continue;
				}
				String orderID = line[0];
				String customer = line[1];
				BigDecimal orderValue = new BigDecimal(line[2]);
				String country = line[3].toUpperCase();
				if (country.length() != 2 || !Order.isoCountryCode.isValidCountry(country))
				{
					System.out.println("Skipped line " + (allData.indexOf(line) + 1) + " because its country code (" + country + ") was invalid");
					continue;
				}
				
				OrderStatus status;

				try 
				{
					status = OrderStatus.valueOf(line[4].trim().replace(' ', '_').toUpperCase());
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("Skipped line " + (allData.indexOf(line) + 1) + " because its order status (" + line[4].trim() + ") was invalid");
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
					System.out.println("Skipped line " + (allData.indexOf(line) + 1) + " because its order value was negative");
					continue;
				}
				catch (IllegalArgumentException e)
				{
					System.out.println("Skipped line " + (allData.indexOf(line) + 1) + " because its order id (" + orderID + ") was not unique");
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
