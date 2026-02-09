package util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import OrderExceptions.DuplicateOrderIdException;
import OrderExceptions.NegativeOrderValueException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import config.CsvColumnConfig;
import model.Order;
import model.Order.OrderStatus;
import validation.Validator;

public class CsvProcessor
{

    /**
     * Processes a csv file containing order data. File must contain the fields orderID, customer, orderValue and country
     *
     * @param config Configuration of csv indexes for specified fields
     * @param file   File to be processed
     * @return {@code ArrayList} of {@code Order} objects
     */
    public static ArrayList<Order> processCSV(CsvColumnConfig config, File file)
    {


        ArrayList<Order> orderList = new ArrayList<>();
        List<String[]> allData;

        try (FileReader fReader = new FileReader(file);
             CSVReader csvReader = new CSVReader(fReader))
        {
            if (config.hasHeader())
                csvReader.readNext(); // Skip header row
            allData = csvReader.readAll();
            csvReader.close();

            for (int i = 0; i < allData.size(); i++)
            {
                String[] line = allData.get(i);
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
                    System.out.println("[Warning] Skipped line " + (i + 1) + " because its country code (" + country + ") was invalid");
                    continue;
                }

                OrderStatus status;

                if (Validator.validateOrderStatus(line[config.getStatusIndex()].trim().replace(' ', '_').toUpperCase()))
                {
                    status = OrderStatus.valueOf(line[config.getStatusIndex()].trim().replace(' ', '_').toUpperCase());
                }
                else
                {
                    System.out.println("[Warning] Skipped line " + (i + 1) + " because its order status (" + line[4].trim() + ") was invalid");
                    continue;
                }

                try
                {
                    orderList.add(new Order(
                            orderID,
                            customer,
                            orderValue,
                            country,
                            status
                    ));
                }

                catch (DuplicateOrderIdException | NegativeOrderValueException e)
                {
                    System.out.println("[Warning] Skipped line " + (i + 1) + ": " + e.getMessage());
                }
            }
        }
        catch (IOException | IllegalArgumentException | CsvException e)
        {
            System.out.println("Failed to process CSV file. Detailed error message: " + e.getLocalizedMessage());
        }

        return orderList;
    }

}
