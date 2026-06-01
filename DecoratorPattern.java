interface ILogger {
    void log(String messsage);
}

class ConsoleLogger implements ILogger {
    @Override
    public void log(String messsage) {
        System.out.println("Console Logger " + messsage);
    }
}

abstract class LoggerDecorator implements ILogger {
    protected ILogger logger;
    public LoggerDecorator(ILogger logger) {
        this.logger = logger;
    }
}

class TimeStampDecorator extends LoggerDecorator {
    public TimeStampDecorator(ILogger logger) {
        super(logger);
    }
    @Override
    public void log(String messsage) {
        System.out.println("Timestamp Added " + System.currentTimeMillis());
        logger.log(messsage);
    }
}

class JsonDecorator extends LoggerDecorator {

    public JsonDecorator(ILogger logger) {
        super(logger);
    }
    @Override
    public void log(String messsage) {
        System.out.println("Json Parsed log: ");
        logger.log(messsage);
    }
}

public class DecoratorPattern {
    public static void main(String[] args) {
        ILogger logger = new ConsoleLogger();
        String message = "vj teaching lld";
        
        logger = new JsonDecorator(new TimeStampDecorator(logger));
        logger.log(message);
    }
}
