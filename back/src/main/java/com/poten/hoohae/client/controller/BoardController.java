package com.poten.hoohae.client.controller;

import com.poten.hoohae.client.common.Paging;
import com.poten.hoohae.client.domain.Board;
import com.poten.hoohae.client.dto.req.BoardRequestDto;
import com.poten.hoohae.client.dto.res.BoardResponseDto;
import com.poten.hoohae.client.dto.PagingDto;
import com.poten.hoohae.client.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

//    @GetMapping("/list")
//    public ResponseEntity<PagingDto> getBoardList(@RequestParam(value = "page", defaultValue = "1") int page
//            , @RequestParam(value = "age", required = false) Long age
//    , @RequestParam(value = "category", required = false) String category
//    , @RequestParam(value = "sort", required = false) String sort
//    , Authentication authentication) {
//        log.info("/api/board/list");
//
//        long totalItemCnt = boardService.totalBoardCnt(age, sort);
//        PagingDto pagingDto = PagingDto.builder()
//                .hasPage(Paging.hasPage(page, totalItemCnt))
//                .data(boardService.getBoardList(page, age, category, sort, authentication.getName()))
//                .build();
//
//        return ResponseEntity.ok(pagingDto);
//    }
    @GetMapping("/list")
    public ResponseEntity<PagingDto> getSearchList(@RequestParam(value = "page", defaultValue = "1") int page
        , @RequestParam(value = "age", required = false) Long age
            , @RequestParam(value = "category", required = false) String category
            , @RequestParam(value = "sort", required = false) String sort
            , @RequestParam(value = "query", required = false) String query
            , Authentication authentication) {

        long totalItemCnt = boardService.getSearchListCount(age, category, sort, query);
        PagingDto pagingDto = PagingDto.builder()
                .hasPage(Paging.hasPage(page, totalItemCnt))
                .totalCnt(totalItemCnt)
                .data(boardService.getSearchList(page, age, category, sort, query, authentication.getName()))
                .build();

        return ResponseEntity.ok(pagingDto);
    }

    @GetMapping("/top5")
    public ResponseEntity<List<BoardResponseDto>> getTop5List(@RequestParam(value = "age", defaultValue = "0") Long age, Authentication authentication) {

        return ResponseEntity.ok(boardService.getTop5Boards(age, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardId(@PathVariable(name = "id") Long id, Authentication authentication) {
        BoardResponseDto dto = boardService.getBoard(id, authentication.getName());

        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public ResponseEntity<Long> saveBoard(@ModelAttribute BoardRequestDto dto, Authentication authentication) throws IOException {
        try {
            Long savedBoardId;
            if(dto.getId() == null) {
                savedBoardId = boardService.saveBoard(dto, authentication.getName());
            } else {
                savedBoardId = boardService.updateBoard(dto, authentication.getName());
            }
            return ResponseEntity.ok(savedBoardId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Long> updateBoard(@PathVariable(name = "id") long id, @RequestBody BoardRequestDto dto, Authentication authentication) throws IOException {
        String email = authentication.getName();
        Long updateId = boardService.updateBoard(id, dto, email);
        return ResponseEntity.ok(updateId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteBoard(@PathVariable(name = "id") long id, Authentication authentication) {
        String email = authentication.getName();
        Long deleteId = boardService.deleteBoard(id, email);
        return ResponseEntity.ok(deleteId);
    }
}