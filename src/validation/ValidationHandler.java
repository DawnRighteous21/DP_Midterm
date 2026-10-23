package validation;

import model.Document;

public abstract class ValidationHandler {
    private ValidationHandler nextHandler;

    public ValidationHandler setNext(ValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public final void validate(Document doc) throws DocumentValidationException {
        System.out.println("  -> Đang kiểm duyệt tại: " + getStationName() + "...");
        check(doc);

        if (nextHandler != null) {
            nextHandler.validate(doc);
        }
    }

    public abstract String getStationName();

    protected abstract void check(Document doc) throws DocumentValidationException;
}
