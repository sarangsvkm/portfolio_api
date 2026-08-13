package com.sarangsvkm.portfolio_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarangsvkm.portfolio_api.dto.ResumeDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeParserService {

    private static final Logger log = LoggerFactory.getLogger(ResumeParserService.class);

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${gemini.api.key:}")
    private String apiKey;

    public ResumeParserService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Extracts raw text from an uploaded PDF file.
     */
    public String extractTextFromPdf(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse empty file");
        }
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            if (text == null || text.trim().isEmpty()) {
                throw new IOException("Could not extract any text from the PDF file");
            }
            return text;
        }
    }

    /**
     * Sends raw resume text to the Google Gemini API to parse it into structured ResumeDTO.
     */
    public ResumeDTO parseResumeText(String rawText) throws Exception {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("Google Gemini API Key is not configured. Please set the GEMINI_API_KEY environment variable.");
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        // Prompt instructing Gemini to structure the resume
        String prompt = "You are a professional ATS-compliant resume parser. Analyze the following raw text extracted from a resume PDF and parse it into a structured JSON object matching the exact schema provided. Return only the raw JSON. If a field is not found, use null or empty arrays.\n\n"
                + "JSON Schema:\n"
                + "{\n"
                + "  \"profile\": {\n"
                + "    \"name\": \"Full Name\",\n"
                + "    \"title\": \"Professional Title\",\n"
                + "    \"about\": \"Short bio/about description\",\n"
                + "    \"email\": \"Email Address\",\n"
                + "    \"phone\": \"Phone Number\",\n"
                + "    \"location\": \"City, State, Country\",\n"
                + "    \"imageUrl\": null,\n"
                + "    \"bannerUrl\": null,\n"
                + "    \"resumeUrl\": null\n"
                + "  },\n"
                + "  \"experiences\": [\n"
                + "    {\n"
                + "      \"company\": \"Company Name\",\n"
                + "      \"role\": \"Job Title/Role\",\n"
                + "      \"startDate\": \"YYYY-MM\",\n"
                + "      \"endDate\": \"YYYY-MM or Present\",\n"
                + "      \"description\": \"Description of duties and achievements\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"educations\": [\n"
                + "    {\n"
                + "      \"institution\": \"School/University Name\",\n"
                + "      \"degree\": \"Degree name\",\n"
                + "      \"fieldOfStudy\": \"Major/Field of study\",\n"
                + "      \"startDate\": \"YYYY-MM\",\n"
                + "      \"endDate\": \"YYYY-MM\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"skills\": [\n"
                + "    {\n"
                + "      \"name\": \"Skill Name (e.g. Java)\",\n"
                + "      \"level\": \"Advanced / Intermediate / Beginner\",\n"
                + "      \"category\": \"Category (e.g. Languages / Frameworks / Databases / Tools)\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"projects\": [\n"
                + "    {\n"
                + "      \"title\": \"Project Name\",\n"
                + "      \"description\": \"Project description\",\n"
                + "      \"techStack\": \"Comma-separated technologies used\",\n"
                + "      \"link\": \"Project link/URL\",\n"
                + "      \"imageUrl\": null\n"
                + "    }\n"
                + "  ]\n"
                + "}\n\n"
                + "Raw Resume Text:\n" + rawText;

        // Build the Gemini Request payload
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        Map<String, Object> partsContainer = new HashMap<>();
        partsContainer.put("parts", Collections.singletonList(textPart));

        Map<String, Object> contentContainer = new HashMap<>();
        contentContainer.put("contents", Collections.singletonList(partsContainer));

        // Request JSON formatting config
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("responseMimeType", "application/json");
        contentContainer.put("generationConfig", generationConfig);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(contentContainer, headers);

        log.info("Sending resume parse request to Google Gemini API...");
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to call Gemini API: " + response.getStatusCode());
        }

        // Extract JSON string from Gemini response
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode candidates = root.path("candidates");
        if (candidates.isMissingNode() || candidates.size() == 0) {
            throw new RuntimeException("Gemini returned no candidates.");
        }

        String jsonText = candidates.get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        if (jsonText == null || jsonText.trim().isEmpty()) {
            throw new RuntimeException("Gemini returned empty text.");
        }

        log.info("Successfully received structured JSON from Gemini API. Deserializing...");
        // Deserialize JSON text into ResumeDTO
        return objectMapper.readValue(jsonText, ResumeDTO.class);
    }
}
