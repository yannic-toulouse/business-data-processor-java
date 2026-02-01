package model;

public class Order {
	int orderID;
	String customer;
	double orderValue;
	String country;
	OrderStatus status;
	
	public Order(int orderID, String customer, double orderValue, String country, OrderStatus status) {
		this.orderID = orderID;
		this.customer = customer;
		if (orderValue > 0)
		{
			this.orderValue = orderValue;
		}
		else
		{
			throw new ExceptionInInitializerError("Order Value must be above 0!");
		}
		this.country = country;
		this.status = status;
	}

	public enum OrderStatus {
		NEW,
		PAID,
		SHIPPED,
		ON_HOLD,
		COMPLETED;
		
		public boolean canTransitionTo(OrderStatus next)
		{
			switch(this)
			{
			case NEW:
				return next == PAID || next == ON_HOLD;
			case PAID:
				return next == SHIPPED || next == ON_HOLD;
			case SHIPPED:
				return next == COMPLETED;
			case ON_HOLD:
				return next == PAID;
			default:
				return false;
			}
		}
	}

	public OrderStatus getStatus()
	{
		return status;
	}

	public void setStatus(OrderStatus status)
	{
		if(this.status.canTransitionTo(status))
		{
			this.status = status;
		}
	}

	public int getOrderID()
	{
		return orderID;
	}

	public String getCustomer()
	{
		return customer;
	}

	public double getOrderValue()
	{
		return orderValue;
	}

	public String getCountry()
	{
		return country;
	}

	@Override
	public String toString()
	{
		return "Order [orderID=" + orderID + ", customer=" + customer + ", orderValue=" + orderValue + ", country="
				+ country + ", status=" + status + "]";
	}
	
}
