import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import services.InMemoryOrderService;
import services.Order;
import services.Transaction;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class InMemoryOrderServiceUT {

    @Mock
    PriorityBlockingQueue<Transaction> orderQueue;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void testDequeue() throws ExecutionException, InterruptedException {

        orderQueue.add(new Transaction(new Date(), 3));

        when(Order.orderQueue).thenReturn(orderQueue);
        InMemoryOrderService service = new InMemoryOrderService();
        CompletableFuture<Void> futureResult = service.dequeueOldOrders(new Date());
        futureResult.get();
        assertEquals((orderQueue.size()), 0);


    }

}