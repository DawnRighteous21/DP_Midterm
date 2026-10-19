package extraction;

public class ThirdPartyAiOcrService {
    public String performOpticalCharacterRecognition(byte[] imageBytes, String languageCode) {
        int size = imageBytes != null ? imageBytes.length : 0;
        System.out.println("  -> [3rd-Party AI OCR SDK] Đang nhận diện quang học (" + languageCode 
                + ") trên ảnh kích thước " + size + " bytes...");
        return "[AI OCR Text] Văn bản nhận diện tự động từ hình ảnh scan.";
    }
}