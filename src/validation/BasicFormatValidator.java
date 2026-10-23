package validation;

import model.Document;
import java.util.Set;

public class BasicFormatValidator extends ValidationHandler {
    private static final long MAX_FILE_SIZE_KB = 5120;
    private final Set<String> supportedExtensions;

    public BasicFormatValidator(Set<String> supportedExtensions) {
        this.supportedExtensions = supportedExtensions;
    }

    @Override
    public String getStationName() {
        return "TRẠM 1 - KIỂM TRA ĐỊNH DẠNG CƠ BẢN";
    }

    @Override
    protected void check(Document doc) throws DocumentValidationException {
        if (doc.getApplicantName().isEmpty() || doc.getApplicantEmail().isEmpty() ||
            doc.getFilePath().isEmpty() || doc.getDigitalSignature().isEmpty()) {
            throw new DocumentValidationException(getStationName(), "Thiếu trường thông tin bắt buộc.");
        }

        if (!doc.getApplicantEmail().contains("@")) {
            throw new DocumentValidationException(getStationName(), "Địa chỉ Email người nộp thiếu ký tự @.");
        }

        if (doc.getFileSizeKB() > MAX_FILE_SIZE_KB) {
            throw new DocumentValidationException(getStationName(),
                "Dung lượng tệp (" + doc.getFileSizeKB() + " KB) vượt quá 5MB.");
        }

        String ext = doc.getFileExtension().toLowerCase();
        if (supportedExtensions != null && !supportedExtensions.contains(ext)) {
            throw new DocumentValidationException(getStationName(),
                "Định dạng tệp ." + ext + " không nằm trong danh sách hỗ trợ.");
        }
    }
}
