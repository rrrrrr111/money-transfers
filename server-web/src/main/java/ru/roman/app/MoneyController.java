package ru.roman.app;

import com.google.inject.Inject;
import ru.roman.app.account.MoneyTransferService;
import ru.roman.app.account.TransferResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("money")
public class MoneyController {

    @Inject
    private MoneyTransferService moneyTransferService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transfer")
    public TransferResult transfer(@QueryParam("debitAccount") String debitAccount,
                                   @QueryParam("creditAccount") String creditAccount,
                                   @QueryParam("amount") BigDecimal amount) {

        return moneyTransferService.transferAmount(debitAccount, creditAccount, amount);
    }
}
