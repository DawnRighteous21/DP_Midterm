package storage;

import java.util.List;
import model.Document;

public interface DocumentStorageRepository {
    String getStorageTypeName();
    void save(Document doc);
    List<Document> loadAll();
}
