package display;

import config.CsvColumnConfig;
import de.vandermeer.asciitable.AsciiTable;
import model.Order;
import statistics.ReportData;

import java.text.NumberFormat;
import java.util.Map;
import java.util.function.Function;

public class ReportDisplay
{
        public static void displayReport(ReportData reportData)
        {
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);

            System.out.println(reportData.getReportString(nf));
        }



    public static void displayReportAsTable(ReportData reportData, CsvColumnConfig columnConfig)
        {
            Map<Integer, String> indexMap = columnConfig.getIndexMap();
            Map<Integer, Function<Order, Object>> valueMap = columnConfig.getValueMap();
            AsciiTable table = new AsciiTable();
            table.addRule();
            table.addRow(indexMap.get(0), indexMap.get(1), indexMap.get(2), indexMap.get(3), indexMap.get(4), "Flags");
            for (Order item : reportData.orderList())
            {
                table.addRule();
                table.addRow(valueMap.get(0).apply(item), valueMap.get(1).apply(item), valueMap.get(2).apply(item), valueMap.get(3).apply(item), valueMap.get(4).apply(item), item.flagAsString());
            }
            table.addRule();
            System.out.println(table.render());
        }
}
