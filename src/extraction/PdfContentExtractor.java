package extraction;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfContentExtractor implements ContentExtractor {
    @Override
    public String extractContent(String filePath) {
        System.out.println("  -> [PdfContentExtractor] Phân tích cấu trúc PDF và bóc tách lớp văn bản: " + filePath);
        try {
            byte[] rawBytes = Files.readAllBytes(Paths.get(filePath));
            return "[PDF Extracted Content (" + rawBytes.length + " bytes)] Nội dung từ: " + filePath;
        } catch (Exception e) {
            return "[Nội dung PDF giả lập] Văn bản bóc tách từ file PDF.";
        }
    }
}