package kz.bakdaulet.telegramchatbot.service;

import kz.bakdaulet.telegramchatbot.model.RootElement;
import kz.bakdaulet.telegramchatbot.repository.RootElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("Unit-level testing for RootElementService")
public class RootElementServiceTest {
    private RootElementRepository rootElementRepository;
    private RootElementService rootElementService;

    @BeforeEach
    public void init(){
        rootElementRepository = Mockito.mock(RootElementRepository.class);
        rootElementService = new RootElementService(rootElementRepository);
    }

    @Test
    public void shouldProperlySaveRootElement(){
        //given
        RootElement rootElement = new RootElement();
        rootElement.setId(12345);
        rootElement.setName("root");

        //when
        rootElementService.save(rootElement);

        //then
        Mockito.verify(rootElementRepository).save(rootElement);
    }
}
