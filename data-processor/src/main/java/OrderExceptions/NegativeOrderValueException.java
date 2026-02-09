package OrderExceptions;

import java.math.BigDecimal;

public class NegativeOrderValueException extends IllegalArgumentException
{
    public NegativeOrderValueException(BigDecimal value)
    {
        super("Order value cannot be negative: " + value);
    }
}
