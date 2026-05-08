
package Semana_3.codigos;

import java.util.*;
import java.util.stream.Collectors;

public class Contact implements Comparable<Contact> {
    final private String name;
    final private String email;
    final private String phone;

    public Contact(String name, String email, String phone) {
        this.name = name; this.email = email; this.phone = phone;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    @Override
    public int compareTo(Contact other) {
        // TODO: orden natural por name (alfabetico)
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object o) {
        // TODO: igualdad basada en email
        if (this == o) return true;
        if (!(o instanceof Contact c)) return false;
        return this.email.equals(c.email);
    }

    @Override
    public int hashCode() {
        // TODO: hash basado en email
        return email.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Contact{name='%s', email='%s', phone='%s'}",
            name, email, phone);
    }
}

class ContactManager {
    private final Set<Contact> contacts = new TreeSet<>();

    public boolean addContact(Contact contact) {
        return contacts.add(contact);
    }

    public Optional<Contact> findByEmail(String email) {
        return contacts.stream()
            .filter(c -> c.getEmail().equals(email))
            .findFirst();
    }

    public List<Contact> findByNamePrefix(String prefix) {
        return contacts.stream()
            .filter(c -> c.getName().toLowerCase().startsWith(prefix.toLowerCase()))
            .collect(Collectors.toList());
    }

    public List<Contact> getAllSortedBy(Comparator<Contact> comp) {
        // TODO: retornar todos los contactos ordenados por el comparator dado
        return contacts.stream()
            .sorted(comp)
            .collect(Collectors.toList());
    }

    public int size() { return contacts.size(); }

    public static void main(String[] args) {
        ContactManager mgr = new ContactManager();

        System.out.println("=== Agregando Contactos ===");
        System.out.println("Ana: " + mgr.addContact(
            new Contact("Ana Garcia", "ana@mail.com", "555-1111")));
        System.out.println("Luis: " + mgr.addContact(
            new Contact("Luis Lopez", "luis@mail.com", "555-2222")));
        System.out.println("Maria: " + mgr.addContact(
            new Contact("Maria Torres", "maria@mail.com", "555-3333")));
        System.out.println("Ana duplicada: " + mgr.addContact(
            new Contact("Ana Garcia", "ana@mail.com", "555-9999")));
        System.out.println("Carlos: " + mgr.addContact(
            new Contact("Carlos Ruiz", "carlos@mail.com", "555-4444")));
        System.out.println("Total contactos: " + mgr.size());

        System.out.println("\n=== Orden Natural (por nombre) ===");
        mgr.getAllSortedBy(Comparator.naturalOrder())
            .forEach(System.out::println);

        System.out.println("\n=== Ordenados por Email ===");
        mgr.getAllSortedBy(Comparator.comparing(Contact::getEmail))
            .forEach(System.out::println);

        System.out.println("\n=== Buscar por Email ===");
        mgr.findByEmail("maria@mail.com")
            .ifPresentOrElse(
                c -> System.out.println("Encontrado: " + c),
                () -> System.out.println("No encontrado"));
        mgr.findByEmail("noexiste@mail.com")
            .ifPresentOrElse(
                c -> System.out.println("Encontrado: " + c),
                () -> System.out.println("No encontrado"));

        System.out.println("\n=== Buscar por Prefijo 'Ma' ===");
        mgr.findByNamePrefix("Ma").forEach(System.out::println);
    }
}