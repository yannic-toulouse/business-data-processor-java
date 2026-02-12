package main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import display.ReportDisplay;
import config.ConfigLoader;
import config.CsvColumnConfig;
import processing.ProcessingEngine;
import statistics.ReportData;
import util.ReportWriter;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String fileString;
        File file;
        CsvColumnConfig columnConfig;
        String configPath;
        ProcessingEngine procEngine;
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
                        procEngine = new ProcessingEngine(columnConfig);
                        break;
                    case 'n':
                        while (true)
                        {
                            System.out.println("Path to properties file:");
                            configPath = scan.nextLine();
                            try
                            {
                                columnConfig = new CsvColumnConfig(ConfigLoader.load(configPath));
                                procEngine = new ProcessingEngine(columnConfig);
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
                    fileString = scan.nextLine().trim();
                    file = new File(fileString);

                    if (file.exists() && !file.isDirectory())
                        break;
                    System.out.println(file.getAbsolutePath());
                    System.out.println("File doesn't exist. Please enter valid path");
                }
                break;
            }

            ReportData reportData = procEngine.fullProcessing(fileString);

            ReportDisplay.displayReportAsTable(reportData, columnConfig);
            ReportDisplay.displayReport(reportData);

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
                ReportWriter.writeReportToTxt(reportData, savePath, overwrite);
            }
            catch (IOException e)
            {
                throw new IOException("Couldn't save report.", e);
            }
        }
    }

}
