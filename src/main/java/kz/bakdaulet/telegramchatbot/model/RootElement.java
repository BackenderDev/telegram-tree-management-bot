package kz.bakdaulet.telegramchatbot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString(exclude = "childrenElements")
@NoArgsConstructor
@Entity
@Table(name = "Root_element")
public class RootElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "rootElement")
    private List<ChildrenElement> childrenElements;
    public RootElement(String name){
        this.name = name;
    }
}
