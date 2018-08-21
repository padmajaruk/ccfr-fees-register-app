package uk.gov.hmcts.fees2.register.api.controllers.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.VolumeAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto.feeVersionDtoWith;
import static uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto.rangedFeeDtoWith;

public class FeeDataUtils {

    @Autowired
    private ObjectMapper objectMapper;

    public FixedFeeDto getCreateProbateCopiesFeeRequest() {
        return fixedFeeDtoWith()
            .channel("default")
            .event("copies")
            .jurisdiction1("family")
            .jurisdiction2("probate registry")
            .service("probate")
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.511Z").toDate())
                .description("Additional copies of the grant representation")
                .status(FeeVersionStatus.approved)
                .memoLine("Additional sealed copy of grant")
                .direction("enhanced")
                .naturalAccountCode("4481102171")
                .siRefId("8b")
                .statutoryInstrument("2014 No 876(L19)")
                .feeOrderName("Non-Contentious Probate Fees")
                .volumeAmount(new VolumeAmountDto(new BigDecimal("0.5")))
                .build())
            .build();
    }

    public RangedFeeDto getCreateRangedFeeRequest() {
        return rangedFeeDtoWith()
            .minRange(new BigDecimal("5000.01"))
            .jurisdiction1("family")
            .jurisdiction2("probate registry")
            .service("probate")
            .channel("default")
            .event("issue")
            .applicantType("personal")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2011-04-04T00:00:00.000Z").toDate())
                .description("Personal Application for grant of Probate")
                .status(FeeVersionStatus.approved)
                .memoLine("Personal Application for grant of Probate")
                .naturalAccountCode("4481102158")
                .direction("enhanced")
                .feeOrderName("Non-Contentious Probate Fees")
                .statutoryInstrument("2011 No. 588 (L. 4)")
                .siRefId("2")
                .flatAmount(new FlatAmountDto(new BigDecimal("215.00")))
                .build())
            .build();
    }

    public FixedFeeDto getCreateFixedFeeRequest() {
        return fixedFeeDtoWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .service("civil money claims")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.000Z").toDate())
                .description("Civil Court fees - Money Claims - Claim Amount - Unspecified")
                .status(FeeVersionStatus.approved)
                .memoLine("GOV - Paper fees - Money claim >£200,000")
                .direction("enhanced")
                .naturalAccountCode("4481102133")
                .flatAmount(new FlatAmountDto(new BigDecimal("10000.00")))
                .build())
            .build();
    }

    public List<FixedFeeDto> getCreateFixedFeesWithKeywordRequest() {
        List<FixedFeeDto> fixedFeeDtos = new ArrayList<>();

        fixedFeeDtos.add(fixedFeeDtoWith()
            .channel("default")
            .event("miscellaneous")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("general")
            .keyword("financial-order")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.000Z").toDate())
                .description("General fees - Money Claims - Claim Amount - Unspecified")
                .status(FeeVersionStatus.approved)
                .memoLine("GOV - Paper fees - Money claim >£200,000")
                .direction("enhanced")
                .naturalAccountCode("4481102133")
                .flatAmount(new FlatAmountDto(new BigDecimal("199.99")))
                .build())
            .build());

        fixedFeeDtos.add(fixedFeeDtoWith()
            .channel("default")
            .event("general application")
            .jurisdiction1("family")
            .jurisdiction2("family court")
            .service("general")
            .keyword("without-notice")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2016-03-21T00:00:00.000Z").toDate())
                .description("Filing an application for a divorce, nullity or civil partnership dissolution – fees order 1.2.")
                .status(FeeVersionStatus.approved)
                .memoLine("GOV - App for divorce/nullity of marriage or CP")
                .direction("enhanced")
                .naturalAccountCode("4481102159")
                .flatAmount(new FlatAmountDto(new BigDecimal("550.00")))
                .build())
            .build());


        return fixedFeeDtos;
    }

    public FixedFeeDto getCmcUnspecifiedFee() throws Exception {
        return fixedFeeDtoWith()
            .channel("default")
            .event("issue")
            .jurisdiction1("civil")
            .jurisdiction2("county court")
            .service("civil money claims")
            .unspecifiedClaimAmount(true)
            .applicantType("all")
            .version(feeVersionDtoWith()
                .validFrom(DateTime.parse("2014-04-22T00:00:00.000Z").toDate())
                .description("Civil Court fees - Money Claims - Claim Amount - Unspecified")
                .status(FeeVersionStatus.approved)
                .memoLine("GOV - Paper fees - Money claim >£200,000")
                .direction("enhanced")
                .naturalAccountCode("4481102133")
                .feeOrderName("Non-Contentious Probate Fees")
                .flatAmount(new FlatAmountDto(new BigDecimal("10000.00")))
                .build())
            .build();
    }

    public List<FixedFeeDto> getIncorrectFixedFeesDto() throws IOException {
        String  csvFees = "[\n" +
            "  {\n" +
            "    \"version\": {\n" +
            "     \"version\": \"1\",\n" +
            "     \"validFrom\": \"2017-11-06T16:33:37.040Z\",\n" +
            "     \"validTo\": \"2020-11-06T16:33:37.040Z\",\n" +
            "     \"description\": \"Testing1\",\n" +
            "     \"status\": \"draft\",\n" +
            "     \"direction\": \"enhanced1\",\n" +
            "     \"memoLine\": \"Test memo line\",\n" +
            "     \"feeOrderName\": \"CMC online fee order name\",\n" +
            "     \"naturalAccountCode\": \"Natural code 001\",\n" +
            "      \"flatAmount\": {\n" +
            "      \"amount\": \"150\"\n" +
            "      }\n" +
            "   },\n" +
            "   \"jurisdiction1\": \"family\",\n" +
            "   \"jurisdiction2\": \"court of protection\",\n" +
            "   \"service\": \"divorce\",\n" +
            "   \"channel\": \"default\",\n" +
            "   \"event\": \"issue\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"version\": {\n" +
            "   \"version\": \"1\",\n" +
            "   \"validFrom\": \"2017-11-06T16:33:37.040Z\",\n" +
            "   \"validTo\": \"2020-11-06T16:33:37.040Z\",\n" +
            "   \"description\": \"Testing2\",\n" +
            "   \"status\": \"approved\",\n" +
            "   \"direction\": \"enhanced\",\n" +
            "   \"memoLine\": \"Test memo line\",\n" +
            "   \"feeOrderName\": \"CMC online fee order name\",\n" +
            "   \"naturalAccountCode\": \"Natural code 002\",\n" +
            "   \"flatAmount\": {\n" +
            "     \"amount\": \"300\"\n" +
            "   }\n" +
            " },\n" +
            "   \"jurisdiction1\": \"family\",\n" +
            "   \"jurisdiction2\": \"court of protection\",\n" +
            "   \"service\": \"civil money claims\",\n" +
            "   \"channel\": \"default\",\n" +
            "   \"event\": \"issue\"\n" +
            "  }\n" +
            "]";

        TypeReference<List<FixedFeeDto>> fixedFeeDtos = new TypeReference<List<FixedFeeDto>>(){};
        return objectMapper.readValue(csvFees, fixedFeeDtos);
    }
}
