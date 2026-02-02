package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import de.vandermeer.asciitable.AsciiTable;
import model.Order;
import util.FileUtils;

public class Main
{
	public static void main(String[] args)
	{
		File file;
		System.out.println("Path to CSV file:");
		try(Scanner scan = new Scanner(System.in))
		{
			file = new File(scan.nextLine().trim());
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
	}

}
