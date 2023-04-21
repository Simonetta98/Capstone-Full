package com.Capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Capstone.exception.NotFoundEx;
import com.Capstone.exception.NotYetImplementedEx;
import com.Capstone.payload.PatchDto;
import com.Capstone.service.CommentService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/comment")
public class CommentController {
	
	@Autowired CommentService service;

	  //PATCH
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> updatePartially(@PathVariable(name = "id") long id,
        @RequestBody PatchDto dto) throws NotYetImplementedEx, NotFoundEx {
      // skipping validations 
      if (dto.getOp().equalsIgnoreCase("update")) {
        boolean result = service.partialUpdate(id, dto.getKey(), dto.getValue());
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
      } else {
        throw new NotYetImplementedEx("NOT_YET_IMPLEMENTED");
      }
    }

}
