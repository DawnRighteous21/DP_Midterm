package storage;

import java.util.ArrayList;
import java.util.List;
import model.Document;

public class MySqlDatabaseStorageRepository
    implements DocumentStorageRepository
{

    private final List<Document> inMemoryDbTable = new ArrayList<>();

    @Override
    public String getStorageTypeName() {
        return "MySQL Relational Database";
    }

    @Override
    public void save(Document doc) {
        inMemoryDbTable.removeIf(existing ->
            existing.getId().equals(doc.getId())
        );
        inMemoryDbTable.add(doc);
        System.out.println(
            "  -> [MySQL Database] Đã thực thi INSERT/UPDATE thành công cho hồ sơ ID: " +
                doc.getId()
        );
    }

    @Override
    public List<Document> loadAll() {
        return new ArrayList<>(inMemoryDbTable);
    }
}
