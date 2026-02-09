package OrderExceptions;

public class DuplicateOrderIdException extends IllegalArgumentException
{
    public DuplicateOrderIdException(String orderId)
    {
        super("Duplicate order ID: " + orderId);
    }
}
