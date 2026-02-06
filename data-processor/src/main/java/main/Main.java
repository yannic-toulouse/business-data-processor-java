package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import config.ConfigLoader;
import config.CsvColumnConfig;
import de.vandermeer.asciitable.AsciiTable;
import model.Order;
import statistics.StatisticsProcessor;
import util.CsvProcessor;
import util.ReportWriter;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        File file;
        CsvColumnConfig columnConfig;
        String configPath;
        try (Scanner scan = new Scanner(System.in))
        {
            while (true)
            {
                System.out.println("Do you want to use the default CSV-Column config? (y/n)");
                String useDefColumnsString = scan.nextLine();
                char useDefColumns = 0;
                if (!useDefColumnsString.isEmpty())
                    useDefColumns = useDefColumnsString.toLowerCase().charAt(0);
                switch (useDefColumns)
                {
                    case 'y':
                        columnConfig = new CsvColumnConfig();
                        break;
                    case 'n':
                        while (true)
                        {
                            System.out.println("Path to properties file:");
                            configPath = scan.nextLine();
                            try
                            {
                                columnConfig = new CsvColumnConfig(ConfigLoader.load(configPath));
                                break;
                            }
                            catch (IOException e)
                            {
                                System.out.println("Failed to load config.\n" +
                                        "Detailed error message: " + e.getLocalizedMessage());
                            }
                        }
                        break;
                    default:
                        System.out.println("Please only enter the values 'y' or 'n'!");
                        continue;
                }
                while (true)
                {
                    System.out.println("Path to CSV file:");
                    file = new File(scan.nextLine().trim());

                    if (file.exists() && !file.isDirectory())
                        break;
                    System.out.println(file.getAbsolutePath());
                    System.out.println("File doesn't exist. Please enter valid path");
                }
                break;
            }


            ArrayList<Order> orderList = CsvProcessor.processCSV(columnConfig, file);
            Map<Integer, String> indexMap = columnConfig.getIndexMap();
            Map<Integer, Function<Order, Object>> valueMap = columnConfig.getValueMap();
            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow(indexMap.get(0), indexMap.get(1), indexMap.get(2), indexMap.get(3), indexMap.get(4), "Flags");
            for (Order item : orderList)
            {
                table.addRule();
                table.addRow(valueMap.get(0).apply(item), valueMap.get(1).apply(item), valueMap.get(2).apply(item), valueMap.get(3).apply(item), valueMap.get(4).apply(item), item.flagAsString());
            }
            table.addRule();
            System.out.println(table.render());
            System.out.printf("Avg order value = %.2f€\n", StatisticsProcessor.getAvgOrderValue(orderList));
            System.out.println("Total order value by countries:");
            System.out.println(StatisticsProcessor.getOrderValueByCountry(orderList));
            System.out.println("Total order amount = " + StatisticsProcessor.getOrderCount(orderList));
            System.out.println("High Value count = " + StatisticsProcessor.getHighValueOrderCount(orderList));

            System.out.println("Where would you like to save the generated report? Enter nothing to delete report.");
            File savePath = new File(scan.nextLine());
            if(savePath.toString().isEmpty())
            {
                System.out.println("[Info] Report deleted.");
                return;
            }
            boolean overwrite = false;
            boolean overwriteIsSet = false;
            if (savePath.exists() && savePath.isFile())
            {
                System.out.println("[Warning] File already exists.");
                while (!overwriteIsSet)
                {
                    System.out.println("Overwrite existing file? (y/n)");
                    String input = scan.nextLine().toLowerCase();
                    if (!input.isEmpty())
                    {
                        char charIn = input.charAt(0);
                        switch (charIn)
                        {
                            case 'y':
                                overwrite = true;
                                overwriteIsSet = true;
                                break;
                            case 'n': // overwrite is set to false by default
                                overwriteIsSet = true;
                                break;
                            default:
                                System.out.println("Please only enter 'y' for yes or 'n' for no.");
                                break;
                        }
                    }
                }
            }

            try
            {
                ReportWriter.writeReportToTxt(orderList, savePath, overwrite);
            }
            catch (IOException e)
            {
                throw new IOException("Couldn't save report.", e);
            }
        }
    }

}
