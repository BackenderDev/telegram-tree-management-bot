package kz.bakdaulet.telegramchatbot.repository;

import kz.bakdaulet.telegramchatbot.model.RootElement;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RootElementRepository extends CrudRepository<RootElement, Integer> {
    Optional<RootElement> findByName(String name);
}
