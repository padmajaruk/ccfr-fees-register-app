package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.ChannelTypeService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.*;


/**
 * Created by tarun on 02/11/2017.
 */

public class Fee2CrudComponentTest extends BaseTest {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    private CreateRangedFeeDto rangedFeeDto;

    @Autowired
    private ChannelTypeService channelTypeService;

    private String feeCode;


    /**
     *
     */
    @Test
    public void createRangedFeeTest() {
        rangedFeeDto = getRangedFeeDto("XCRUD00");
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        assertNotNull(savedFee);
    }

    @Test(expected = BadRequestException.class)
    public void duplicateFeeCreationTest() {
        rangedFeeDto = getRangedFeeDto("XCRUD00");
        feeService.save(feeDtoMapper.toFee(rangedFeeDto));
    }

    @Test
    public void createRangedFeeWithAllReferenceDataTest() {
        feeCode = UUID.randomUUID().toString();
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 3000, feeCode, FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        Fee2Dto feeDto = feeDtoMapper.toFeeDto(savedFee);

        assertEquals(rangedFeeDto.getCode(), feeCode);
        FeeVersionDto feeVersionDtoResult = feeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");
        assertEquals(feeVersionDtoResult.getFlatAmount().getAmount(), new BigDecimal(2500));
    }

    @Test
    @Transactional
    public void ReadRangedFeeWithAllReferenceDataTest() {
        // Insert a new ranged fee
        feeCode = UUID.randomUUID().toString();
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2000, feeCode, FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        Fee fee = feeService.get(feeCode);

        Fee2Dto feeDto = feeDtoMapper.toFeeDto(fee);
        assertEquals(rangedFeeDto.getCode(), feeCode);
        FeeVersionDto feeVersionDtoResult = feeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");
    }


    @Test
    public void createDraftFeeAndApproveTheFeeTest() {
        // Insert a new ranged fee
        feeCode = UUID.randomUUID().toString();
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2999, feeCode, FeeVersionStatus.draft);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        Fee fee = feeService.get(feeCode);

        boolean result = feeService.approve(fee.getCode(), 1);
        assertTrue(result);
    }


}