package model;

import java.io.File;
import java.util.UUID;

public class Document {
    private final String id;
    private final String applicantName;
    private final String applicantEmail;
    private final String applicantPhone;
    private final String officerName;
    private final String officerEmail;
    private final String officerPhone;
    private final String documentType;
    private final String filePath;
    private final String fileExtension;
    private final long fileSizeKB;
    private final String digitalSignature;
    private String extractedContent;
    private String status;

    private Document(DocumentBuilder builder) {
        this.id = builder.id;
        this.applicantName = builder.applicantName;
        this.applicantEmail = builder.applicantEmail;
        this.applicantPhone = builder.applicantPhone;
        this.officerName = builder.officerName;
        this.officerEmail = builder.officerEmail;
        this.officerPhone = builder.officerPhone;
        this.documentType = builder.documentType;
        this.filePath = builder.filePath;
        this.fileExtension = builder.fileExtension;
        this.fileSizeKB = builder.fileSizeKB;
        this.digitalSignature = builder.digitalSignature;
        this.extractedContent = builder.extractedContent;
        this.status = builder.status;
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public DocumentBuilder toBuilder() {
        return new DocumentBuilder(this);
    }

    public String getId() {
        return id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public String getOfficerName() {
        return officerName;
    }

    public String getOfficerEmail() {
        return officerEmail;
    }

    public String getOfficerPhone() {
        return officerPhone;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public long getFileSizeKB() {
        return fileSizeKB;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public String getExtractedContent() {
        return extractedContent;
    }

    public String getStatus() {
        return status;
    }

    public void setExtractedContent(String extractedContent) {
        this.extractedContent = extractedContent;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Hồ sơ [%s] - Nộp bởi: %s - Trạng thái: %s", id, applicantName, status);
    }

    public static class DocumentBuilder {
        private String id = UUID.randomUUID().toString().substring(0, 8);
        private String applicantName = "";
        private String applicantEmail = "";
        private String applicantPhone = "";
        private String officerName = "Cán bộ trực ban";
        private String officerEmail = "officer@tdtu.edu.vn";
        private String officerPhone = "0123456789";
        private String documentType = "DON_XIN_PHEP";
        private String filePath = "";
        private String fileExtension = "";
        private long fileSizeKB = 0;
        private String digitalSignature = "";
        private String extractedContent = null;
        private String status = "LUU_NHAP";

        public DocumentBuilder() {}

        public DocumentBuilder(Document existingDoc) {
            this.id = existingDoc.id;
            this.applicantName = existingDoc.applicantName;
            this.applicantEmail = existingDoc.applicantEmail;
            this.applicantPhone = existingDoc.applicantPhone;
            this.officerName = existingDoc.officerName;
            this.officerEmail = existingDoc.officerEmail;
            this.officerPhone = existingDoc.officerPhone;
            this.documentType = existingDoc.documentType;
            this.filePath = existingDoc.filePath;
            this.fileExtension = existingDoc.fileExtension;
            this.fileSizeKB = existingDoc.fileSizeKB;
            this.digitalSignature = existingDoc.digitalSignature;
            this.extractedContent = existingDoc.extractedContent;
            this.status = existingDoc.status;
        }

        public DocumentBuilder withId(String id) {
            if (id != null && !id.isBlank()) {
                this.id = id.trim();
            }
            return this;
        }

        public DocumentBuilder withApplicantInfo(String name, String email, String phone) {
            this.applicantName = name != null ? name.trim() : "";
            this.applicantEmail = email != null ? email.trim() : "";
            this.applicantPhone = phone != null ? phone.trim() : "";
            return this;
        }

        public DocumentBuilder withOfficerInfo(String name, String email, String phone) {
            this.officerName = name != null ? name.trim() : "";
            this.officerEmail = email != null ? email.trim() : "";
            this.officerPhone = phone != null ? phone.trim() : "";
            return this;
        }

        public DocumentBuilder withDocumentType(String documentType) {
            this.documentType = documentType != null ? documentType.trim() : "";
            return this;
        }

        public DocumentBuilder withAttachmentFile(File file) {
            if (file != null) {
                this.filePath = file.getAbsolutePath();
                this.fileSizeKB = file.length() / 1024;
                String name = file.getName();
                int lastDot = name.lastIndexOf('.');
                this.fileExtension = (lastDot > 0) ? name.substring(lastDot + 1) : "";
            }
            return this;
        }

        public DocumentBuilder withDigitalSignature(String digitalSignature) {
            this.digitalSignature = digitalSignature != null ? digitalSignature.trim() : "";
            return this;
        }

        public DocumentBuilder withExtractedContent(String extractedContent) {
            this.extractedContent = extractedContent;
            return this;
        }

        public DocumentBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public Document buildDraft() {
            this.status = "LUU_NHAP";
            return new Document(this);
        }

        public Document buildSubmitted() {
            this.status = "MOI_TAO";
            return new Document(this);
        }
    }
}