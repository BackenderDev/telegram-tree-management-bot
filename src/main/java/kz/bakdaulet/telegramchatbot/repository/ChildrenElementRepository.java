package kz.bakdaulet.telegramchatbot.repository;

import kz.bakdaulet.telegramchatbot.model.ChildrenElement;
import kz.bakdaulet.telegramchatbot.model.RootElement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChildrenElementRepository extends CrudRepository<ChildrenElement, Integer> {
    List<ChildrenElement> findByRootElement(RootElement rootElement);
    Optional<ChildrenElement> findByName(String name);
}
