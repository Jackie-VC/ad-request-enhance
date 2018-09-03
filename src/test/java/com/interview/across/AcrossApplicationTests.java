package com.interview.across;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AcrossApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void latencyTest() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"69.250.196.118\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";
    this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
//				.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(containsString("demographics")))
        .andExpect(MockMvcResultMatchers.content().string(containsString("publisher")))
        .andExpect(MockMvcResultMatchers.content().string(containsString("country")));
  }

  @Test
  public void testMissingSiteId() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"69.250.196.118\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";
    this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
//				.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(containsString("site.id")));
  }

  public void testMissingSitePage() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"device\": {\n"
        + "\"ip\": \"69.250.196.118\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";
    this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
//				.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(containsString("site.page")));
  }

  public void testMissingDeviceIp() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";
    this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
//				.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(containsString("device.ip")));
  }

}
