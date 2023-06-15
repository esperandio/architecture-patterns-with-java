package app.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private AllocateController allocateController;

    @Autowired
    private AddBatchController addBatchController;

    @Test
    void happyPathReturns201AndAllocatedBatch() {
        String sku = RandomStringUtils.random(10, true, false);

        String earliestBatchReference = RandomStringUtils.random(5, true, true);
        String mediumBatchReference = RandomStringUtils.random(5, true, true);
        String latestBatchReference = RandomStringUtils.random(5, true, true);

        addBatchController.addBatch(new AddBatchRequest(earliestBatchReference, sku, 10, null));
        addBatchController.addBatch(new AddBatchRequest(mediumBatchReference, sku, 10, LocalDateTime.now().plusDays(1)));
        addBatchController.addBatch(new AddBatchRequest(latestBatchReference, sku, 10, LocalDateTime.now().plusDays(2)));

        var response = allocateController.allocate(new AllocateRequest("order-001", sku, 1));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains(earliestBatchReference);
    }
}
