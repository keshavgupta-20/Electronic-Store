package com.lcwd.electronic.store.Helpers;

import com.lcwd.electronic.store.dtos.PegeableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class helper {
    public static<U,V> PegeableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
        List<U> entity = page.getContent();
        List<V> dtoList  = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());
        PegeableResponse<V> response =  new PegeableResponse<>();
        response.setContent(dtoList);
        response.setPagesize(page.getSize());
        response.setLastPage(page.isLast());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setPageNumber(page.getNumber());
        return response;
    }
}
