package notification;

import model.Document;

public class AppPushNotificationObserver extends BaseChannelObserver {

    @Override
    public String getChannelName() {
        return "APP_PUSH";
    }

    @Override
    protected void sendToRecipient(
        String recipientInfo,
        Document doc,
        String newStatus
    ) {
        System.out.println(
            "  [GỬI APP PUSH] -> " +
                recipientInfo +
                ": Hồ sơ [" +
                doc.getId() +
                "] chuyển sang trạng thái " +
                newStatus
        );
    }
}
