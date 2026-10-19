package validation;

import model.Document;

public abstract class ValidationHandler {
    private ValidationHandler nextHandler;

    public ValidationHandler setNext(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler; // Trả về nextHandler để hỗ trợ Fluent chaining nếu cần
    }

    // Template Method (được đánh dấu final để khóa cấu trúc định tuyến)
    public final void validate(Document doc) throws DocumentValidationException {
        System.out.println("  -> Đang kiểm duyệt tại: " + getStationName() + "...");
        check(doc); // Trạm hiện tại kiểm tra (sẽ ném Exception nếu có lỗi)
        
        // Nếu không có ngoại lệ, tự động chuyển tiếp sang trạm kế
        if (nextHandler != null) {
            nextHandler.validate(doc);
        }
    }

    public abstract String getStationName();

    protected abstract void check(Document doc) throws DocumentValidationException;
}