package uk.gov.hmcts.fees.register.service;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.model.Fee;
import uk.gov.hmcts.fees.register.model.FixedFee;
import uk.gov.hmcts.fees.register.model.PercentageFee;

@Component
public class FeesDtoFactory {


    public FeesDto toFeeDto(Fee fee, int claimAmount) {

        FeesDto feeDto = null;

        if (fee instanceof PercentageFee) {
            PercentageFee percentageFee = (PercentageFee) fee;

            feeDto = FeesDto.feeDtoWith()
                    .id(percentageFee.getId().toString())
                    .amount(claimAmount)
                    .description(percentageFee.getDescription())
                    .feeAmount(percentageFee.calculate(claimAmount))
                    .percentage(percentageFee.getPercentage())
                    .build();
        }


        if (fee instanceof FixedFee) {

            FixedFee fixedFee = (FixedFee) fee;

            feeDto = FeesDto.feeDtoWith()
                    .id(fixedFee.getId().toString())
                    .amount(claimAmount)
                    .description(fixedFee.getDescription())
                    .feeAmount(fixedFee.getAmount())
                    .build();


        }

        return feeDto;
    }
}