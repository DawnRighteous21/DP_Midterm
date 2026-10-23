package validation;

import model.Document;
import java.util.ArrayList;
import java.util.List;

public class ValidationPipeline {
    private final List<ValidationHandler> stations = new ArrayList<>();

    public void addStation(ValidationHandler station) {
        if (station != null) {
            stations.add(station);
            rebuildChain();
        }
    }

    public void clearStations() {
        stations.clear();
    }

    public List<ValidationHandler> getStations() {
        return new ArrayList<>(stations);
    }

    private void rebuildChain() {
        for (int i = 0; i < stations.size() - 1; i++) {
            stations.get(i).setNext(stations.get(i + 1));
        }
        if (!stations.isEmpty()) {
            stations.get(stations.size() - 1).setNext(null);
        }
    }

    public void execute(Document doc) throws DocumentValidationException {
        if (stations.isEmpty()) {
            System.out.println("  [CẢNH BÁO] Đường ống kiểm duyệt đang trống!");
            return;
        }
        stations.get(0).validate(doc);
    }
}
