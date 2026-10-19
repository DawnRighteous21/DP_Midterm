package extraction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ContentExtractorFactory {
    private final Map<String, ContentExtractor> extractorRegistry = new HashMap<>();

    public ContentExtractorFactory() {
        // Đăng ký sẵn các định dạng mặc định khi khởi tạo (Có thể thêm bớt ở Runtime)
        registerExtractor("txt", new TxtContentExtractor());
        registerExtractor("pdf", new PdfContentExtractor());

        ContentExtractor ocrAdapter = new ImageOcrExtractorAdapter(new ThirdPartyAiOcrService());
        registerExtractor("jpg", ocrAdapter);
        registerExtractor("jpeg", ocrAdapter);
        registerExtractor("png", ocrAdapter);
    }

    public void registerExtractor(String extension, ContentExtractor extractor) {
        if (extension != null && extractor != null) {
            extractorRegistry.put(extension.toLowerCase(Locale.ROOT).trim(), extractor);
        }
    }

    public boolean isSupported(String extension) {
        if (extension == null) return false;
        return extractorRegistry.containsKey(extension.toLowerCase(Locale.ROOT).trim());
    }

    public ContentExtractor getExtractor(String extension) {
        if (extension == null || !isSupported(extension)) {
            throw new UnsupportedOperationException("Định dạng tệp ." + extension + " chưa được hỗ trợ trích xuất.");
        }
        return extractorRegistry.get(extension.toLowerCase(Locale.ROOT).trim());
    }
}