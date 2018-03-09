import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import services.InMemoryOrderService;
import services.OrderService;
import services.Transaction;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;
import static services.OrderService.queueCapacity;

public class InMemoryOrderServiceUT {


    PriorityBlockingQueue<Transaction> mockedOrderQueue = new PriorityBlockingQueue<>(queueCapacity, Transaction::compareTo);;
    ;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void testDequeue() throws ExecutionException, InterruptedException {

        mockedOrderQueue.add(new Transaction(new DateTime(), 3));

        when(OrderService.orderQueue).thenReturn(mockedOrderQueue);
        InMemoryOrderService service = new InMemoryOrderService();
        CompletableFuture<Void> futureResult = service.dequeueOldOrders(new DateTime());
        futureResult.get();
        assertEquals((mockedOrderQueue.size()), 1);


    }

}
