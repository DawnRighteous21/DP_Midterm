package notification;

import model.Document;

public interface DocumentObserver {
    String getChannelName();
    void onStatusChanged(Document doc, String oldStatus, String newStatus);
}
