package extraction;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageOcrExtractorAdapter implements ContentExtractor {
    private final ThirdPartyAiOcrService aiOcrService;

    public ImageOcrExtractorAdapter(ThirdPartyAiOcrService aiOcrService) {
        this.aiOcrService = aiOcrService;
    }

    @Override
    public String extractContent(String filePath) {
        byte[] imageBytes = null;
        try {
            imageBytes = Files.readAllBytes(Paths.get(filePath));
        } catch (Exception e) {
            imageBytes = new byte[1024];
        }
        return aiOcrService.performOpticalCharacterRecognition(imageBytes, "vie+eng");
    }
}
