package model;

import java.math.BigDecimal;
import java.util.ArrayList;

import OrderExceptions.DuplicateOrderIdException;
import OrderExceptions.NegativeOrderValueException;
import rules.BusinessRules;

import validation.Validator;

public class Order
{
	private static final ArrayList<String> orderIDList = new ArrayList<>();
	
	private final String orderID;
	private final String customer;
	private final BigDecimal orderValue;
	private final String country;
	private OrderStatus status;
	private final ArrayList<Flag> flags = new ArrayList<>();

	public Order(String orderID, String customer, BigDecimal orderValue, String country, OrderStatus status)
	{
		if (orderIDList.contains(orderID))
			throw new DuplicateOrderIdException(orderID);
		
		this.orderID = orderID;
		orderIDList.add(orderID);
		this.customer = customer;
		if (orderValue.compareTo(new BigDecimal(0)) >= 0)
		{
			this.orderValue = orderValue;
		}
		else
		{
			throw new NegativeOrderValueException(orderValue);
		}
		
		country = country.toUpperCase();
		if(Validator.validateISOCountryCode(country))
			this.country = country;
		else
			throw new IllegalArgumentException("Invalid country code. Must be in ISO 3166-1 alpha-2 format!");
		
		this.status = status;
		
		if (this.orderValue.compareTo(BusinessRules.getHighValueLimit()) > 0)
			flags.add(Flag.HIGH_VALUE);
		
		if(!BusinessRules.isEUCountry(country))
			flags.add(Flag.EXPORT_CHECK);
		
		
		if (this.flags.isEmpty())
			flags.add(Flag.NONE);
	}

	public enum OrderStatus
	{
		NEW,
		PAID,
		PROCESSING,
		SHIPPED,
		ON_HOLD,
		COMPLETED,
		CANCELLED;

		public boolean canTransitionTo(OrderStatus next)
		{
            return switch (this)
            {
                case NEW -> next == PAID || next == ON_HOLD || next == CANCELLED;
                case PAID -> next == PROCESSING || next == ON_HOLD || next == CANCELLED;
                case SHIPPED -> next == COMPLETED;
                case ON_HOLD -> next == PAID;
                default -> false;
            };
		}
	}

	public enum Flag
	{
		NONE,
		HIGH_VALUE,
		EXPORT_CHECK
    }

	public OrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(OrderStatus status)
	{
		if (this.status.canTransitionTo(status))
		{
			this.status = status;
		}
	}

	public String getOrderID()
	{
		return orderID;
	}

	public String getCustomer()
	{
		return customer;
	}

	public BigDecimal getOrderValue()
	{
		return orderValue;
	}

	public String getCountry()
	{
		return country;
	}
	
	public ArrayList<Flag> getFlags()
	{
		return flags;
	}

	@Override
	public String toString()
	{
		return "Order [orderID=" + orderID + ", customer=" + customer + ", orderValue=" + orderValue + ", country="
				+ country + ", status=" + status + "]";
	}
	
	public ArrayList<String> flagAsString()
	{
		ArrayList<String> flagStrings = new ArrayList<>();
		for (Flag flag : this.flags)
		{
			flagStrings.add(flag.toString().replace('_', ' '));
		}
		return flagStrings;
	}

}
