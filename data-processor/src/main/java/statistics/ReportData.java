package statistics;

import java.math.BigDecimal;
import java.util.Map;

public record ReportData(
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
}
