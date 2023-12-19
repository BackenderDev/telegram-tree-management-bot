package kz.bakdaulet.telegramchatbot.service;

import kz.bakdaulet.telegramchatbot.model.RootElement;
import kz.bakdaulet.telegramchatbot.repository.RootElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RootElementService {
    private final RootElementRepository rootElementRepository;

    public List<RootElement> findAll(){
        Iterable<RootElement> rootElements = rootElementRepository.findAll();
        List<RootElement> rootElementList = new ArrayList<>();
        rootElements.forEach(rootElementList::add);

        return rootElementList;
    }

    public Optional<RootElement> findByName(String name){
        return rootElementRepository.findByName(name);
    }
    @Transactional
    public void save(RootElement rootElement){
        rootElementRepository.save(rootElement);
    }


    @Transactional
    public void delete(Integer id){
        rootElementRepository.deleteById(id);
    }
}