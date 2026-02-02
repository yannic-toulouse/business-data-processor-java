package model;

import java.math.BigDecimal;
import java.util.ArrayList;

import rules.BusinessRules;

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
		
		if(isoCountryCode.isValidCountry(country))
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
	
	public enum isoCountryCode {
			AF, AX, AL, DZ, AS, AD, AO, AI, AQ, AG, AR, AM, AW, AU, AT, AZ,
		    BS, BH, BD, BB, BY, BE, BZ, BJ, BM, BT, BO, BQ, BA, BW, BV, BR,
		    IO, BN, BG, BF, BI, KH, CM, CA, CV, KY, CF, TD, CL, CN, CX, CC,
		    CO, KM, CG, CD, CK, CR, CI, HR, CU, CW, CY, CZ, DK, DJ, DM, DO,
		    EC, EG, SV, GQ, ER, EE, SZ, ET, FK, FO, FJ, FI, FR, GF, PF, TF,
		    GA, GM, GE, DE, GH, GI, GR, GL, GD, GP, GU, GT, GG, GN, GW, GY,
		    HT, HM, VA, HN, HK, HU, IS, IN, ID, IR, IQ, IE, IM, IL, IT, JM,
		    JP, JE, JO, KZ, KE, KI, KP, KR, KW, KG, LA, LV, LB, LS, LR, LY,
		    LI, LT, LU, MO, MG, MW, MY, MV, ML, MT, MH, MQ, MR, MU, YT, MX,
		    FM, MD, MC, MN, ME, MS, MA, MZ, MM, NA, NR, NP, NL, NC, NZ, NI,
		    NE, NG, NU, NF, MK, MP, NO, OM, PK, PW, PS, PA, PG, PY, PE, PH,
		    PN, PL, PT, PR, QA, RE, RO, RU, RW, BL, SH, KN, LC, MF, PM, VC,
		    WS, SM, ST, SA, SN, RS, SC, SL, SG, SX, SK, SI, SB, SO, ZA, GS,
		    SS, ES, LK, SD, SR, SJ, SE, CH, SY, TW, TJ, TZ, TH, TL, TG, TK,
		    TO, TT, TN, TR, TM, TC, TV, UG, UA, AE, GB, US, UM, UY, UZ, VU,
		    VE, VN, VG, VI, WF, EH, YE, ZM, ZW;
		
		public static boolean isValidCountry(String code)
		{
			try
			{
				isoCountryCode.valueOf(code.toUpperCase());
				return true;
			}
			catch (@SuppressWarnings("unused") IllegalArgumentException e)
			{
				return false;
			}
		}
	};

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
