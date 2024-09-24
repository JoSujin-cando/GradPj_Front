package com.example.gradfront.data

data class KakaoPayCancelResponse(
    val aid : String, // 요청 고유 번호
    val tid : String, // 결제 고유 번호
    val cid : String, // 가맹점 코드
    val status : String, // 결제 상태
    val partner_order_id : String, // 가맹점 주문 번호
    val partner_user_id : String, // 가맹점 회원 ID
    val payment_method_type : String, // 결제 수단
    val amount : Amount, // 결제 금액 정보, 결제 요청 구현할때 이미 구현해놓음
    val approved_cancel_amount : ApprovedCancelAmount, // 이번 요청으로 취소된 금액
    val canceled_amount : CanceledAmount, // 누계 취소 금액
    val cancel_available_amount : CancelAvailableAmount, // 남은 취소 금액
    val item_name : String, // 상품 이름
    val item_code : String, // 상품 코드
    val quantity : Int, // 상품 수량
    val created_at : String, // 결제 준비 요청 시각
    val approved_at : String, // 결제 승인 시각
    val canceled_at : String, // 결제 취소 시각
    val payload : String // 취소 요청 시 전달한 값
)
