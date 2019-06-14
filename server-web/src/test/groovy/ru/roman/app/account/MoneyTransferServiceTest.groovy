package ru.roman.app.account

import com.sun.net.httpserver.HttpServer
import groovy.transform.CompileStatic
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import ru.roman.app.JerseyServer

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder

import static ru.roman.app.GuiceConfig.INJECTOR_INSTANCE
import static ru.roman.app.JerseyServer.BASE_URI
import static ru.roman.app.account.TransferResult.AMOUNT_LESS_OR_EQUAL_ZERO
import static ru.roman.app.account.TransferResult.CAN_NOT_TRANSFER_TO_SAME_ACCOUNT
import static ru.roman.app.account.TransferResult.CREDIT_ACCOUNT_NOT_FOUND
import static ru.roman.app.account.TransferResult.DEBIT_ACCOUNT_NOT_FOUND
import static ru.roman.app.account.TransferResult.INSUFFICIENT_FUNDS
import static ru.roman.app.account.TransferResult.SUCCESS

@Test
@CompileStatic
class MoneyTransferServiceTest {

    private HttpServer server
    private MoneyTransferServiceImpl subj
    private Client client = ClientBuilder.newClient()

    @BeforeClass
    def before() throws IOException {
        this.server = JerseyServer.start()
        this.subj = INJECTOR_INSTANCE.getInstance(MoneyTransferService.class) as MoneyTransferServiceImpl
    }

    @AfterClass
    def after() {
        server.stop(0)
    }

    @DataProvider
    private Object[][] provider() {
        [
                ["1", 1.0g, "2", 2.0g, 1.0g, SUCCESS, 0.0g, 3.0g],
                ["1", 1.0g, "2", 2.0g, 0.555g, SUCCESS, 0.445g, 2.555g],
                ["1", 1.0g, "2", 2.0g, 2.0g, INSUFFICIENT_FUNDS, 1.0g, 2.0g],
                ["1", 1.0g, "2", 2.0g, -1.0g, AMOUNT_LESS_OR_EQUAL_ZERO, 1.0g, 2.0g],
                ["1", 1.0g, "1", 1.0g, 1.0g, CAN_NOT_TRANSFER_TO_SAME_ACCOUNT, 1.0g, 1.0g],
        ] as Object[][]
    }


    @Test(dataProvider = "provider")
    void "Test money transfer"(String debAccId, BigDecimal debAccBalance,
                               String crdAccId, BigDecimal crdAccBalance,
                               BigDecimal amountToTransfer, TransferResult expectedResult,
                               BigDecimal debAccResultBalance,
                               BigDecimal crdAccResultBalance) {

        subj.clearAccounts()
        subj.addAccount(new Account(id: debAccId, balance: debAccBalance))
        subj.addAccount(new Account(id: crdAccId, balance: crdAccBalance))

        TransferResult result = executeRequest(debAccId, crdAccId, amountToTransfer)

        assert result == expectedResult
        assert subj.findAccount(debAccId).balance == debAccResultBalance
        assert subj.findAccount(crdAccId).balance == crdAccResultBalance
    }

    void "Test money transfer, account not exist"() {

        subj.clearAccounts()
        subj.addAccount(new Account(id: "1", balance: 0.0g))

        TransferResult result = executeRequest("2", "3", 1.0g)

        assert result == DEBIT_ACCOUNT_NOT_FOUND

        result = executeRequest("1", "3", 1.0g)

        assert result == CREDIT_ACCOUNT_NOT_FOUND
    }

    private TransferResult executeRequest(String debAccId,
                                          String crdAccId,
                                          BigDecimal amountToTransfer) {

        client.target(BASE_URI + "money/transfer")
                .queryParam("debitAccount", debAccId)
                .queryParam("creditAccount", crdAccId)
                .queryParam("amount", amountToTransfer)
                .request().get()
                .readEntity(TransferResult.class)
    }
}