package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import de.vandermeer.asciitable.AsciiTable;
import model.Order;
import statistics.StatisticsProcessor;
import util.FileUtils;

public class Main
{
	public static void main(String[] args)
	{
		File file;
		try(Scanner scan = new Scanner(System.in))
		{
			while(true)
			{
				System.out.println("Path to CSV file:");
				file = new File(scan.nextLine().trim());
				
				if(file.exists() && !file.isDirectory())
					break;
				System.out.println(file.getAbsolutePath());
				System.out.println("File doesn't exist. Please enter valid path");
			}
		}

		ArrayList<Order> orderList = FileUtils.processCSV(file);
		AsciiTable table = new AsciiTable();
		table.addRule();
		table.addRow("Order ID", "Customer", "Order Value", "Country", "Status", "Flags");
		for (Order item : orderList)
		{
			table.addRule();
			table.addRow(item.getOrderID(), item.getCustomer(), item.getOrderValue(), item.getCountry(), item.getStatus(), item.flagAsString());
		}
		table.addRule();
		System.out.println(table.render());
		System.out.printf("Avg order value = %.2f€\n", StatisticsProcessor.getAvgOrderValue(orderList));
		System.out.println("Total order value by countries:");
		System.out.println(StatisticsProcessor.getOrderValueByCountry(orderList));
		System.out.println("Total order amount = " + StatisticsProcessor.getOrderCount(orderList));
		System.out.println("High Value count = " + StatisticsProcessor.getHighValueOrderCount(orderList));
	}

}
