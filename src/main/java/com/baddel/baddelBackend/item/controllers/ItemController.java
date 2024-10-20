package com.baddel.baddelBackend.item.controllers;

import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.item.service.ItemService;
import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class ItemController {

    private final ItemService itemService;
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/api/v1/public/items")
    public ResponseDTO getAllItems(@RequestParam(required = false) String searchTerm,
                                   @RequestParam(required = false) List<Long> categoryIds,
                                   @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                   @RequestParam(required = false, defaultValue = "20") int pageSize) {
        ResponseDTO responseDTO = new ResponseDTO();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        responseDTO.setMessage("Items retrieved successfully");
        if (searchTerm == null || searchTerm.isEmpty() || searchTerm.isBlank()){
            responseDTO.setData(itemService.getAllItems(categoryIds, pageable));
        }else {
            responseDTO.setData(itemService.searchItems(searchTerm, categoryIds, pageable));
        }
        return responseDTO;
    }
    @GetMapping("/api/v1/user/items")
    public ResponseDTO getUserItems(Authentication authentication,
                                    @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                    @RequestParam(required = false, defaultValue = "20") int pageSize) {
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Items retrieved successfully");
        responseDTO.setData(itemService.getUserItems(username, pageable));
        return responseDTO;
    }
    @GetMapping("/api/v1/public/users/{listerId}/items")
    public ResponseDTO getItemsByListerId(@PathVariable Long listerId,
                                          @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                          @RequestParam(required = false, defaultValue = "20") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Items retrieved successfully");
        responseDTO.setData(itemService.getListerItems(listerId, pageable));
        return responseDTO;
    }

    @GetMapping("/api/v1/public/items/{id}")
    public ResponseDTO getItemById(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Items retrieved successfully");
        responseDTO.setData(itemService.getItemById(id));
        return responseDTO;
    }
    @PostMapping("/api/v1/user/items")
    public ResponseDTO saveItem(@RequestBody ItemDTO item, Authentication authentication) {
        System.out.println(item);
        ResponseDTO responseDTO = new ResponseDTO();
        String username = authentication.getName();
        responseDTO.setMessage("Items created successfully");
        responseDTO.setData(itemService.saveItem(item, username));

        return responseDTO;
    }
    @PutMapping("/api/v1/user/items")
    public ResponseDTO updateItem(
            @RequestBody ItemDTO updatedItemDTO,
            Authentication authentication) {

        ResponseDTO responseDTO = new ResponseDTO();

            String username = authentication.getName();

            ItemDTO updatedItem = itemService.updateItem(updatedItemDTO, username);

            responseDTO.setMessage("Item updated successfully");
            responseDTO.setData(updatedItem);


        return responseDTO;
    }
    @DeleteMapping("/api/v1/user/items/{id}")
    public ResponseDTO deleteItem(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        itemService.deleteItem(id, username);
        return new ResponseDTO("Item deleted successfully", null);
    }
}
