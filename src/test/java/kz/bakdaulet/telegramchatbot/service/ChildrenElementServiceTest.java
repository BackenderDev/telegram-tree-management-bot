package kz.bakdaulet.telegramchatbot.service;

import kz.bakdaulet.telegramchatbot.model.ChildrenElement;
import kz.bakdaulet.telegramchatbot.model.RootElement;
import kz.bakdaulet.telegramchatbot.repository.ChildrenElementRepository;
import kz.bakdaulet.telegramchatbot.repository.RootElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

@DisplayName("Unit-level testing for ChildrenElementService")
public class ChildrenElementServiceTest {
    private final static String CHILD_NAME = "qwerty";
    private ChildrenElementRepository childrenElementRepository;
    private RootElementRepository rootElementRepository;
    private ChildrenElementService childrenElementService;

    @BeforeEach
    public void init(){
        childrenElementRepository = Mockito.mock(ChildrenElementRepository.class);
        rootElementRepository = Mockito.mock(RootElementRepository.class);
        childrenElementService = new ChildrenElementService(childrenElementRepository,
                rootElementRepository);

        ChildrenElement childrenElement = new ChildrenElement();

        Mockito.when(childrenElementService.findByName(CHILD_NAME)).thenReturn(Optional.of(childrenElement));
    }

    @Test
    public void saveChildrenElement_shouldInvokeRepositorySave(){
        //given
        RootElement rootElement = new RootElement();
        rootElement.setId(123456);
        rootElement.setName("r1");
        rootElementRepository.save(rootElement);
        ChildrenElement childElement = new ChildrenElement();
        childElement.setRootElement(rootElement);
        childElement.setId(123456);
        childElement.setName("ch1");

        //when
        childrenElementService.save(childElement);

        //then
        Mockito.verify(childrenElementRepository).save(childElement);
    }
}
