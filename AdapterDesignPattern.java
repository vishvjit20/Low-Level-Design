interface IPayment {
    void pay();
}

class GrowwPayment implements IPayment {
    @Override
    public void pay() {
        System.out.println("Payment by groww");
    }
}

class StripePayment {
    public void stripePayment() {
        System.out.println("Stripe own payment third party");
    }
}

class PaymentAdapterStripe implements IPayment {
    StripePayment payment;
    public PaymentAdapterStripe() {
        payment = new StripePayment();
    }
    @Override
    public void pay() {
        payment.stripePayment();
    }
}

public class AdapterDesignPattern {
    public static void main(String[] args) {
        IPayment payment = new PaymentAdapterStripe();
        payment.pay();
    }
}
