package validation;

import model.Document;
import java.util.Set;

public class DuplicateSubmissionValidator extends ValidationHandler {
    private final Set<String> submittedSignatures;

    public DuplicateSubmissionValidator(Set<String> submittedSignatures) {
        this.submittedSignatures = submittedSignatures;
    }

    @Override
    public String getStationName() {
        return "TRẠM 3 - KIỂM TRA CHỐNG TRÙNG LẶP";
    }

    @Override
    protected void check(Document doc) throws DocumentValidationException {
        // Tạo dấu vân tay (Fingerprint) dựa trên Email và Chữ ký số
        String fingerprint = (doc.getApplicantEmail() + "|" + doc.getDigitalSignature()).toLowerCase();
        
        if (submittedSignatures != null && submittedSignatures.contains(fingerprint)) {
            throw new DocumentValidationException(getStationName(), 
                "Hồ sơ bị trùng lặp! Chữ ký số này đã được sử dụng trước đó.");
        }
        
        // Nếu hợp lệ, lưu vào danh sách giả lập để bắt trùng ở lần nộp sau (Demo)
        if (submittedSignatures != null) {
            submittedSignatures.add(fingerprint);
        }
    }
}