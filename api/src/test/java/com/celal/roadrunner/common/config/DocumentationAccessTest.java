package com.celal.roadrunner.common.config;

import com.celal.roadrunner.user.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentationController.class)
@Import(SecurityConfig.class)
class DocumentationAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @Test
    void redirectsScalarTrailingSlashWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/scalar/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scalar"));
    }
}
