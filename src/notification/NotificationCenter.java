package notification;

import java.util.ArrayList;
import java.util.List;
import model.Document;

public class NotificationCenter implements DocumentSubject {

    private final List<DocumentObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(DocumentObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unsubscribe(DocumentObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(
        Document doc,
        String oldStatus,
        String newStatus
    ) {
        for (DocumentObserver observer : observers) {
            observer.onStatusChanged(doc, oldStatus, newStatus);
        }
    }

    public void changeDocumentStatus(Document doc, String newStatus) {
        String oldStatus = doc.getStatus();
        doc.setStatus(newStatus);
        notifyObservers(doc, oldStatus, newStatus);
    }
}
