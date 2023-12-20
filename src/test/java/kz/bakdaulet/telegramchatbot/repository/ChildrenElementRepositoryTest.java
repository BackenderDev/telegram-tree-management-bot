package kz.bakdaulet.telegramchatbot.repository;

import kz.bakdaulet.telegramchatbot.model.ChildrenElement;
import kz.bakdaulet.telegramchatbot.model.RootElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ChildrenElementRepositoryTest {
    @Autowired
    private ChildrenElementRepository childrenElementRepository;
    @Autowired
    private RootElementRepository rootElementRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/fiveChildForRoot.sql"})
    @Test
    public void shouldProperlyFindByRootElement(){
        Optional<RootElement> rootElement = rootElementRepository.findById(1);
        //when
        List<ChildrenElement> child = new ArrayList<>();
        if (rootElement.isPresent()) {
            child = childrenElementRepository.findByRootElement(rootElement.get());
        }

        //then
        Assertions.assertEquals(5, child.size());
    }
}
