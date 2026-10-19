package service;

import extraction.ContentExtractor;
import extraction.ContentExtractorFactory;
import model.Document;
import notification.NotificationCenter;
import storage.DocumentStorageRepository;
import validation.DocumentValidationException;
import validation.ValidationPipeline;

public class DocumentProcessor {

    private final ValidationPipeline validationPipeline;
    private final ContentExtractorFactory extractorFactory;
    private DocumentStorageRepository storageRepository;
    private final NotificationCenter notificationCenter;

    public DocumentProcessor(
        ValidationPipeline validationPipeline,
        ContentExtractorFactory extractorFactory,
        DocumentStorageRepository storageRepository,
        NotificationCenter notificationCenter
    ) {
        this.validationPipeline = validationPipeline;
        this.extractorFactory = extractorFactory;
        this.storageRepository = storageRepository;
        this.notificationCenter = notificationCenter;
    }

    public void setStorageRepository(
        DocumentStorageRepository storageRepository
    ) {
        this.storageRepository = storageRepository;
        System.out.println(
            "\n[HỆ THỐNG] Đã chuyển đổi chiến lược lưu trữ sang: " +
                storageRepository.getStorageTypeName()
        );
    }

    public DocumentStorageRepository getStorageRepository() {
        return storageRepository;
    }

    public void saveDraft(Document draftDoc) {
        System.out.println(
            "\n=== THỰC THI LƯU NHÁP HỒ SƠ: " + draftDoc.getId() + " ==="
        );
        notificationCenter.changeDocumentStatus(draftDoc, "LUU_NHAP");
        storageRepository.save(draftDoc);
        System.out.println("[HOÀN TẤT] Lưu nháp hồ sơ thành công!");
    }

    public boolean process(Document doc) {
        System.out.println(
            "\n=== BẮT ĐẦU XỬ LÝ HỒ SƠ: " + doc.getId() + " ==="
        );

        notificationCenter.changeDocumentStatus(doc, "DA_TIEP_NHAN");

        try {
            validationPipeline.execute(doc);
            System.out.println(
                "  [KIỂM DUYỆT] Hồ sơ hợp lệ, vượt qua toàn bộ các trạm!"
            );
        } catch (DocumentValidationException e) {
            System.err.println(
                "  [TỪ CHỐI BỞI " + e.getStationName() + "] " + e.getMessage()
            );
            notificationCenter.changeDocumentStatus(doc, "TU_CHOI");
            return false;
        }

        notificationCenter.changeDocumentStatus(doc, "DANG_XET_DUYET");
        try {
            ContentExtractor extractor = extractorFactory.getExtractor(
                doc.getFileExtension()
            );
            String content = extractor.extractContent(doc.getFilePath());
            doc.setExtractedContent(content);
        } catch (Exception e) {
            System.err.println("  [CẢNH BÁO TRÍCH XUẤT] " + e.getMessage());
        }

        storageRepository.save(doc);

        notificationCenter.changeDocumentStatus(doc, "HOAN_TAT");
        System.out.println("=== XỬ LÝ HỒ SƠ THÀNH CÔNG ===");
        return true;
    }
}
