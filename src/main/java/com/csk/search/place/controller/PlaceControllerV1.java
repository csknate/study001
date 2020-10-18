package com.csk.search.place.controller;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csk.search.common.domain.Response;
import com.csk.search.common.exception.CommonException;
import com.csk.search.place.service.PlaceService;

@RestController
@RequestMapping("/v1/place")
@Validated
public class PlaceControllerV1 {

	@Autowired
    private PlaceService placeService;

    @GetMapping(value="")
    public Response place(@RequestParam(required = true) @Size(max=50,min=2) String keyword,
    					@RequestParam(required = true) @Max(45) @Min(1) Integer pageNo) throws CommonException {
        return placeService.getPlace(keyword, pageNo);
    }
    
    @GetMapping(value="/rank")
    public Response rank() throws CommonException {
        return placeService.getKeywordRank10();
    }
}
