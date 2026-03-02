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
    public static void writeReportToTxt(ReportData reportData, File outFile, boolean overwrite) throws IOException
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        String contentToWrite = reportData.getReportString(nf);

        try
        {
            if (outFile.createNewFile() || overwrite)
            {
                System.out.println("[Info] Output file successfully created.");
                writeContent(outFile, contentToWrite);
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
