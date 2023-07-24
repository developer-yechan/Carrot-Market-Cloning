package clone.carrotMarket.api.controller;
import clone.carrotMarket.domain.Member;
import clone.carrotMarket.dto.ChatRoomDTO;
import clone.carrotMarket.dto.EnterRoomDto;
import clone.carrotMarket.dto.ErrorDTO;
import clone.carrotMarket.dto.SuccessDTO;
import clone.carrotMarket.service.ChatRoomService;
import clone.carrotMarket.web.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Tag(name = "Chatting Room", description = "채팅방 생성, 조회, 입장 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/chat")
@Slf4j
public class ChatRoomAPIController {
    private final ChatRoomService chatRoomService;

    @Operation(summary = "나의 채팅방 목록 조회", description = "나의 채팅방 목록 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "나의 채팅방 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @GetMapping("/rooms")
    public List<ChatRoomDTO> findMyRooms(@Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        log.info("# All Chat Rooms");
        Member loginMember = principal.getMember();
        List<ChatRoomDTO> allRooms = chatRoomService.findAllRooms(loginMember.getId());
        return allRooms;
    }

    @Operation(summary = "상품 관련 채팅방 목록 조회", description = "상품 관련 채팅방 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 관련 채팅방 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매 글 id",required = true)
    })
    @GetMapping("/rooms/{sellId}")
    public List<ChatRoomDTO> findRoomsOfSell(@PathVariable Long sellId,  @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        List<ChatRoomDTO> roomsOfSell = chatRoomService.findRoomsOfSell(sellId);

        return roomsOfSell;
    }
    @Operation(summary = "채팅방 개설", description = "채팅방을 개설한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "채팅방 개설 성공", content = @Content(schema = @Schema(implementation = SuccessDTO.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="sellId",description = "판매 글 id",required = true)
    })
    @PostMapping(value = "/rooms/{sellId}")
    public SuccessDTO create(@PathVariable Long sellId, @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal){
        Member loginMember = principal.getMember();
        Long senderId = loginMember.getId();
        log.info("# Create Chat Room , senderId: " + senderId);
        Long chatRoomId = chatRoomService.findRoomId(sellId, loginMember.getId());
        if(chatRoomId != null){
            return new SuccessDTO(302,"http://localhost:8080/api/chat/rooms/"+chatRoomId);
        }
        Long roomId = chatRoomService.createRoom(sellId, senderId);
        return new SuccessDTO(201,"http://localhost:8080/api/chat/rooms/"+roomId) ;
    }
    @Operation(summary = "채팅방 입장", description = "채팅방 입장한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 입장 성공", content = @Content(schema = @Schema(implementation = EnterRoomDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @Parameters({
            @Parameter(name="roomId",description = "채팅방 id",required = true),
    })
    @GetMapping("/rooms/enter/{roomId}")
    public EnterRoomDto enterRoom(@PathVariable Long roomId ,  @Parameter(hidden = true) @AuthenticationPrincipal PrincipalDetails principal,
                          Model model, HttpServletResponse response) throws IOException {

        log.info("# Enter Chat Room, roomID : " + roomId);
        ChatRoomDTO chatRoomDTO = chatRoomService.findRoom(roomId);
        Member loginMember = principal.getMember();
        if(loginMember.getId() != chatRoomDTO.getSellerId() && loginMember.getId() != chatRoomDTO.getSenderId()){
            response.sendError(403, "잘못된 요청입니다.");
        }

        return new EnterRoomDto(chatRoomDTO,loginMember.getId(),loginMember.getNickname());
    }
}
