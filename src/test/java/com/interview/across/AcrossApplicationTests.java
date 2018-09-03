package com.interview.across;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.interview.across.exception.ErrorCode.BadRequest;
import com.interview.across.exception.ErrorCode.NotFound;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AcrossApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  private static final String EXTERNAL_REQUEST_ERROR = "External Request Error";

  @Test
  public synchronized void correctness() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"69.250.196.118\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";
    String result = this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
        .andReturn().getResponse().getContentAsString();
    if (!result.contains(EXTERNAL_REQUEST_ERROR)) {
      Assert.assertTrue(result.contains("demographics"));
      Assert.assertTrue(result.contains("publisher"));
      Assert.assertTrue(result.contains("country"));
    }
  }

  @Test
  public void testMissingParam() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"69.250.196.118\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";
    String result = this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
        .andReturn().getResponse().getContentAsString();
    if (!result.contains(EXTERNAL_REQUEST_ERROR)) {
      Assert.assertTrue(
          result.contains(String.format(BadRequest.MISSING_PARAMETER.getMessage(), "site.id")));
    }
  }

  @Test
  public synchronized void testNotUsIp() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"218.107.132.66\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";

    String result = this.mockMvc.perform(
        post("/RequestEnhance").contentType(MediaType.APPLICATION_JSON).content(requestModel))
        .andReturn().getResponse().getContentAsString();
    if (!result.contains(EXTERNAL_REQUEST_ERROR)) {
      Assert.assertTrue(result.contains(BadRequest.NOT_US_IP.getMessage()));
    }
  }

  @Test
  public synchronized void testNotInjectDemographics() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"218.107.132.66\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";

    String result = this.mockMvc.perform(
        post("/RequestEnhance?injectDemographics=false").contentType(MediaType.APPLICATION_JSON)
            .content(requestModel))
        .andReturn().getResponse().getContentAsString();
    if (!result.contains(EXTERNAL_REQUEST_ERROR)) {
      Assert.assertTrue(!result.contains("demographics"));
    }
  }

  @Test
  public synchronized void testNotInjectPublisher() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"218.107.132.66\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";

    String result = this.mockMvc.perform(
        post("/RequestEnhance?injectPublisher=false").contentType(MediaType.APPLICATION_JSON)
            .content(requestModel))
        .andReturn().getResponse().getContentAsString();
    if (!result.contains(EXTERNAL_REQUEST_ERROR)) {
      Assert.assertTrue(!result.contains("publisher"));
    }
  }

  @Test
  public synchronized void testNotInjectGeo() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"218.107.132.66\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";

    String result = this.mockMvc.perform(
        post("/RequestEnhance?injectGeo=false").contentType(MediaType.APPLICATION_JSON)
            .content(requestModel))
        .andReturn().getResponse().getContentAsString();
    if (!result.contains(EXTERNAL_REQUEST_ERROR)) {
      Assert.assertTrue(!result.contains("geo"));
    }
  }

  @Test
  public synchronized void testNotFoundPage() throws Exception {
    String requestModel = "{\n"
        + "\"site\": {\n"
        + "\"id\": \"foo123\",\n"
        + "\"page\": \"http://www.foo.com/why-foo\" },\n"
        + "\"device\": {\n"
        + "\"ip\": \"218.107.132.66\"\n"
        + "}, \"user\": {\n"
        + "\"id\": \"9cb89r\" }\n"
        + "}";

    String result = this.mockMvc.perform(
        post("/NotExistedPage").contentType(MediaType.APPLICATION_JSON).content(requestModel))
        .andReturn().getResponse().getContentAsString();
    Assert.assertTrue(!result.contains(NotFound.PAGE_NOT_FOUND.getMessage()));
  }

}
