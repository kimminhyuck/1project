package com.shop.controller;

import com.shop.constant.ReservationStatus;
import com.shop.dto.ReservationRequestDto;
import com.shop.entity.Reservation;
import com.shop.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public String adminPage(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "admin/reservation";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public ResponseEntity<?> getReservationList() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        reservationService.createReservation(reservationRequestDto);
        return ResponseEntity.ok("예약이 성공적으로 생성되었습니다.");
    }

    @PutMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestBody ReservationStatus newStatus) {
        try {
            reservationService.updateStatus(id, newStatus);
            return ResponseEntity.ok("상태가 " + newStatus.getDisplayName() + "으로 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("잘못된 요청: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("상태 변경 불가: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류 발생: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok("예약이 삭제되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("삭제 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 오류 발생: " + e.getMessage());
        }
    }
}
