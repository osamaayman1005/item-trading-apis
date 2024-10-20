package com.baddel.baddelBackend.item.service;

import com.baddel.baddelBackend.exception.ApiException;
import com.baddel.baddelBackend.global.DummyItemLinks;
import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.item.enums.ItemStatus;
import com.baddel.baddelBackend.item.models.Item;
import com.baddel.baddelBackend.item.models.ItemMedia;
import com.baddel.baddelBackend.item.repositories.CategoryRepository;
import com.baddel.baddelBackend.item.repositories.ItemMediaRepository;
import com.baddel.baddelBackend.item.repositories.ItemRepository;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMediaRepository itemMediaRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserService userService, ItemMediaRepository itemMediaRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.itemMediaRepository = itemMediaRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    public Page<ItemDTO> getAllItems(List<Long> categoryIds, Pageable pageable) {
        Page<Item> items;
        if (categoryIds == null || categoryIds.isEmpty()){
            items = itemRepository.findAllByIsDeletedFalseAndStatus(ItemStatus.LISTED, pageable);
        }
        else {
            items = itemRepository.findAllByIsDeletedFalseAndStatusListedAndCategory( categoryIds, pageable);

        }
        return items.map(Item::toItemDTO);
    }
    public Page<ItemDTO> searchItems(String keyword, List<Long> categoryIds, Pageable pageable){
        Long[] categoryIdsArray = null;
        if(categoryIds!=null){
            categoryIdsArray = categoryIds.toArray(new Long[0]);
        }
        keyword = keyword.trim();
        Page<Item> items =
                itemRepository.searchItemsByNameAndDescriptionAndCategory(keyword, categoryIdsArray,
                        pageable);
        return items.map(Item::toItemDTO);
    }

    public ItemDTO getItemById(Long id) {
        Optional<Item> optionalItem = itemRepository.findByIdAndIsDeletedFalse(id);
        return optionalItem.map(Item::toItemDTO).orElse(null);
    }

    public ItemDTO saveItem(ItemDTO itemDTO, String username) {
        verifyItem(itemDTO);
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(itemDTO.getDescription());
        item.setCondition(itemDTO.getCondition());
        item.setStatus(ItemStatus.PENDING);
        if (itemDTO.getPriceVariation() != null) {
            item.setMaximumAmountToPay(itemDTO.getPriceVariation().getMaximumAmountToPay());
            item.setMaximumAmountToReceive(itemDTO.getPriceVariation().getMaximumAmountToReceive());
        }
        item.setLister(userService.getUserByUsername(username));
        item.setCategory(categoryRepository.findById(itemDTO.getCategoryId()).orElseThrow(
                () -> new ApiException("wrong category", HttpStatus.BAD_REQUEST)
        ));
        item.setIsDeleted(false);

        // Save the Item to get its ID

        Item savedItem = itemRepository.save(item);

        // Map media links to Media entities and associate them with the item
        if (itemDTO.getMedia() != null && !itemDTO.getMedia().isEmpty()) {
            List<ItemMedia> itemMediaList = new ArrayList<>();
            for (ItemMedia itemMedia : itemDTO.getMedia()) {
                itemMedia.setItem(savedItem);
                itemMedia.setBase64("base64 should not be here :(");
                itemMedia.setLink(DummyItemLinks.getRandomMedia());
                ItemMedia savedItemMedia = itemMediaRepository.save(itemMedia);
                itemMediaList.add(savedItemMedia);
            }
            savedItem.setItemMedia(itemMediaList);
        }

        // Save the updated Item with associated ItemMedia entities
        Item updatedItem = itemRepository.save(savedItem);
        return updatedItem.toItemDTO();
    }

    public ItemDTO updateItem(ItemDTO itemDTO, String username) {
        ApplicationUser applicationUser = userService.getUserByUsername(username);
        Optional<Item> optionalItem = itemRepository.findByIdAndIsDeletedFalse(itemDTO.getId());
        if (optionalItem.isEmpty()) {
            throw new ApiException("Item was not found", HttpStatus.NOT_FOUND);
        }
        if (!applicationUser.getId().equals(optionalItem.get().getLister().getId())) {
            throw new ApiException("You don't have access to edit this item",
                    HttpStatus.valueOf(401));
        }
        verifyItem(itemDTO);
        Item item = optionalItem.get();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setPrice(itemDTO.getPrice());
        item.setDescription(itemDTO.getDescription());
        item.setCondition(itemDTO.getCondition());
        item.setStatus(itemDTO.getStatus());
        item.setMaximumAmountToPay(itemDTO.getPriceVariation().getMaximumAmountToPay());
        item.setMaximumAmountToReceive(itemDTO.getPriceVariation().getMaximumAmountToReceive());
        item.setCategory(categoryRepository.findById(itemDTO.getCategoryId()).orElseThrow(
                () -> new ApiException("wrong category", HttpStatus.BAD_REQUEST)
        ));
        // Save the Item to get its ID
        Item savedItem = itemRepository.save(item);

        // Map media links to Media entities and associate them with the item
        if (itemDTO.getMedia() != null && !itemDTO.getMedia().isEmpty()) {
            List<ItemMedia> itemMediaList = new ArrayList<>();
            for (ItemMedia itemMedia : itemDTO.getMedia()) {
                itemMedia.setItem(savedItem);
                itemMedia.setBase64("base64 should not be here :(");
                itemMedia.setLink(DummyItemLinks.getRandomMedia());
                ItemMedia savedItemMedia = itemMediaRepository.save(itemMedia);
                itemMediaList.add(savedItemMedia);
            }
            savedItem.setItemMedia(itemMediaList);
        }

        // Save the updated Item with associated ItemMedia entities
        Item updatedItem = itemRepository.save(savedItem);
        return updatedItem.toItemDTO();
    }

    public void deleteItem(Long id, String username) {
        ApplicationUser applicationUser = userService.getUserByUsername(username);
        Optional<Item> optionalItem = itemRepository.findByIdAndIsDeletedFalse(id);
        if (optionalItem.isEmpty()) {
            throw new ApiException("Item was not found", HttpStatus.NOT_FOUND);
        }
        if (!applicationUser.getId().equals(optionalItem.get().getLister().getId())) {
            throw new ApiException("You don't have access to delete this item",
                    HttpStatus.valueOf(401));
        }
        optionalItem.ifPresent(item -> {
            item.setIsDeleted(true);
            itemRepository.save(item);
        });
    }

    public Page<ItemDTO> getUserItems(String username, Pageable pageable) {
        return getListerItems(
                userService.getUserByUsername(username).getId(), pageable
        );
    }

    public Page<ItemDTO> getListerItems(Long listerId,  Pageable pageable) {
        Page<Item> items = itemRepository.findByListerIdAndIsDeletedFalseAndStatus(listerId,
                ItemStatus.LISTED, pageable);
        return items.map(Item::toItemDTO);
    }
    public ItemDTO updateItemStatus(Long itemId, ItemStatus itemStatus){
        Item item = itemRepository.findByIdAndIsDeletedFalse(itemId)
                .orElseThrow( ()-> new ApiException("Item " +
                        itemId + " was not found or deleted", HttpStatus.NOT_FOUND));
        item.setStatus(itemStatus);
        return itemRepository.save(item).toItemDTO();
    }

    private void verifyItem(ItemDTO item) {
        if (item.getPrice() < 0)
            throw new ApiException("price is not correct",
                    HttpStatus.BAD_REQUEST);
        if (item.getPriceVariation() != null) {
            if (item.getPriceVariation().getMaximumAmountToReceive() != null &&
                    item.getPriceVariation().getMaximumAmountToReceive() < 0) {
                throw new ApiException("maximum amount to receive is not correct",
                        HttpStatus.BAD_REQUEST);
            }
            if (item.getPriceVariation().getMaximumAmountToPay() != null &&
                    item.getPriceVariation().getMaximumAmountToPay() < 0) {
                throw new ApiException("maximum amount to pay is not correct",
                        HttpStatus.BAD_REQUEST);
            }

        }
    }

}
