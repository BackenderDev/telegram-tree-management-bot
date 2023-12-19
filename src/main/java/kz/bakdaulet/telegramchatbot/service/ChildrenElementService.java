package kz.bakdaulet.telegramchatbot.service;

import kz.bakdaulet.telegramchatbot.model.ChildrenElement;
import kz.bakdaulet.telegramchatbot.repository.ChildrenElementRepository;
import kz.bakdaulet.telegramchatbot.repository.RootElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChildrenElementService {
    private final ChildrenElementRepository childrenElementRepository;
    private final RootElementRepository rootElementRepository;
    public List<ChildrenElement> findAll(Integer root_id){
        return childrenElementRepository.findByRootElement(rootElementRepository.findById(root_id).orElseThrow());
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
}
