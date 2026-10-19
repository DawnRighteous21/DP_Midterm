package notification;

import model.Document;

public class SmsNotificationObserver extends BaseChannelObserver {

    @Override
    public String getChannelName() {
        return "SMS";
    }

    @Override
    protected void sendToRecipient(
        String recipientInfo,
        Document doc,
        String newStatus
    ) {
        System.out.println(
            "  [GỬI SMS]      -> " +
                recipientInfo +
                ": Hồ sơ [" +
                doc.getId() +
                "] chuyển sang trạng thái " +
                newStatus
        );
    }
}
