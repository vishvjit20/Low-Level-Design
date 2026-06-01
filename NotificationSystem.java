interface NotificationChannel {
    void sendNotification(String message);
}

class Whatsapp implements NotificationChannel {

    @Override
    public void sendNotification(String message) {
        System.out.println("Whatsapp"); 
    }
}

class SMS implements NotificationChannel {

    @Override
    public void sendNotification(String message) {
        System.out.println("SMS ...");
    }
}

class SendNotificatioService {
    NotificationChannel channel;

    public SendNotificatioService(NotificationChannel channel) {
        this.channel = channel;
    }

    void changeChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    void sendNotification(String message) {
        channel.sendNotification(message);
    }
}

public class NotificationSystem {

    public static void main(String[] args) {
        
        SendNotificatioService service = new SendNotificatioService(new SMS());
        service.sendNotification("This is a sample message");
        service.changeChannel(new Whatsapp());
        service.sendNotification("This is a sample message");
    }
}