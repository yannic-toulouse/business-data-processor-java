package statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Order;

public class StatisticsProcessor
{
    /**
     * Counts the number of orders in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Total amount of orders in orderList
     */
    public static int getOrderCount(List<Order> orderList)
    {
        return orderList.size();
    }

    /**
     * Counts the amount of high value orders in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Count of Order objects in orderList with the HIGH_VALUE flag
     */
    public static int getHighValueOrderCount(List<Order> orderList)
    {
        int cnt = 0;
        for (Order order : orderList)
        {
            if (order.getFlags().contains(Order.Flag.HIGH_VALUE))
            {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Counts the amount of orders marked as requiring an export check in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Count of Order objects in orderList with the EXPORT_CHECK flag
     */
    public static int getExportCheckOrderCount(List<Order> orderList)
    {
        int cnt = 0;
        for (Order order : orderList)
        {
            if (order.getFlags().contains(Order.Flag.EXPORT_CHECK))
            {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Sums order value of each Order in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Sum of orderValue of Order objects in orderList
     */
    public static BigDecimal getTotalOrderValue(List<Order> orderList)
    {
        BigDecimal sum = BigDecimal.ZERO;
        for (Order order : orderList)
        {
            sum = sum.add(order.getOrderValue());
        }
        return sum;
    }

    /**
     * Calculates the average order value in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Average order value of all Order objects in orderList, or {@code null} if the list is empty
     */
    public static BigDecimal getAvgOrderValue(List<Order> orderList)
    {
        if (!orderList.isEmpty())
        {
            BigDecimal cnt = BigDecimal.ZERO;
            BigDecimal sum = BigDecimal.ZERO;
            for (Order order : orderList)
            {
                cnt = cnt.add(BigDecimal.ONE);
                sum = sum.add(order.getOrderValue());
            }
            return sum.divide(cnt, 2, RoundingMode.HALF_UP);
        }
        return null;

    }

    /**
     * Finds the lowest order value in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Lowest order value in orderList, or {@code null} if the list is empty
     */
    public static BigDecimal getLowestOrderValue(List<Order> orderList)
    {
        if (orderList.isEmpty())
            return null;
        BigDecimal min = orderList.getFirst().getOrderValue();
        for (Order order : orderList)
        {
            if (order.getOrderValue().compareTo(min) < 0)
            {
                min = order.getOrderValue();
            }
        }
        return min;
    }

    /**
     * Finds the highest order value in a List
     *
     * @param orderList List of Order objects to be processed
     * @return Highest order value in orderList, or {@code null} if the list is empty
     */
    public static BigDecimal getHighestOrderValue(List<Order> orderList)
    {
        if (orderList.isEmpty())
            return null;
        BigDecimal max = orderList.getFirst().getOrderValue();
        for (Order order : orderList)
        {
            if (order.getOrderValue().compareTo(max) > 0)
            {
                max = order.getOrderValue();
            }
        }
        return max;
    }

    /**
     * Calculates the total order values by country and stores them in a Map
     *
     * @param orderList List of Order objects to be processed
     * @return Total order value by country in orderList
     */
    public static Map<String, BigDecimal> getOrderValueByCountry(List<Order> orderList)
    {
        Map<String, BigDecimal> countryMap = new HashMap<>();

        for (Order order : orderList)
        {
            countryMap.merge(order.getCountry(), order.getOrderValue(), BigDecimal::add);
        }
        return countryMap;
    }

    /**
     * Creates a complete report from all methods contained in {@code StatisticsProcessor}
     * @param orderList List of Order objects to be processed
     * @return ReportData record containing the generated statistics
     */
    public static ReportData fullReport(List<Order> orderList)
    {
        int orderCount = StatisticsProcessor.getOrderCount(orderList);
        int highValueOrderCount = StatisticsProcessor.getHighValueOrderCount(orderList);
        int exportCheckOrderCount = StatisticsProcessor.getExportCheckOrderCount(orderList);
        BigDecimal totalOrderValue = StatisticsProcessor.getTotalOrderValue(orderList);
        BigDecimal avgOrderValue = StatisticsProcessor.getAvgOrderValue(orderList);
        BigDecimal lowestOrderValue = StatisticsProcessor.getLowestOrderValue(orderList);
        BigDecimal highestOrderValue = StatisticsProcessor.getHighestOrderValue(orderList);
        Map<String, BigDecimal> orderValueByCountry = StatisticsProcessor.getOrderValueByCountry(orderList);

		return new ReportData(orderCount, highValueOrderCount, exportCheckOrderCount, totalOrderValue, avgOrderValue, lowestOrderValue, highestOrderValue, orderValueByCountry);
    }

}
