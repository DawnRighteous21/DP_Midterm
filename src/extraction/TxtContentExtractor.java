package extraction;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TxtContentExtractor implements ContentExtractor {
    @Override
    public String extractContent(String filePath) {
        System.out.println("  -> [TxtContentExtractor] Đọc trực tiếp chuỗi văn bản từ file .TXT: " + filePath);
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            return "[Nội dung TXT giả lập] Văn bản trích xuất từ " + filePath;
        }
    }
}
