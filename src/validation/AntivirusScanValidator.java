package validation;

import model.Document;

public class AntivirusScanValidator extends ValidationHandler {
    @Override
    public String getStationName() {
        return "TRẠM 2 - QUÉT MÃ ĐỘC (ANTIVIRUS)";
    }

    @Override
    protected void check(Document doc) throws DocumentValidationException {
        String path = doc.getFilePath().toLowerCase();
        // Giả lập phát hiện mã độc qua tên file để phục vụ Demo
        if (path.contains("virus") || path.contains("malware") || path.endsWith(".exe")) {
            throw new DocumentValidationException(getStationName(), 
                "Phát hiện mã độc/tệp thực thi giả mạo bên trong hồ sơ!");
        }
    }
}