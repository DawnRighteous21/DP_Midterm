package storage;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import model.Document;

public class LocalJsonStorageRepository implements DocumentStorageRepository {

    private final String storageDirPath = "server_storage";

    @Override
    public String getStorageTypeName() {
        return "Local JSON Storage";
    }

    @Override
    public void save(Document doc) {
        try {
            File storageDir = new File(storageDirPath);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }

            if (doc.getFilePath() != null && !doc.getFilePath().isEmpty()) {
                Path sourcePath = Paths.get(doc.getFilePath());
                if (Files.exists(sourcePath)) {
                    Path targetPath = Paths.get(
                        storageDirPath,
                        doc.getId() + "_" + sourcePath.getFileName().toString()
                    );
                    Files.copy(
                        sourcePath,
                        targetPath,
                        StandardCopyOption.REPLACE_EXISTING
                    );
                }
            }

            String safeContent =
                doc.getExtractedContent() != null
                    ? doc
                          .getExtractedContent()
                          .replace("\\", "\\\\")
                          .replace("\"", "\\\"")
                          .replace("\n", "\\n")
                    : "";

            String json =
                "{\n" +
                "  \"id\": \"" +
                doc.getId() +
                "\",\n" +
                "  \"applicantName\": \"" +
                doc.getApplicantName() +
                "\",\n" +
                "  \"status\": \"" +
                doc.getStatus() +
                "\",\n" +
                "  \"extractedContent\": \"" +
                safeContent +
                "\"\n" +
                "}";

            File dataFile = new File(
                storageDirPath + File.separator + doc.getId() + "_data.json"
            );
            try (FileWriter writer = new FileWriter(dataFile)) {
                writer.write(json);
            }
            System.out.println(
                "  -> [LocalJsonStorage] Đã lưu hồ sơ và xuất file: " +
                    dataFile.getName()
            );
        } catch (Exception e) {
            System.err.println(
                "  [LỖI LƯU TRỮ] Không thể lưu file cục bộ: " + e.getMessage()
            );
        }
    }

    @Override
    public List<Document> loadAll() {
        return new ArrayList<>();
    }
}
