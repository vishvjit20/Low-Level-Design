import java.util.ArrayList;
import java.util.List;

interface IObserver {
    public void notify(int val);
}

class Vj implements IObserver {
    @Override
    public void notify(int val) {
        System.out.println("VJ value is changed " + val);
    }
}

class Amit implements IObserver {
    @Override
    public void notify(int val) {
        System.out.println("Amit value is changed " + val);
    }
}

interface IObservable {
    void addObserver(IObserver o);
    void deleteObserver(IObserver o);
    void notifyObserver(int val);
}

class YoutubeChannel implements IObservable {

    List<IObserver> subscribers;
    public YoutubeChannel() {
        this.subscribers = new ArrayList<>();
    }

    void videoUpload(int videoId){
        System.out.println("Video uploaded");
        notifyObserver(videoId);
    }

    @Override
    public void addObserver(IObserver o) {
        subscribers.add(o);
    }

    @Override
    public void deleteObserver(IObserver o) {
        subscribers.remove(o);
    }

    @Override
    public void notifyObserver(int val) {
        for(IObserver observer : subscribers){
            observer.notify(val);
        }
    }
    
}

public class Observer_Pattern {
    public static void main(String[] args) {
        IObserver observer1 = new Vj();
        IObserver observer2 = new Amit();

        YoutubeChannel youtubeChannel = new YoutubeChannel();
        youtubeChannel.addObserver(observer1);
        youtubeChannel.addObserver(observer2);

        youtubeChannel.videoUpload(122324);

        
    }
}
