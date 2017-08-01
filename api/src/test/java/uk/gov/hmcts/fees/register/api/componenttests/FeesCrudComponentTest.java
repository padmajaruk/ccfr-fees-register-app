package uk.gov.hmcts.fees.register.api.componenttests;

import java.math.BigDecimal;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto.percentageFeeDtoWith;

public class FeesCrudComponentTest extends ComponentTestBase {

    @Test
    public void retrieveAllFees() throws Exception {
        restActions
            .get("/fees")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(FeeDto.class, fees -> {
                assertThat(fees).contains(
                    fixedFeeDtoWith()
                        .code("X0433")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 5000.01 upto 10000 GBP")
                        .amount(41000)
                        .build(),

                    percentageFeeDtoWith()
                        .code("X0434")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value")
                        .percentage(BigDecimal.valueOf(4.5))
                        .build()
                );
            }));
    }

    @Test
    public void retrieveById() throws Exception {
        restActions
            .get("/fees/X0434")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeDto.class, fee -> {
                assertThat(fee).isEqualTo(
                    percentageFeeDtoWith()
                        .code("X0434")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value")
                        .percentage(BigDecimal.valueOf(4.5))
                        .build()
                );
            }));
    }

    @Test
    public void retrieveByUnknownId() throws Exception {
        restActions
            .get("/fees/-1")
            .andExpect(status().isNotFound());
    }
}
