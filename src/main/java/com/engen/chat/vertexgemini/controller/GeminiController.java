package com.engen.chat.vertexgemini.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.api.GenerationConfig;
import com.google.cloud.vertexai.api.HarmCategory;
import com.google.cloud.vertexai.api.SafetySetting;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import java.util.Map;

@RestController
public class GeminiController {

    public static Log logger = LogFactory.getLog(GeminiController.class);

    /**
     *
     * @return String
     */
    @RequestMapping(path = "/gemini", method = RequestMethod.GET)
    public ResponseEntity geminiChat() {
        try (VertexAI vertexAi = new VertexAI("mohanganesh", "us-central1");) {
            GenerationConfig generationConfig = GenerationConfig.newBuilder()
                    .setMaxOutputTokens(2048)
                    .setTemperature(0.9F)
                    .setTopP(1)
                    .build();
            GenerativeModel model = new GenerativeModel("gemini-1.0-pro-001", generationConfig, vertexAi);

            List<SafetySetting> safetySettings = Arrays.asList(
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HATE_SPEECH)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build(),
                    SafetySetting.newBuilder()
                            .setCategory(HarmCategory.HARM_CATEGORY_HARASSMENT)
                            .setThreshold(SafetySetting.HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE)
                            .build());

            // For multi-turn responses, start a chat session.
            ChatSession chatSession = model.startChat();

            GenerateContentResponse response;
            response = chatSession.sendMessage("tell me about adding button inside text area", safetySettings);
            System.out.println(ResponseHandler.getText(response));

            response = chatSession.sendMessage("give me the steps to springboot app with java 17", safetySettings);
            System.out.println(ResponseHandler.getText(response));
        } catch (IOException e) {
            logger.error("Something went wrong", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("alive");

    }

}