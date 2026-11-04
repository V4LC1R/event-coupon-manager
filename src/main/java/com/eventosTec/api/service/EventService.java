package com.eventosTec.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.eventosTec.api.domain.event.Event;
import com.eventosTec.api.domain.event.EventRequestDTO;
import com.eventosTec.api.domain.event.EventResponseDTO;
import com.eventosTec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private AddressService addressService;

    public Event createEvent(EventRequestDTO data){
         String imageUrl = null;

         if(data.image()!=null){
             imageUrl = this.uploadImg(data.image());
         }

         Event newEvent = new Event();
         newEvent.setTitle(data.title());
         newEvent.setDescription(data.description());
         newEvent.setDate(new Date(data.date()));
         newEvent.setImageUrl(imageUrl);
         newEvent.setEventUrl(data.eventUrl());
         newEvent.setRemote(data.remote());

         eventRepo.save(newEvent);

         if (!data.remote()){
             addressService.createAddress(data,newEvent);
         }

         return newEvent;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page,int size){
        Pageable pageble = PageRequest.of(page, size);

        Page<Event> eventPage = eventRepo.findUpcomingEvents(new Date(),pageble);

        return eventPage.map(evt -> new EventResponseDTO(
                evt.getId(),
                evt.getTitle(),
                evt.getDescription(),
                evt.getDate(),
                "",
                "",
                evt.getRemote(),
                evt.getEventUrl(),
                evt.getImageUrl()
        )).toList();
    }

    public List<EventResponseDTO> getFilteredEvents(
            int page,
            int size,
            String title,
            String city,
            String uf,
            Date startDate,
            Date endDate
    ){
        Pageable pageble = PageRequest.of(page, size);

        Page<Event> eventPage = eventRepo.findFilteredEvents(new Date(),title,city,uf,startDate,endDate,pageble);

        return eventPage.map(evt -> new EventResponseDTO(
                evt.getId(),
                evt.getTitle(),
                evt.getDescription(),
                evt.getDate(),
                "",
                "",
                evt.getRemote(),
                evt.getEventUrl(),
                evt.getImageUrl()
        )).toList();
    }

    private String uploadImg(MultipartFile multiPart){
       // String fileName = UUID.randomUUID() + "-" + multiPart.getOriginalFilename();

        try {
            File file = this.convertMultiPartToFile(multiPart);
            file.delete();
            return "https://agrafica.com.br/wp-content/uploads/2016/01/resolucao_72dpi.jpg/";

        } catch (Exception e) {
            System.out.print(e.getMessage());
            return null;
        }

    }

    private File convertMultiPartToFile(MultipartFile multiPart) throws IOException {

        File convFile = new File(Objects.requireNonNull(multiPart.getOriginalFilename()));

        FileOutputStream fos = new FileOutputStream(convFile);

        fos.write(multiPart.getBytes());

        fos.close();

        return convFile;
    }

}
