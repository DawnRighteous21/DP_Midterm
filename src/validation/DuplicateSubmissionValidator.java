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
        String fingerprint = (doc.getApplicantEmail() + "|" + doc.getDigitalSignature()).toLowerCase();

        if (submittedSignatures != null && submittedSignatures.contains(fingerprint)) {
            throw new DocumentValidationException(getStationName(),
                "Hồ sơ bị trùng lặp! Chữ ký số này đã được sử dụng trước đó.");
        }

        if (submittedSignatures != null) {
            submittedSignatures.add(fingerprint);
        }
    }
}
