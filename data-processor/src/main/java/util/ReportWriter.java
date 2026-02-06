package util;

import model.Order;
import statistics.ReportData;
import statistics.StatisticsProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

public class ReportWriter
{
    public static void writeReportToTxt(List<Order> orderList, File outFile, boolean overwrite) throws IOException
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        ReportData reportData = StatisticsProcessor.fullReport(orderList);
        StringBuilder contentToWrite = new StringBuilder();
        StringBuilder countryValueSB = new StringBuilder();

        contentToWrite.append("Order count = ").append(String.format("%,d", reportData.orderCount())).append("\n");
        contentToWrite.append("High value order count = ").append(String.format("%,d", reportData.highValueOrderCount())).append("\n");
        contentToWrite.append("Export check order count = ").append(String.format("%,d", reportData.exportCheckOrderCount())).append("\n");
        contentToWrite.append("Total order value = ").append(String.format("%s", nf.format(reportData.totalOrderValue()))).append("\n");
        contentToWrite.append("Average order value = ").append(String.format("%s", nf.format(reportData.avgOrderValue()))).append("\n");
        contentToWrite.append("Lowest order value = ").append(String.format("%s", nf.format(reportData.lowestOrderValue()))).append("\n");
        contentToWrite.append("Highest order value = ").append(String.format("%s", nf.format(reportData.highestOrderValue()))).append("\n");
        contentToWrite.append("Order value by country:").append("\n");
        for (String country : reportData.orderValueByCountry().keySet())
            countryValueSB.append(String.format("%2s: %s", country, nf.format(reportData.orderValueByCountry().get(country)))).append("\n");
        contentToWrite.append(countryValueSB);

        try
        {
            if (outFile.createNewFile() || overwrite)
            {
                System.out.println("[Info] Output file successfully created.");
                writeContent(outFile, contentToWrite.toString());
            }
            else
            {
                System.out.println("[Info] Cancelling write operation. Existing file was not overwritten.");
            }
        }
        catch (IOException e)
        {
            throw new IOException("[Error] Couldn't create file.", e);
        }
    }

    private static void writeContent(File outFile, String content) throws IOException
    {
        try (FileWriter fWriter = new FileWriter(outFile))
        {
            fWriter.write(content);
            System.out.println("[Info] Output file successfully written to.");
            System.out.println("[Info] Output file path: " + outFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            throw new IOException("[Error] Couldn't write to file.", e);
        }
    }
}
