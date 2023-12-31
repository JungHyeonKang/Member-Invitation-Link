# Member-Invitation-Link
회원 초대 링크 생성 기능 프로젝트입니다.

프로젝트 다운로드 링크 : https://drive.google.com/file/d/1qZLORJsCuxSep3-NMhcGdelRCzFpGcB3/view?usp=drive_link

#  API 명세

### 회원 초대 API

POST `/api/invite`

**Request Body**
```
{
  "memberId": "String",
  "memberName": "String",
  "memberPhone": "String",
  "memberEmail": "String"
}
```

**Response Body**

```
{
  "inviteCode": "String"
}
```

### 초대 수락 API

POST `/api/join/{inviteCode}`

**Response Body**
```
{
   "message": "String"
}
```

### DTO

**InviteRequestDto**
```
{
  "memberId": "String",
  "memberName": "String",
  "memberPhone": "String",
  "memberEmail": "String",
}
```

**InviteResponseDto**
```
{
  "inviteCode": "String"
}
```

**JoinResponseDto**
```
{
  "message": "String"
}
```

**ErrorResponseDto**
```
{
  "code": "String",
  "message": "String",
  "validationError": "List"
}
```

