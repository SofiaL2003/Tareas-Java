package Semana_2.codigos;

import java.util.*;

enum EventType { ORDER_PLACED, ORDER_SHIPPED, ORDER_CANCELLED }

@FunctionalInterface
interface EventHandler {
    void handle(String eventData);
}

interface NotificationStrategy {
    void send(String message);
}

class EmailStrategy implements NotificationStrategy {
    @Override
    public void send(String message) {
        System.out.println("[EMAIL] Enviando: " + message);
    }
}

class SmsStrategy implements NotificationStrategy {
    @Override
    public void send(String message) {
        System.out.println("[SMS] Enviando: " + message);
    }
}

class SlackStrategy implements NotificationStrategy {
    @Override
    public void send(String message) {
        System.out.println("[SLACK] Enviando: " + message);
    }
}

class EventBus {
    // TODO: Map de EventType a lista de handlers
    private final Map<EventType, List<EventHandler>> listeners = new HashMap<>();

    public EventBus() {
        // TODO: inicializar lista vacia para cada EventType
        for (EventType type : EventType.values()) {
            listeners.put(type, new ArrayList<>());
        }
    }

    public void subscribe(EventType type, EventHandler handler) {
        // TODO: agregar handler a la lista del tipo
        listeners.get(type).add(handler);
    }

    public void publish(EventType type, String data) {
        System.out.printf("[BUS] Publicando %s%n", type);
        // TODO: ejecutar todos los handlers del tipo
        for (EventHandler handler : listeners.get(type)) {
            handler.handle(data);
        }
    }
}

class OrderProcessingException extends Exception {
    // TODO: constructor con mensaje
    public OrderProcessingException(String message) {
        super(message);
    }
}

class OrderService {
    private final EventBus eventBus;
    private final NotificationStrategy notifier;

    // TODO: constructor con DI
    public OrderService(EventBus eventBus, NotificationStrategy notifier) {
        this.eventBus = eventBus;
        this.notifier = notifier;
    }

    public void placeOrder(String orderId, double amount)
            throws OrderProcessingException {
        // TODO: validar amount > 0, si no lanzar excepcion
        if (amount <= 0) throw new OrderProcessingException(
            "Monto invalido para orden " + orderId + ": $" + amount);

        String data = String.format("Orden %s por $%.2f", orderId, amount);
        eventBus.publish(EventType.ORDER_PLACED, data);
        notifier.send("Nueva orden: " + data);
    }

    public void shipOrder(String orderId) {
        eventBus.publish(EventType.ORDER_SHIPPED,
            "Orden " + orderId + " enviada");
    }

    public void cancelOrder(String orderId) {
        eventBus.publish(EventType.ORDER_CANCELLED,
            "Orden " + orderId + " cancelada");
    }
}

public class EventFramework {
    public static void main(String[] args) {
        EventBus bus = new EventBus();

        // Suscribir handlers con lambdas
        bus.subscribe(EventType.ORDER_PLACED,
            data -> System.out.println("  [LOG] " + data));
        bus.subscribe(EventType.ORDER_SHIPPED,
            data -> System.out.println("  [LOG] " + data));
        bus.subscribe(EventType.ORDER_CANCELLED,
            data -> System.out.println("  [ALERTA] " + data));

        // Crear servicio con Email strategy
        System.out.println("=== Ordenes con Email ===");
        OrderService service = new OrderService(bus, new EmailStrategy());
        try {
            service.placeOrder("ORD-001", 299.99);
            service.shipOrder("ORD-001");
        } catch (OrderProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Orden invalida
        System.out.println("\n=== Orden Invalida ===");
        try {
            service.placeOrder("ORD-002", -50.00);
        } catch (OrderProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Cambiar estrategia a SMS
        System.out.println("\n=== Cambiar a SMS Strategy ===");
        OrderService serviceSms = new OrderService(bus, new SmsStrategy());
        try {
            serviceSms.placeOrder("ORD-003", 150.00);
            serviceSms.cancelOrder("ORD-003");
        } catch (OrderProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Para usar SlackStrategy:
        //new SlackStrategy().send("Prueba de notificacion Slack");
    }
}