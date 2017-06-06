package uk.gov.hmcts.fees.register.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees.register.model.Category;
import uk.gov.hmcts.fees.register.model.Fee;
import uk.gov.hmcts.fees.register.model.FeesRegister;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Repository
public class FeesRegisterRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FeesRegisterRepository.class);

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    private FeesRegister feesRegister = null;

    @Autowired
    public FeesRegisterRepository(ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        try {
            InputStream fileAsStream = resourceLoader.getResource("classpath:FeesRegister.json").getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileAsStream, "UTF-8"));

            feesRegister = objectMapper.readValue(reader, FeesRegister.class);
        } catch (IOException | NullPointerException e) {
            LOG.error("Loading of FeesRegister.json file failed");
            throw new FeesRegisterNotLoadedException("Loading of FeesRegister.json file failed", e);
        }
    }

    public FeesRegister getFeesRegister() {
        return feesRegister;
    }

    public List<Category> getAllCategories() {
        return getFeesRegister().getCategories();
    }

    public Optional<Fee> getFeeDetails(String eventId) {
        return getFeesRegister().getFeeDetails(eventId);
    }
}