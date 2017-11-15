package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.model.*;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "fee2DtoWith")
public class Fee2Dto {

    private String code;

    private String memoLine;

    private ChannelType channelTypeDto;

    private DirectionType directionTypeDto;

    private EventType eventTypeDto;

    private Jurisdiction1 jurisdiction1Dto;

    private Jurisdiction2 jurisdiction2Dto;

    private ServiceType serviceTypeDto;

    private String naturalAccountCode;

    private String feeOrderName;

    private List<FeeVersionDto> feeVersionDtos;

    // only ranged fee
    private BigDecimal minRange;

    private BigDecimal maxRange;

    private boolean unspecifiedClaimAmount;

}