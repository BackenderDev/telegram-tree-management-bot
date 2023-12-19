package kz.bakdaulet.telegramchatbot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Child_element")
public class ChildrenElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "root_id", referencedColumnName = "id")
    private RootElement rootElement;

    public ChildrenElement(String name, RootElement rootElement) {
        this.name = name;
        this.rootElement = rootElement;
    }
}
