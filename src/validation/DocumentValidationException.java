package validation;

public class DocumentValidationException extends Exception {
    private final String stationName;

    public DocumentValidationException(String stationName, String reason) {
        super(reason);
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }

    @Override
    public String getMessage() {
        return String.format("[%s] %s", stationName, super.getMessage());
    }
}