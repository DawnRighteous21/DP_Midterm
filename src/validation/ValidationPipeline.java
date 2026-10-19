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
        // Tự động móc xích trạm hiện tại với trạm tiếp theo
        for (int i = 0; i < stations.size() - 1; i++) {
            stations.get(i).setNext(stations.get(i + 1));
        }
        // Trạm cuối cùng trỏ về null
        if (!stations.isEmpty()) {
            stations.get(stations.size() - 1).setNext(null);
        }
    }

    public void execute(Document doc) throws DocumentValidationException {
        if (stations.isEmpty()) {
            System.out.println("  [CẢNH BÁO] Đường ống kiểm duyệt đang trống!");
            return;
        }
        // Kích hoạt trạm đầu tiên, luồng sẽ tự động chạy qua các trạm nhờ Template Method
        stations.get(0).validate(doc);
    }
}