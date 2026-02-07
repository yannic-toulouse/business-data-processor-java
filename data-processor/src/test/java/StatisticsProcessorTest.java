import model.Order;
import org.junit.jupiter.api.Test;
import rules.BusinessRules;
import statistics.ReportData;
import statistics.StatisticsProcessor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StatisticsProcessorTest
{
    @Test
    public void getOrderCount_returnsListSize()
    {
        List<Order> orderList = List.of(
                order(new BigDecimal("10"), "DE"),
                order(new BigDecimal("20"), "US")
        );

        assertEquals(2, StatisticsProcessor.getOrderCount(orderList));
    }

    @Test
    public void getOrderCount_emptyList()
    {
        assertEquals(0, StatisticsProcessor.getOrderCount(List.of()));
    }

    @Test
    public void getHighValueOrderCount()
    {
        List<Order> orderList = List.of(
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "DE"),
                order(new BigDecimal("20"), "US"),
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "FR"),
                order(new BigDecimal("40"), "DE"),
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "IT")
        );
        assertEquals(3, StatisticsProcessor.getHighValueOrderCount(orderList));
    }

    @Test
    public void getExportCheckOrderCount()
    {
        List<Order> orderList = List.of(
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "DE"),
                order(new BigDecimal("20"), "US"),
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "FR"),
                order(new BigDecimal("40"), "DE"),
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "CN")
        );
        assertEquals(2, StatisticsProcessor.getExportCheckOrderCount(orderList));
    }

    @Test
    public void getTotalOrderValue()
    {
        List<Order> orderList = List.of(
                order(new BigDecimal("10"), "DE"),
                order(new BigDecimal("20"), "US")
        );

        assertEquals(0, new BigDecimal("30").compareTo(StatisticsProcessor.getTotalOrderValue(orderList)));
    }

    @Test
    public void getAvgOrderValue()
    {
        List<Order> orderList = List.of(
                order(new BigDecimal("10"), "DE"),
                order(new BigDecimal("20"), "US")
        );

        assertEquals(0, new BigDecimal("15.00").compareTo(StatisticsProcessor.getAvgOrderValue(orderList)));
    }

    @Test
    public void getAvgOrderValue_returnsNull()
    {
        List<Order> orderList = List.of();

        assertNull(StatisticsProcessor.getAvgOrderValue(orderList));
    }

    @Test
    public void getLowestOrderValue()
    {
        List<Order> orderList = List.of(
                order(new BigDecimal("40"), "DE"),
                order(new BigDecimal("10"), "DE"),
                order(new BigDecimal("20"), "US")
        );

        assertEquals(0, new BigDecimal("10").compareTo(StatisticsProcessor.getLowestOrderValue(orderList)));
    }

    @Test
    public void getLowestOrderValue_returnsNull()
    {
        List<Order> orderList = List.of();

        assertNull(StatisticsProcessor.getLowestOrderValue(orderList));
    }

    @Test
    public void getHighestOrderValue()
    {
        List<Order> orderList = List.of(
                order(new BigDecimal("10"), "DE"),
                order(new BigDecimal("40"), "DE"),
                order(new BigDecimal("20"), "US")
        );

        assertEquals(0, new BigDecimal("40").compareTo(StatisticsProcessor.getHighestOrderValue(orderList)));
    }

    @Test
    public void getHighestOrderValue_returnsNull()
    {
        List<Order> orderList = List.of();

        assertNull(StatisticsProcessor.getHighestOrderValue(orderList));
    }

    @Test
    public void getOrderValueByCountry()
    {
        List<Order> orderList = List.of(
                order(new BigDecimal("10"), "DE"),
                order(new BigDecimal("40"), "DE"),
                order(new BigDecimal("20"), "US")
        );

        Map<String, BigDecimal> resMap = Map.of(
                "DE", new BigDecimal("50"),
                "US", new BigDecimal("20")
        );

        assertEquals(resMap, StatisticsProcessor.getOrderValueByCountry(orderList));
    }

    @Test
    public void fullReport()
    {
        List<Order> orderList = List.of(
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "DE"),
                order(new BigDecimal("20"), "US"),
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "FR"),
                order(new BigDecimal("40"), "DE"),
                order(BusinessRules.getHighValueLimit().add(BigDecimal.ONE), "CN")
        );
        int orderCount = 5;
        int highValueOrderCount = 3;
        int exportCheckOrderCount = 2;
        BigDecimal totalOrderValue = BusinessRules.getHighValueLimit().multiply(new BigDecimal("3")).add(new BigDecimal("3")).add(new BigDecimal("60"));
        BigDecimal avgOrderValue = totalOrderValue.divide(new BigDecimal("5"), 2, RoundingMode.HALF_UP);
        BigDecimal lowestOrderValue = new BigDecimal("20");
        BigDecimal highestOrderValue = BusinessRules.getHighValueLimit().add(BigDecimal.ONE);
        Map<String, BigDecimal> orderValueByCountry = Map.of(
                "DE", BusinessRules.getHighValueLimit().add(BigDecimal.ONE).add(new BigDecimal("40")),
                "US", new BigDecimal("20"),
                "FR", BusinessRules.getHighValueLimit().add(BigDecimal.ONE),
                "CN", BusinessRules.getHighValueLimit().add(BigDecimal.ONE)
        );
        ReportData reportData = new ReportData(orderCount, highValueOrderCount, exportCheckOrderCount, totalOrderValue, avgOrderValue, lowestOrderValue, highestOrderValue, orderValueByCountry);
        assertEquals(reportData, StatisticsProcessor.fullReport(orderList));
    }

    private Order order(BigDecimal value, String country)
    {
        return new Order(
                UUID.randomUUID().toString(),
                "Test Customer",
                value,
                country,
                Order.OrderStatus.NEW
        );
    }
}
