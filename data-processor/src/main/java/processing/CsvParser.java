package processing;

import OrderExceptions.DuplicateOrderIdException;
import OrderExceptions.InvalidCountryCodeException;
import OrderExceptions.NegativeOrderValueException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import config.CsvColumnConfig;
import model.Order;
import validation.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public class CsvParser
{
    private final CsvColumnConfig config;
    private final Logger logger;
    public CsvParser(CsvColumnConfig config, Logger logger)
    {
        this.config = config;
        this.logger = logger;
    }
    public ArrayList<Order> csvToOrders(String csvPath)
    {
        logger.addHandler(new ConsoleHandler());
        ArrayList<Order> orderList = new ArrayList<>();
        String[] line;
        try(FileReader fReader = new FileReader(new File(csvPath)))
        {
            CSVReader csvReader = new CSVReader(fReader);
            if (config.hasHeader())
                csvReader.readNext(); // Skip header row
            for(int i = 0; (line = csvReader.readNext()) != null; i++)
            {
                if (line.length < 5)
                {
                    logger.warning("Skipped line " + (i + 1) + " because it was missing data");
                    continue;
                }
                String orderID = line[config.getOrderIdIndex()];
                String customer = line[config.getCustomerIndex()];
                String orderValueString = line[config.getOrderValueIndex()];
                String country = line[config.getCountryIndex()].toUpperCase();
                String statusString = line[config.getStatusIndex()];

                if(!Validator.validateOrderStatus(statusString))
                {
                    logger.warning("Skipped line " + (i + 1) + " because it contained an invalid order status");
                    continue;
                }

                try
                {
                    orderList.add(new Order(orderID, customer, new BigDecimal(orderValueString), country, Order.OrderStatus.valueOf(statusString)));
                }
                catch (DuplicateOrderIdException e)
                {
                    logger.warning("Skipped line " + (i + 1) + " because it contained a duplicate order ID");
                }
                catch(InvalidCountryCodeException e)
                {
                    logger.warning("Skipped line " + (i + 1) + " because it contained an invalid country code");
                }
                catch(NegativeOrderValueException e)
                {
                    logger.warning("Skipped line " + (i + 1) + " because it contained a negative order value");
                }

            }
        }
        catch (FileNotFoundException e)
        {
            logger.severe("File not found: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        catch(IOException e)
        {
            logger.severe("Could not read file: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        catch (CsvValidationException e)
        {
            logger.severe("Could not parse CSV: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return orderList;
    }
}
