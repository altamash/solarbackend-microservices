package com.solaramps.api.it;

import com.solaramps.api.it.config.DocumentManagementApplication;
import com.solaramps.api.it.config.DocumentManagementServiceITContextConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc

//@RunWith(SpringRunner.class)
@SpringBootTest
        (
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = DocumentManagementApplication.class)
//@ContextConfiguration(classes = DocumentManagementApplication.class)
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@Import(DocumentManagementServiceITContextConfiguration.class)
@ActiveProfiles("test")
public class DocumentConfigurationIT {

    @Test
    public void contextLoads() {
    }
}
