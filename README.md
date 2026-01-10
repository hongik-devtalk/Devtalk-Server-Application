# 🗣️ Devtalk Backend
https://hongikdevtalk.com/
> 홍익대학교 세미나 플랫폼 ‘Devtalk’의 백엔드 서버입니다.  
> 세미나 신청부터 라이브 참여, 후기 작성, 관리자용 세미나 관리까지  
> 전 과정을 통합 관리하는 **Spring Boot 기반 REST API 서버**입니다.

---
## 🚀 Tech Stack

| Category | Stack                             |
|-----------|-----------------------------------|
| **Language** | Java 17                           |
| **Framework** | Spring Boot 3.x                   |
| **ORM / DB** | Spring Data JPA, MySQL            |
| **Auth / Security** | Spring Security, JWT              |
| **Infra / Storage** | AWS S3, EC2/ECS, RDS              |
| **Mail Service** | JavaMailSender, MimeMessageHelper |
| **Build / Deploy** | Gradle, Docker, GitHub Actions    |
| **Etc** | Lombok, Validation, Scheduler     |

## 🧱 Architecture

```text
Client (React)
      ↓
Nginx (HTTPS)
      ↓
Spring Boot (API Server)
      ↓
MySQL / S3 / Mail Service
```


## ✨ 주요 기능 요약

| 구분 | 주요 기능 |
|------|------------|
| **인증/권한** | 관리자 로그인, 토큰 재발급, 로그아웃 |
| **세미나 관리 (Admin)** | 등록 / 수정 / 삭제 / 후기 관리 / 출석 체크 / 회차 관리 |
| **세미나 참여 (User)** | 세미나 리스트 / 상세 조회 / 신청 / 리뷰 작성 |
| **홈 화면 관리** | 노출 세미나 설정, 후기 카드 노출, 홍보 이미지 및 FAQ/문의 링크 관리 |
| **이메일 발송** | 신청 확인 메일, 리마인드 메일 (스케줄러 기반) |

---

## 🔍 상세 기능 명세

### 🧩 관리자 관련 기능
- **로그인** → `POST /admin/login`
- **토큰 재발급** → `POST /admin/refresh`
- **로그아웃** → `POST /admin/logout`
- **아이디 관리** → 조회 / 추가 / 삭제 (`GET/POST/DELETE /admin/authority/loginIds`)

---

### 🎓 세미나 관리 (Admin)
- **세미나 등록/수정/삭제** → CRUD + S3 파일 업로드
- **세미나 상세 조회** → `GET /admin/seminars/{seminarId}`
- **후기 관리 (ON/OFF/삭제)** → `/admin/seminars/reviews/...`
- **출석체크** → `POST /admin/seminars/{seminarId}/applicants/{studentId}`
- **세미나 회차 리스트 조회** → `GET /admin/seminars/nums`

---

### 👤 사용자 기능 (User)
- **세미나 리스트 조회** → `GET /user/seminarList/`
- **세미나 상세 정보**
    - 본문: `/user/seminars/{seminarId}`
    - 세션: `/user/seminars/{seminarId}/session`
    - 후기: `/user/seminars/{seminarId}/review`
- **세미나 신청** → `POST /user/seminars`
- **라이브 입장 및 출석체크** → `POST /user/live/attend`
- **신청 인증** → `POST /user/live/auth`
- **리뷰 작성** → `POST /user/live/review`

---

### 🏠 홈 화면 및 관리 기능
- **홈 노출 세미나 설정/조회** → `/admin/show-seminar`, `/show-seminar`
- **홍보 이미지 관리** → `/admin/home/images`
- **FAQ / 문의 링크 관리** → `/admin/home/faq-link`, `/admin/home/inquiry-link`
- **후기 카드 관리** → `/admin/home/reviews`

---

### ✉️ 이메일 / 알림
- **신청 확인 메일 발송** → `POST /user/seminars`
- **리마인드 메일 발송** → 스케줄러 자동 실행

---

## 📂 Directory Structure

```text
Devtalk-Server-Application/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   └── workflows/
├── build/
├── gradle/
├── nginx/
├── src/
│   └── main/
│       └── java/
│           └── com/hongik/devtalk/
│               ├── controller/
│               ├── domain/
│               ├── global/
│               ├── repository/
│               ├── scheduler/
│               ├── service/
│               ├── util/
│               └── DevtalkApplication.java
├── build.gradle
├── settings.gradle
└── README.md
```

## 🚢 CI/CD

> GitHub Actions → ECR 푸시 → EC2 배포(Compose)까지 원클릭.  
> main 브랜치에 push되면 자동으로 빌드/배포한다.

### 파이프라인 개요
```text
Developer (push main)
        └── GitHub Actions
             ├─ Gradle build (prod 프로파일)
             ├─ ECR 로그인
             ├─ Spring 이미지 빌드/푸시
             ├─ Nginx 이미지 빌드/푸시
             ├─ docker-compose-prod.yml 서버로 전송
             └─ EC2 SSH 접속 → docker login / pull / up -d
```

## 🧾 API 인증
- JWT 기반 인증 
- 관리자 전용 API는 ROLE_ADMIN 권한 필요 
- 토큰 만료 시 /admin/refresh 로 재발급 가능

## 🧑‍💻 Contributors

| 이름 | 역할 | 담당 기능 |
|------|------|------------|
| [@yujeong430](https://github.com/yujeong430) | 백엔드 | 어드민 세미나 관리 전반 (등록, 수정, 삭제, 후기/출석/질문 관리) |
| [@nsh0919](https://github.com/nsh0919) | 백엔드 | 관리자 인증 및 권한 관리 (로그인, 토큰, 아이디 관리), 출석체크 로직 구현 |
| [@Mymyseoyoung](https://github.com/Mymyseoyoung) | 백엔드 | 유저 세미나 상세 조회 (세미나, 세션, 후기) 및 신청 기능 |
| [@shinae1023](https://github.com/shinae1023) | 백엔드 | 세미나 신청 인증, 라이브 입장/출석체크, 리뷰 작성 기능 |
| [@LABYRINTH3](https://github.com/LABYRINTH3) | 백엔드 | 홈 화면 관리 기능 (홍보 이미지, FAQ/문의 링크, 후기 카드) |
| [@soyeoneeii](https://github.com/soyeoneeii) | 백엔드 | 유저 홈화면 세미나 조회, 어드민 노출 세미나 설정, 이메일 발송 로직, 서버 배포 자동화 및 CI/CD 구축 |
| [@weeeeestern](https://github.com/weeeeestern) | 백엔드 | 이메일 발송 로직, 서버 배포 자동화 및 CI/CD 구축, SSM+KMS 기반 런타임 시크릿 주입 |

---
