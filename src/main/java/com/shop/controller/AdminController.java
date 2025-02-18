package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.entity.Member;
import com.shop.service.AdminService;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final OrderService orderService;

    @GetMapping(value = {"", "/", "/dashboard"})
    public String adminDashboard(Model model) {
        List<OrderHistDto> recentOrders = orderService.getRecentOrders();
        model.addAttribute("recentOrders", recentOrders);
        return "admin/dashboard";
    }

    @GetMapping("/members")
    public String members(Model model) {
        model.addAttribute("members", adminService.getAllMembers());
        return "admin/members";
    }

    @PostMapping("/members/update")
    @ResponseBody
    public ResponseEntity<?> updateMember(@RequestBody Map<String, String> updateData) {
        try {
            Long memberId = Long.parseLong(updateData.get("id"));
            Member updatedMember = adminService.updateMember(memberId, updateData);
            return ResponseEntity.ok(updatedMember);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/members/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        try {
            adminService.deleteMember(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/members/export")
    public void exportToExcel(HttpServletResponse response) {
        // 엑셀 다운로드 로직 구현
    }

    @GetMapping("/members/email/{email}")
    @ResponseBody
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getMemberByEmail(email));
    }
}