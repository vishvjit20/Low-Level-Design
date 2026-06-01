interface Payment {
    void pay();
}

interface Refund {
    void refund();
}

interface Invoice {
    void generate();
}

class PaytmPayment implements Payment {
    @Override
    public void pay() {
        System.out.println("Paytm payment done");
    }
}

class PaytmRefund implements Refund {
    @Override
    public void refund() {
        System.out.println("Paytm refund processed");
    }
}

class PaytmInvoice implements Invoice {
    @Override
    public void generate() {
        System.out.println("Paytm invoice generated");
    }
}

class RazorpayPayment implements Payment {
    @Override
    public void pay() {
        System.out.println("Razorpay payment done");
    }
}

class RazorpayRefund implements Refund {
    @Override
    public void refund() {
        System.out.println("Razorpay refund processed");
    }
}

class RazorpayInvoice implements Invoice {
    @Override
    public void generate() {
        System.out.println("Razorpay invoice generated");
    }
}

interface PaymentGatewayFactory {
    Payment createPayment();
    Refund createRefund();
    Invoice createInvoice();
}

class PaytmFactory implements PaymentGatewayFactory {

    @Override
    public Payment createPayment() {
        return new PaytmPayment();
    }

    @Override
    public Refund createRefund() {
        return new PaytmRefund();
    }

    @Override
    public Invoice createInvoice() {
        return new PaytmInvoice();
    }
    
}

class RazorpayFactory implements PaymentGatewayFactory {

    @Override
    public Payment createPayment() {
        return new RazorpayPayment();
    }

    @Override
    public Refund createRefund() {
        return new RazorpayRefund();
    }

    @Override
    public Invoice createInvoice() {
        return new RazorpayInvoice();
    }
    
}

public class AbstractFactoryPattern {
    public static void main(String[] args) {
        // Using Paytm Factory
        PaymentGatewayFactory paytmFactory = new PaytmFactory();

        Payment paytmPayment = paytmFactory.createPayment();
        Refund paytmRefund = paytmFactory.createRefund();
        Invoice paytmInvoice = paytmFactory.createInvoice();

        paytmPayment.pay();
        paytmRefund.refund();
        paytmInvoice.generate();

        System.out.println("-------------------");

        // Using Razorpay Factory
        PaymentGatewayFactory razorpayFactory = new RazorpayFactory();

        Payment razorpayPayment = razorpayFactory.createPayment();
        Refund razorpayRefund = razorpayFactory.createRefund();
        Invoice razorpayInvoice = razorpayFactory.createInvoice();

        razorpayPayment.pay();
        razorpayRefund.refund();
        razorpayInvoice.generate();
    }
}
