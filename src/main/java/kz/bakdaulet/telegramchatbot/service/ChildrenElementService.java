package kz.bakdaulet.telegramchatbot.service;

import kz.bakdaulet.telegramchatbot.model.ChildrenElement;
import kz.bakdaulet.telegramchatbot.model.RootElement;
import kz.bakdaulet.telegramchatbot.repository.ChildrenElementRepository;
import kz.bakdaulet.telegramchatbot.repository.RootElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildrenElementService {
    private final ChildrenElementRepository childrenElementRepository;
    private final RootElementRepository rootElementRepository;
    public List<ChildrenElement> findAll(Integer root_id){
        return childrenElementRepository.findByRootElement(rootElementRepository.findById(root_id).orElseThrow());
    }

    @Transactional
    public void saveAllData(Map<String, List<String>> map){
        for (Map.Entry<String, List<String>> entry : map.entrySet()){
            RootElement rootElement = rootElementRepository.findByName(entry.getKey()).orElse(null);
            if(rootElement == null){
                rootElementRepository.save(new RootElement(entry.getKey()));
                rootElement = rootElementRepository.findByName(entry.getKey()).orElseThrow();
                    for (String s : entry.getValue()) {
                        save(new ChildrenElement(s, rootElement));
                    }
            }else
                for (String s : entry.getValue()){
                    ChildrenElement childrenElement = findByName(s).orElse(null);
                    if (childrenElement == null) {
                        save(new ChildrenElement(s, rootElement));
                    }
                }
        }
    }

    public Map<String, List<String>> readData(){
        Map<String, List<String>> map = new LinkedHashMap<>();
        Iterable<RootElement> root = rootElementRepository.findAll();
        List<ChildrenElement> childList;
        List<String> childNames;
        for (RootElement rootElement : root) {
            childList = findAll(rootElement.getId());
            childNames = new ArrayList<>();
            for(ChildrenElement ch : childList){
                childNames.add(ch.getName());
            }
            map.put(rootElement.getName(), childNames);
        }
        return map;
    }


    @Transactional
    public boolean save(String child, RootElement rootElement){
        ChildrenElement childrenElement = findByName(child).orElse(null);
        if(childrenElement == null){
            save(new ChildrenElement(child, rootElement));
            return true;
        }
        return false;
    }


    @Transactional
    public boolean delete(String root){
        return rootElementRepository.findByName(root).map(this::deleteRootElement)
                .orElse(false);
    }

    @Transactional
    public boolean deleteRootElement(RootElement rootElement) {
        findAll(rootElement.getId()).forEach((ChildrenElement ch) -> delete(ch.getId()));
        rootElementRepository.delete(rootElement);
        return true;
    }
    public Optional<ChildrenElement> findByName(String name){
        return childrenElementRepository.findByName(name);
    }
    @Transactional
    public void save(ChildrenElement childrenElement){
        childrenElementRepository.save(childrenElement);
    }

    @Transactional
    public void delete(Integer id){
        childrenElementRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(){
        childrenElementRepository.deleteAll();
    }
}
