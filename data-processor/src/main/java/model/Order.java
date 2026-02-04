package model;

import java.math.BigDecimal;
import java.util.ArrayList;

import rules.BusinessRules;

import validation.Validator;

public class Order
{
	private static ArrayList<String> orderIDList = new ArrayList<String>();
	
	private String orderID;
	private String customer;
	private BigDecimal orderValue;
	private String country;
	private OrderStatus status;
	private ArrayList<Flag> flags = new ArrayList<Flag>();

	public Order(String orderID, String customer, BigDecimal orderValue, String country, OrderStatus status)
	{
		if (orderIDList.contains(orderID))
			throw new IllegalArgumentException("Order IDs must be unique!");
		
		this.orderID = orderID;
		orderIDList.add(orderID);
		this.customer = customer;
		if (orderValue.compareTo(new BigDecimal(0)) >= 0)
		{
			this.orderValue = orderValue;
		}
		else
		{
			throw new ExceptionInInitializerError("Order Value can't be negative!");
		}
		
		country = country.toUpperCase();
		if(Validator.validateISOCountryCode(country))
			this.country = country;
		else
			throw new IllegalArgumentException("Invalid country code. Must be in ISO 3166-1 alpha-2 format!");
		
		this.status = status;
		
		if (this.orderValue.compareTo(BusinessRules.getHighValueLimit()) > 0)
			flags.add(Flag.HIGH_VALUE);
		
		if(!BusinessRules.isEUCounry(country))
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
			switch (this)
			{
			case NEW:
				return next == PAID || next == ON_HOLD || next == CANCELLED;
			case PAID:
				return next == PROCESSING || next == ON_HOLD || next == CANCELLED;
			case SHIPPED:
				return next == COMPLETED;
			case ON_HOLD:
				return next == PAID;
			default:
				return false;
			}
		}
	}

	public enum Flag
	{
		NONE,
		HIGH_VALUE,
		EXPORT_CHECK;
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
		ArrayList<String> flagStrings = new ArrayList<String>();
		for (Flag flag : this.flags)
		{
			flagStrings.add(flag.toString().replace('_', ' '));
		}
		return flagStrings;
	}

}
