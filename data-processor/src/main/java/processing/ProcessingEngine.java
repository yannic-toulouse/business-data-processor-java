package processing;

import config.CsvColumnConfig;
import model.Order;
import statistics.ReportData;
import statistics.StatisticsProcessor;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ProcessingEngine
{
    private static final Logger logger = Logger.getLogger(ProcessingEngine.class.getName());
    private final CsvParser parser;

    public ProcessingEngine(CsvColumnConfig config)
    {
        this.parser = new CsvParser(config, logger);
    }

    public ReportData fullProcessing(String csvPath)
    {
        ArrayList<Order> orders = parser.csvToOrders(csvPath);
        return StatisticsProcessor.fullReport(orders);
    }


}
