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

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ChildrenElementRepositoryTest {
    @Autowired
    private ChildrenElementRepository childrenElementRepository;

    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/fiveChildForRoot.sql"})
    @Test
    public void shouldProperlyFindByRootElement(){
        //when
//        List<ChildrenElement> child = childrenElementRepository.findByRootElement(new RootElement("root1"));

        //then
//        Assertions.assertEquals(5, child.size());
    }
}
