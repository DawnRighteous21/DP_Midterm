package notification;

import model.Document;

public interface DocumentSubject {
    void subscribe(DocumentObserver observer);
    void unsubscribe(DocumentObserver observer);
    void notifyObservers(Document doc, String oldStatus, String newStatus);
}
