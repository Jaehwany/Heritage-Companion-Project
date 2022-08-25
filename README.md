# <img src="https://user-images.githubusercontent.com/52437364/185876019-f544dd02-ab2e-4f10-af4e-43a5d970e4cd.png" align="left" width="40" height="40"> 좋구만유 - 문화유산 동행 APP
> **Cultural Heritage Companion Application, Joekoomanyoo**


<img src="https://user-images.githubusercontent.com/97875998/186781871-0b0237d8-7153-4dda-be06-d6125e05a6cc.png" align="left" width="90%" height="50%">

<br><br><br><br><br><br><br><br><br><br><br><br>


## 🕘 **프로젝트 진행 기간**

**2022.07.07(목) ~ 2022.08.19(금)**

<br>


## 🧛 **팀원**
<table>
 <tr>
    <td align="center"><a href="https://github.com/dttmm"><img src="https://avatars.githubusercontent.com/dttmm" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/mxxxxxji"><img src="https://avatars.githubusercontent.com/mxxxxxji" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/kettle4ot"><img src="https://avatars.githubusercontent.com/kettle4ot" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/us13579"><img src="https://avatars.githubusercontent.com/us13579" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/Jaehwany"><img src="https://avatars.githubusercontent.com/Jaehwany" width="80px;" alt=""></td>
  </tr>
  <tr>
    <td align="center">팀장, AOS</td>
    <td align="center">AOS</td>
    <td align="center">AOS, UI 설계</td>
    <td align="center">Backend</td>
    <td align="center">Backend, Infra</td>
  </tr>
     <tr>
    <td align="center"><a href="https://github.com/mxxxxxji"><sub><b>김명지</b></td>
    <td align="center"><a href="https://github.com/dttmm"><sub><b>강태웅</b></td>
    <td align="center"><a href="https://github.com/kettle4ot"><sub>김수</b></td>
    <td align="center"><a href="https://github.com/us13579"><sub><b>김지수</b></td>
    <td align="center"><a href="https://github.com/Jaehwany"><sub><b>이재환</b></td>
  </tr>
     
</table>
     
<br>     
     



## 🏴 **Introduce**

**좋구만유**는 사용자의 실시간 위치를 기반으로 문화 유산 정보 및 동행 서비스를 제공하는 프로젝트 입니다.

<br>

 ## 🏴 주요 기능
 <br>
 
- **문화 유산**

   - 문화 유산에 대한 상세 정보를 볼 수 있습니다

   - 지도에서 문화 유산 위치를 확인할 수 있습니다

   - 마음에 드는 문화 유산을 스크랩할 수 있습니다

   - 문화 유산을 모임에 공유할 수 있습니다

   - 문화 유산에 대한 리뷰를 작성할 수 있습니다

<br>

- **모임**

   - 문화유산 동행을 위한 모임 개설 or 가입이 가능합니다

   - 모임원들과 실시간 채팅을 통해 모임 계획을 세울 수 있습니다

   - 모임 일정을 등록하고, 모임 목적지를 설정할 수 있습니다

   - 모임 후 모임원 간 상호 평가를 통해 모임원을 칭찬할 수 있습니다

<br>

- **사진 피드**

   - 사진 피드를 등록하여 자신이 찍은 문화 유산 사진을 다른 사용자와 공유할 수 있습니다

   - 좋아요 기능을 통해 사진에 대한 호감을 표할 수 있습니다

<br>

- **AR 방문 인증**

   - AR 기능을 이용하여 등록된 문화 유산 스탬프를 수집할 수 있습니다

   - 도감을 통해 문화유산 카테고리별로 스탬프 수집 현황을 볼 수 있습니다

   - 순위 기능을 통해 스탬프 수집에 대한 성취감을 얻을 수 있습니다

</br></br>

> ## 🔨 아키텍처

<img src="https://user-images.githubusercontent.com/52437364/186411816-4e96bbe4-952b-40d9-a7e2-b90caadec223.png" align="left">
<br><br>

> ## ERD

<img src="https://user-images.githubusercontent.com/97875998/186412162-df1c7e85-c347-4726-bded-a8fbf2ab1bce.png" align="left"><br><br>

<br><br><br><br>
<br><br>
<br><br>
<br><br>
<br><br>


<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
> ## 개발 환경

- BackEnd

   - Spring Boot 2.6.3
   - Spring Data JPA
   - Spring Security
   - Spring Validation
   - Java 11
   - QueryDSL 5.0
   - Lombok 1.18.24
   - Firebase 8.x
   - Gradle 7.4.x
   - Swagger 3.x

- Android

   - Kotlin 1.7.0
   - Room 2.4.2
   - Retrofit 2.9.0
   - Glide 4.13.2

- DB

   - MySQL 8.0.29 

- CI/CD

   - AWS EC2
   - Docker
   - Jenkins

- IDE
   - IntelliJ
   - Spring Tool Suite 3
   - MySQL Workbench 8.0
   - Android Studio

<br>

> ## 협업 환경

- GitLab

  - 코드의 버전을 관리
  - 이슈 생성, 관리, 해결을 위한 소통
  - branch를 통한 세부 기능 구현 후 MR을 통해 코드 리뷰 후 피드백

- JIRA

  - 전체 프로젝트 일정 관리
  - 일정 기간동안 목표량을 설정하여 Sprint 진행 ( 1주일 )
  - Task 마다 Story Point를 설정하여 진행

- MatterMost & Webex & Discord

  - 소통
  - 회의

- Notion

  - 매일 오전 회의 / 오후 회의를 기록
  - 회의록에 어제 한 일 , 오늘 할 일, 우려사항 등을 기입하여 회의 시간 단축 & 목표 설정
  - 오류나 강의, 알게된 것들을 알기 쉽게 공유하며 팀원들 간의 자료 공유
  - 각종 컨벤션 정리 ( 팀, 코드, 깃 )
  - 일정 관리 ( 간트 차트 )
  - 프로젝트 과정 중 팀원들 간의 공유가 필요한 문서 관리
    - 와이어프레임 & 스토리보드
    - 요구사항 명세서
    - 기획서
    - 디자인
    - PPT
    - ..

- Tool
  - Figma
  - Adobe XD
  - ERD Cloud
  
<br>

> ## 💁 팀원
 <table>
  <tr>
    <td align="center"><a href="https://github.com/dttmm"><img src="https://avatars.githubusercontent.com/dttmm" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/mxxxxxji"><img src="https://avatars.githubusercontent.com/mxxxxxji" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/kettle4ot"><img src="https://avatars.githubusercontent.com/kettle4ot" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/us13579"><img src="https://avatars.githubusercontent.com/us13579" width="80px;" alt=""></td>
    <td align="center"><a href="https://github.com/Jaehwany"><img src="https://avatars.githubusercontent.com/Jaehwany" width="80px;" alt=""></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/dttmm"><sub><b>강태웅</b></td>
    <td align="center"><a href="https://github.com/mxxxxxji"><sub><b>김명지</b></td>
    <td align="center"><a href="https://github.com/kettle4ot"><sub><b>김수빈</b></td>
    <td align="center"><a href="https://github.com/us13579"><sub><b>김지수</b></td>
    <td align="center"><a href="https://github.com/Jaehwany"><sub><b>이재환</b></td>
  </tr>
</table>
</br></br>
      
> ## 서비스화면
간단한 글과 움짤
</br></br>

<div>
  <img src="https://user-images.githubusercontent.com/66373647/186418969-96825ca7-25be-449e-bee1-e360bb392e58.gif" height="480px;" alt="">
  <img src="https://user-images.githubusercontent.com/66373647/186419085-b95ec474-1dcb-4b14-99ba-a65007d15543.gif" height="480px;" alt="">
  <img src="https://user-images.githubusercontent.com/66373647/186419365-9c77b5c7-18cd-4c03-926b-e0e0e0ca6104.gif" height="480px;" alt="">
  <img src="https://user-images.githubusercontent.com/66373647/186419826-f30e6189-91d1-4de1-b2d0-ad8f001f919e.gif" height="480px;" alt="">
</div>

<div>
  <img src="https://user-images.githubusercontent.com/66373647/186419176-2f866652-61aa-401b-ad57-194128a1ce1b.gif" height="480px;" alt="">
  <img src="https://user-images.githubusercontent.com/66373647/186419431-e6893d09-e6ef-4af3-82df-17c8e0cda515.gif" height="480px;" alt="">
</div>
  
<div>
  <img src="https://user-images.githubusercontent.com/66373647/186419212-b93f76d8-1509-4932-80c7-9d9f3afa4158.gif" height="480px;" alt="">
  <img src="https://user-images.githubusercontent.com/66373647/186419248-f0f013b0-5da6-4ba0-96bc-95bbc3aa4d44.gif" height="480px;" alt="">
</div>

<div>
   <img src="https://user-images.githubusercontent.com/66373647/186428945-89fce42b-b23e-4bdd-aeb1-bd26a4ca94fc.gif" height="480px;" alt="">
   <img src="https://user-images.githubusercontent.com/66373647/186429270-b222c4d2-eb58-48c0-8a9f-aefd92697348.gif" height="480px;" alt="">
</div>
