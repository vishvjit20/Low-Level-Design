


class Request {
    int priority;
    String work;
    public Request(int priority, String work) {
        this.priority = priority;
        this.work = work;
    }
}

abstract class LevelHandler {
    LevelHandler nextHandler;
    public LevelHandler(LevelHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    abstract boolean canHandle(Request request);
    abstract void processRequest(Request request);
}

class Level1Handler extends LevelHandler {

    public Level1Handler(LevelHandler nextHandler) {
        super(nextHandler);
        //TODO Auto-generated constructor stub
    }

    @Override
    boolean canHandle(Request request) {
        if (request.priority < 5) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    void processRequest(Request request) {
        if (canHandle(request)) {
            System.out.println("Level 1 Processing the request " + request.work);
        } else {
            nextHandler.processRequest(request);
        }
    }
    
}

class Level2Handler extends LevelHandler{

    public Level2Handler(LevelHandler nextHandler){
        super(nextHandler);
    }
    @Override
    boolean canHandle(Request request) {
        if(request.priority <= 10){
            return true;
        }else{
            return false;
        }
    }

    @Override
    void processRequest(Request request) {
        if(canHandle(request)){
            System.out.println("Level2 Processing the requests " + request.work);
        }else{
            nextHandler.processRequest(request);
        }
    }
}

class Level3Handler extends LevelHandler {

    public Level3Handler(LevelHandler nextHandler){
        super(nextHandler);
    }
    @Override
    boolean canHandle(Request request) {
        if(request.priority < 15){
            return true;
        }else{
            return false;
        }
    }

    @Override
    void processRequest(Request request) {
        if(canHandle(request)){
            System.out.println("Level3 Processing the requests " + request.work);
        }else{
            if(nextHandler != null){
                nextHandler.processRequest(request);
            }
            System.out.println("Last Handler so no futher handlers");
        }
    }
}

public class SupportTicketSystem {
    public static void main(String[] args) {
        LevelHandler handler = new Level1Handler(new Level2Handler(new Level3Handler(null)));
        Request request = new Request(8, "Work");
        handler.processRequest(request);
    }
}
