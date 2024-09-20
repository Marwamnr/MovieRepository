package org.example.entities;

import jakarta.persistence.*; // Importerer JPA-annotationer
import lombok.Data; // Importerer Data annotation fra Lombok
import lombok.EqualsAndHashCode; // Importerer EqualsAndHashCode annotation fra Lombok
import java.util.HashSet; // Importerer HashSet
import java.util.Set; // Importerer Set

@Entity // Angiver at klassen er en JPA-entitet
@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@EqualsAndHashCode(of = "id") // Genererer equals og hashCode metoder baseret på ID
public class Actor {

    @Id // Angiver ID-feltet som primær nøgle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genererer ID automatisk
    private Long id;

    private String name;

    private String character;

    private String department;

    @ManyToMany(mappedBy = "actors") // Angiver en mange-til-mange relation til Movie
    private Set<Movie> movies = new HashSet<>(); // Sæt af film, som skuespilleren har været med i

    // Konstruktør med parametre
    public Actor(long id, String name, String character, String department) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.department = department;
    }

    // Standard konstruktør
    public Actor() {
    }

    // ToString metode for bedre læsbarhed ved udskrivning
    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", character='" + character + '\'' +
                '}';
    }
}

