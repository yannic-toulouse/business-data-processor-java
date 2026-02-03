package statistics;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Order;

public class StatisticsProcessor
{

	public BigDecimal getTotalOrderValue (List<Order> orderList)
	{
		BigDecimal sum = BigDecimal.ZERO;
		for (Order order : orderList)
		{
			sum = sum.add(order.getOrderValue());
		}
		return sum;
	}
	
	public static BigDecimal getAvgOrderValue (List<Order> orderList)
	{
		BigDecimal cnt = BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;
		for(Order order : orderList)
		{
			cnt = cnt.add(new BigDecimal(1));
			sum = sum.add(order.getOrderValue());
		}
		return sum.divide(cnt);
	}
	
	public static BigDecimal getHighestOrderValue (List<Order> orderList)
	{
		if(orderList.isEmpty())
			return BigDecimal.ZERO;
		BigDecimal max = BigDecimal.ZERO;
		for(Order order : orderList)
		{
			if(order.getOrderValue().compareTo(max) > 0)
			{
				max = order.getOrderValue();
			}
		}
		return max;
	}
	
	public static Map<String, BigDecimal> getOrderValueByCountry(List<Order> orderList)
	{
		Map<String, BigDecimal> countryMap = new HashMap<>();
		
		for(Order order : orderList)
		{
			countryMap.merge(order.getCountry(), order.getOrderValue(), BigDecimal::add);
		}
		return countryMap;
	}
	
	
	
}
