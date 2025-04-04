# 일정 관리 API

<br>

## 개발 기간
2025-03-30 ~ 2025-04-03

## 기술 스택
- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- Querydsl
- MySQL 8.0
- Bcrypt

## 주요 기능
- 회원가입, 로그인, 로그아웃, 내 정보 수정, 회원 탈퇴
- 일정 등록 / 수정 / 삭제 / 조건별 목록 조회 (페이징 포함)
- 댓글 등록 / 수정 / 삭제 / 목록 조회 (페이징 포함)

<br>

## 폴더 구조

- task/
    - schedule/
        - common/
        - config/
        - controller/
        - dto/
        - entity/
        - exception/
        - filter/
        - repository/
        - service/

<br>

## ERD
![erd.png](erd.png)

<br>

## 기능 명세서

### 1. 회원 기능

#### 1.1. 회원가입
- 이름, 이메일, 비밀번호를 입력해 새로운 사용자를 등록할 수 있다.
- 비밀번호는 암호화되어 저장된다.
- `createdAt`, `updatedAt`은 자동 기록된다.
- ID는 자동 생성된다.
- **입력값:**
    - `name`: 문자열, 필수
    - `email`: 이메일 형식, 필수, 중복 불가
    - `password`: 8자 이상, 영문/숫자/특수문자 포함

#### 1.2. 로그인
- 이메일과 비밀번호를 통해 로그인을 수행하며, 로그인 성공 시 세션이 생성된다.
- 로그인 실패 시 401 상태코드와 에러 메시지가 반환된다.
- 기존 세션이 있다면 초기화 후 새로 로그인한다.
- **입력값:**
    - `email`: 이메일 형식, 필수
    - `password`: 필수

#### 1.3. 내 정보 조회
- 현재 로그인한 사용자의 정보를 조회할 수 있다.
- 세션 기반 인증 필요

#### 1.4. 이름 변경
- 로그인한 사용자는 본인의 이름을 수정할 수 있다.
- **입력값:**
    - `name`: 필수, 공백 불가, 10자 이하 제한

#### 1.5. 비밀번호 변경
- 로그인한 사용자는 본인의 비밀번호를 변경할 수 있다.
- 변경 전 기존 비밀번호 검증 절차를 진행한다.
- 비밀번호는 암호화되어 저장된다.
- **입력값:**
    - `oldPassword`: 기존 비밀번호
    - `newPassword`: 새 비밀번호 (8자 이상, 영문/숫자/특수문자 포함)

#### 1.6. 로그아웃
- 현재 로그인한 사용자의 세션을 종료하고 로그아웃한다.

#### 1.7. 회원 탈퇴
- 로그인된 사용자가 본인의 계정을 삭제할 수 있다.
- 삭제 완료 후 자동 로그아웃 처리됨

<br>

### 2. 일정 기능

#### 2.1. 일정 등록
- 로그인된 사용자가 새로운 일정을 등록할 수 있다.
- ID는 자동 생성된다.
- `createdAt`, `updatedAt`은 자동 기록된다.
- **입력값:**
    - `date`: `yyyy-MM-dd` 형식
    - `title`: 50자 이하
    - `content`: 200자 이하

#### 2.2. 일정 목록 조회
- 특정 사용자의 일정을 조건에 따라 페이지 단위로 조회할 수 있다.
- 일정별 댓글 개수 포함 응답
- `updatedAt` 기준 내림차순 정렬
- **조회 조건:**
    - 필수: `userId`
    - 선택: `year`, `month`, `date`, `title`, `content`
    - 페이징: `page`, `size` (기본값: 0, 10)

#### 2.3. 일정 단건 조회 (댓글 포함)
- 특정 사용자의 일정 상세 내용을 조회하며, 해당 일정에 달린 댓글도 함께 조회할 수 있다.
-  댓글 목록은 페이지 단위로 포함됨

#### 2.4. 일정 수정
- 로그인한 사용자가 본인의 일정을 수정할 수 있다.
- 수정할 내용이 없으면 수정되지 않음
- 본인의 일정이 아니면 401 예외 발생
- **입력값:**
    - `date`, `title`, `content` 중 선택

#### 2.5. 일정 삭제
- 로그인한 사용자가 본인의 일정을 삭제할 수 있다.
- 본인의 일정이 아닌 경우 삭제할 수 없음

<br>

### 3. 댓글 기능

#### 3.1. 댓글 작성
- 로그인한 사용자가 특정 일정에 댓글을 작성할 수 있다.
- 작성자 ID 및 일정 ID는 자동 연동됨
- `createdAt`, `updatedAt`은 자동 기록됨
- **입력값:**
    - `content`: 댓글 내용 (TEXT)

#### 3.2. 댓글 목록 조회
- 특정 일정에 달린 댓글 목록을 페이지 단위로 조회할 수 있다.
- `updatedAt` 기준 내림차순 정렬
- **조회 조건:**
    - `scheduleId`
    - `page`, `size` (기본값 0, 10)

#### 3.3. 댓글 단건 조회
- 특정 댓글의 상세 내용을 조회할 수 있다.

#### 3.4. 댓글 수정
- 로그인한 사용자가 본인의 댓글을 수정할 수 있다.
- 기존 내용과 같으면 수정되지 않음
- 본인 댓글이 아니면 수정 불가
- **입력값:**
    - `content`: 수정할 댓글 내용

#### 3.5. 댓글 삭제
- 로그인한 사용자가 본인의 댓글을 삭제할 수 있다.

<br>

## API 명세서

### 1. 회원 API

#### 1.1 회원가입
- **POST** `/users/signup`
- **Request Body**
```json
{
  "name": "홍길동",
  "email": "hong@example.com",
  "password": "Test@1234"
}
```
- **Response**
```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com"
}
```
- **Status Code:** `201 Created`

#### 1.2 로그인
- **POST** `/users/login`
- **Request Body**
```json
{
  "email": "hong@example.com",
  "password": "Test@1234"
}
```
- **Response**
```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com"
}
```
- **Status Code:** `200 OK`

#### 1.3 내 정보 조회
- **GET** `/users/me`
- **Response**
```json
{
  "id": 1,
  "name": "홍길동",
  "email": "hong@example.com"
}
```
- **Status Code:** `200 OK`

#### 1.4 이름 변경
- **PATCH** `/users/info/name`
- **Request Body**
```json
{
  "name": "김철수"
}
```
- **Status Code:** `200 OK`

#### 1.5 비밀번호 변경
- **PATCH** `/users/info/password`
- **Request Body**
```json
{
  "oldPassword": "Test@1234",
  "newPassword": "NewPass@5678"
}
```
- **Status Code:** `200 OK`

#### 1.6 로그아웃
- **POST** `/users/logout`
- **Status Code:** `200 OK`

#### 1.7 회원 탈퇴
- **DELETE** `/users`
- **Status Code:** `204 No Content`

### 2. 일정 API

#### 2.1 일정 등록
- **POST** `/schedules`
- **Request Body**
```json
{
  "date": "2025-04-03",
  "title": "할 일 제목",
  "content": "할 일 상세 내용"
}
```
- **Response**
```json
{
  "id": 1,
  "userName": "홍길동",
  "date": "2025-04-03",
  "title": "할 일 제목",
  "content": "할 일 상세 내용",
  "commentCount": 0,
  "comments": null
}
```
- **Status Code:** `201 Created`

#### 2.2 일정 목록 조회
- **GET** `/schedules/{userId}?page=0&size=10&year=2025&month=4`
- **Response**
```json
{
  "content": [
    {
      "id": 1,
      "userName": "홍길동",
      "date": "2025-04-03",
      "title": "할 일 제목 1",
      "content": "할 일 상세 내용 1",
      "commentCount": 0,
      "comments": null
    },
    {
      "id": 2,
      "userName": "홍길동",
      "date": "2025-04-03",
      "title": "할 일 제목 2",
      "content": "할 일 상세 내용 2",
      "commentCount": 0,
      "comments": null
    },
    {
      "id": 3,
      "userName": "홍길동",
      "date": "2025-04-03",
      "title": "할 일 제목 3",
      "content": "할 일 상세 내용 3",
      "commentCount": 0,
      "comments": null
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 20,
  "totalPages": 2,
  "first": true,
  "last": false,
  "empty": false
}
```
- **Status Code:** `200 OK`
 수정일 기준 내림차순 정렬, 댓글 수 포함됨

#### 2.3 일정 단건 조회 (댓글 포함)
- **GET** `/schedules/{userId}/{scheduleId}`
- **Response**
```json
{
  "id": 1,
  "userName": "홍길동",
  "date": "2025-04-03",
  "title": "할 일 제목",
  "content": "할 일 상세 내용",
  "commentCount": 3,
  "comments": {
    "content": [
      {
        "id": 1,
        "scheduleId": 1,
        "userName": "댓글 작성자 이름",
        "content": "댓글 내용",
        "updatedAt": "2025-04-02T23:17:32.970298"
      },
      {
        "id": 2,
        "scheduleId": 1,
        "userName": "댓글 작성자 이름",
        "content": "댓글 내용",
        "updatedAt": "2025-04-02T23:17:32.970298"
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 2,
    "totalPages": 1,
    "first": true,
    "last": true,
    "empty": false
  }
}
```
- **Status Code:** `200 OK`

#### 2.4 일정 수정
- **PATCH** `/schedules/{id}`
- **Request Body**
```json
{
  "date": "2025-04-05",
  "title": "수정된 제목",
  "content": "수정된 내용"
}
```
- **Status Code:** `200 OK`

#### 2.5 일정 삭제
- **DELETE** `/schedules/{id}`
- **Status Code:** `204 No Content`

### 3. 댓글 API

#### 3.1 댓글 작성
- **POST** `/comments/{scheduleId}`
- **Request Body**
```json
{
  "content": "댓글 내용입니다."
}
```
- **Response**
```json
{
  "id": 1,
  "content": "댓글 내용입니다.",
  "writer": "홍길동",
  "createdAt": "2025-04-03T11:17:32.970298"
}
```
- **Status Code:** `201 Created`

#### 3.2 댓글 목록 조회
- **GET** `/comments/schedule/{scheduleId}?page=0&size=5`
- **Response**
```json
{
  "content": [
    {
      "id": 1,
      "scheduleId": 1,
      "userName": "댓글 작성자 이름",
      "content": "댓글 내용",
      "updatedAt": "2025-04-02T23:17:32.970298"
    },
    {
      "id": 2,
      "scheduleId": 1,
      "userName": "댓글 작성자 이름",
      "content": "댓글 내용",
      "updatedAt": "2025-04-02T23:17:32.970298"
    },
    {
      "id": 3,
      "scheduleId": 1,
      "userName": "댓글 작성자 이름",
      "content": "댓글 내용",
      "updatedAt": "2025-04-02T23:17:32.970298"
    },
    {
      "id": 4,
      "scheduleId": 1,
      "userName": "댓글 작성자 이름",
      "content": "댓글 내용",
      "updatedAt": "2025-04-02T23:17:32.970298"
    }
  ],
  "pageNumber": 0,
  "pageSize": 5,
  "totalElements": 13,
  "totalPages": 3,
  "first": true,
  "last": false,
  "empty": false
}
```
- **Status Code:** `200 OK`

#### 3.3 댓글 단건 조회
- **GET** `/comments/{commentId}`
- **Response**
```json
{
  "id": 1,
  "content": "댓글 내용입니다.",
  "writer": "홍길동",
  "createdAt": "2025-04-03T11:17:32.970298"
}
```
- **Status Code:** `200 OK`

#### 3.4 댓글 수정
- **PATCH** `/comments/{commentId}`
- **Request Body**
```json
{
  "content": "수정된 댓글입니다."
}
```
- **Status Code:** `200 OK`

#### 3.5 댓글 삭제
- **DELETE** `/comments/{commentId}`
- **Status Code:** `204 No Content`

<br>

## 4. 예외 처리
### 4.1 사용자 정의 예외 코드

| 번호 | HTTP 상태 코드 | 예외 코드 | 예외 메시지                    |
|------|----------------|-------------|---------------------------|
| 1 | 409 CONFLICT | DUPLICATE_EMAIL | 이미 가입된 이메일입니다.            |
| 2 | 401 UNAUTHORIZED | NOT_LOGINED | 로그인되지 않은 사용자입니다.          |
| 3 | 401 UNAUTHORIZED | LOGIN_FAILED | 이메일 또는 비밀번호가 일치하지 않습니다.   |
| 4 | 401 UNAUTHORIZED | UNAUTHORIZED_ACCESS | 잘못된 접근입니다.                |
| 5 | 400 BAD_REQUEST | VALIDATION_FAILED | 입력 값이 유효하지 않습니다.          |
| 6 | 400 BAD_REQUEST | INVALID_DATE_FORMAT | 올바른 날짜 형식이 아닙니다.          |
| 7 | 404 NOT_FOUND | NOT_FOUND_USER | 사용자가 존재하지 않습니다.           |
| 8 | 404 NOT_FOUND | NOT_FOUND_SCHEDULE | 일정이 존재하지 않습니다.            |
| 9 | 404 NOT_FOUND | NOT_FOUND_COMMENT | 댓글이 존재하지 않습니다.            |
| 10 | 404 NOT_FOUND | UPDATE_FAILED | 데이터 변경에 실패했습니다.           |
| 11 | 404 NOT_FOUND | DELETE_FAILED | 데이터 삭제에 실패했습니다.           |
| 12 | 400 BAD_REQUEST | INVALID_PASSWORD | 비밀번호가 일치하지 않습니다.          |
| 13 | 204 NO_CONTENT | NO_CHANGES | 변경된 내용이 없습니다.             |
| 14 | 500 INTERNAL_SERVER_ERROR | RELOAD_FAILED | 데이터를 불러오는 데 실패했습니다.       |

<hr>

▶ [일정 관리 API (JPA) 트러블 슈팅](https://withong.github.io/project/schedule-02/)