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
        List<RootElement> rootElementList = new ArrayList<>();
        rootElementRepository.findAll().forEach(rootElementList::add);

        return rootElementList;
    }

    @Transactional
    public boolean save(String rootElement){
        if(check(rootElement) == null) {
            save(new RootElement(rootElement));
            return true;
        }
        return false;
    }

    public RootElement check(String rootElement){
        return findByName(rootElement).orElse(null);
    }


    public Optional<RootElement> findByName(String name){
        return rootElementRepository.findByName(name);
    }
    @Transactional
    public void save(RootElement rootElement){
        rootElementRepository.save(rootElement);
    }



    @Transactional
    public void deleteAll(){
        rootElementRepository.deleteAll();
    }
}