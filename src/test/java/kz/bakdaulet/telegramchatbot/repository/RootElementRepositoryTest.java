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

import java.util.List;
import java.util.Optional;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RootElementRepositoryTest {
    @Autowired
    private RootElementRepository rootElementRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/fiveChildForRoot.sql"})
    @Test
    public void findById_shouldReturnRootWithCorrectChildrenElements(){
        //when
        Optional<RootElement> root = rootElementRepository.findById(1);

        //then
        Assertions.assertTrue(root.isPresent());
        Assertions.assertEquals(1, root.get().getId());
        List<ChildrenElement> child = root.get().getChildrenElements();
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(Integer.valueOf(i + 1), child.get(i).getId());
        }

    }
}
