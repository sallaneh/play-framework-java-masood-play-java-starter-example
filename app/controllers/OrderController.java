package controllers;

import javax.inject.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.*;
import play.Logger;

import java.util.Map;
import java.util.concurrent.CompletionStage;

import services.OrderService;
import services.Transaction;


@Singleton
public class OrderController extends Controller {

    private final OrderService orderService;

    @Inject
    public OrderController(OrderService order) {
        this.orderService = order;
    }

    public CompletionStage<Result> addOrder() {
        Logger.debug("Entering " + this.getClass().getSimpleName() + ".addOrder method; remoteAddress=" + request().remoteAddress() + " ; Content-type= " + request().contentType());
        Map<String, String[]> form = request().body().asFormUrlEncoded();
        String[] amountValue = form.get("sales_amount");
        if (amountValue == null) {
            Logger.debug("sales_amount field is not found in the request body: " + form.toString());

//            todo: Better error type should be returned both for field type and value type
            throw new IllegalArgumentException("\"sales_amount\" field is not found in the JSON body ");
        }
        int saleAmount = Integer.valueOf(amountValue[0]);
        Transaction transaction = new Transaction(new DateTime(), saleAmount);

        return orderService.addOrder(transaction).thenApply(result -> status(202, "Accepted"));
    }


    public CompletionStage<Result> getOrderStatistics() {

        Logger.debug("Entering " + this.getClass().getSimpleName() + ".getOrderStatistics method; remoteAddress=" + request().remoteAddress() + " ; Content-type= " + request().contentType());
        return orderService.getStatistics(new DateTime()).thenApply(stat -> {
            ObjectNode result = Json.newObject();
            result.put("total_sales_amount", stat.getTotalSalesAmount());
            result.put("average_amount_per_order:", stat.getAverageAmountPerOrder());
            return ok(result);
        });

    }

}
