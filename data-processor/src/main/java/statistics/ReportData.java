package statistics;

import model.Order;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

public record ReportData(
        List<Order> orderList,
        int orderCount,
        int highValueOrderCount,
        int exportCheckOrderCount,
        BigDecimal totalOrderValue,
        BigDecimal avgOrderValue,
        BigDecimal lowestOrderValue,
        BigDecimal highestOrderValue,
        Map<String, BigDecimal> orderValueByCountry
)
{
    public ReportData
    {
        orderList = List.copyOf(orderList);
        orderValueByCountry = Map.copyOf(orderValueByCountry);
    }
    @Override
    public List<Order> orderList()
    {
        return List.copyOf(orderList);
    }

    @Override
    public BigDecimal avgOrderValue()
    {
        return avgOrderValue;
    }

    @Override
    public int orderCount()
    {
        return orderCount;
    }

    @Override
    public int highValueOrderCount()
    {
        return highValueOrderCount;
    }

    @Override
    public int exportCheckOrderCount()
    {
        return exportCheckOrderCount;
    }

    @Override
    public BigDecimal totalOrderValue()
    {
        return totalOrderValue;
    }

    @Override
    public BigDecimal lowestOrderValue()
    {
        return lowestOrderValue;
    }

    @Override
    public BigDecimal highestOrderValue()
    {
        return highestOrderValue;
    }

    @Override
    public Map<String, BigDecimal> orderValueByCountry()
    {
        return orderValueByCountry;
    }

    public String getReportString(NumberFormat nf)
    {
        StringBuilder contentToWrite = new StringBuilder();
        StringBuilder countryValueSB = new StringBuilder();

        contentToWrite.append("Order count = ").append(String.format("%,d", this.orderCount)).append("\n");
        contentToWrite.append("High value order count = ").append(String.format("%,d", this.highValueOrderCount)).append("\n");
        contentToWrite.append("Export check order count = ").append(String.format("%,d", this.exportCheckOrderCount)).append("\n");
        contentToWrite.append("Total order value = ").append(String.format("%s", nf.format(this.totalOrderValue))).append("\n");
        contentToWrite.append("Average order value = ").append(String.format("%s", nf.format(this.avgOrderValue))).append("\n");
        contentToWrite.append("Lowest order value = ").append(String.format("%s", nf.format(this.lowestOrderValue))).append("\n");
        contentToWrite.append("Highest order value = ").append(String.format("%s", nf.format(this.highestOrderValue))).append("\n");
        contentToWrite.append("Order value by country:").append("\n");
        for (String country : this.orderValueByCountry.keySet())
            countryValueSB.append(String.format("%2s: %s", country, nf.format(this.orderValueByCountry().get(country)))).append("\n");
        contentToWrite.append(countryValueSB);
        return contentToWrite.toString();
    }
}
