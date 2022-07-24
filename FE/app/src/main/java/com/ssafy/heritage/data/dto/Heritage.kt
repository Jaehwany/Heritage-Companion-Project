package com.ssafy.heritage.data.dto

data class Heritage(
    val heritageSeq: Int,           // 문화유산 번호
    val heritageName: String,       // 문화유산 이름
    val heritageTime: String,       // 문화유산 시대
    val heritageAddress: String,    // 문화유산 주소
    val heritageCategory: String,   // 문화유산 카테고리
    val heritageLng: String,        // 문화유산 경도
    val heritageLat: String,        // 문화유산 위도
    val heritageImgUrl: String,     // 문화유산 이미지 링크
    val heritageMemo: String,       // 문화유산 설명
    val heritageVoice: String,      // 문화유산 나레이션
    val stampExist: Char,           // 문화유산 스탬프 유무(Y,N)
    val heritageClass: String,      // 문화유산 종목명
)