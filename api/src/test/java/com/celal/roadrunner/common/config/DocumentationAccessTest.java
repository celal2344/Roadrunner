package com.celal.roadrunner.common.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentationController.class)
@Import(SecurityConfig.class)
class DocumentationAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void redirectsScalarTrailingSlashWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/scalar/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scalar"));
    }
}
