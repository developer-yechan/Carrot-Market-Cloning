# 당근마켓 클론 코딩 프로젝트

## 목차
[소개](#소개)

[ERD](#ERD)

[주요 기능](#주요-기능)

[문제 해결](#문제-해결)

[기술 스택](#기술-스택)

[회고](#회고)


## 소개

IT 프로젝트 플랫폼 numble에서 주최한 'Spring, Spring Boot로 당근마켓 초기버전 클론코딩하기'에 참여하여  
개발 요구 사항 및 와이어프레임을 제공 받아 구현한 결과물입니다.

### 배포 URL
http://54.180.41.136:8080/members/login  
현재 AWS Free Tier 플랜 사용량 한계로 인해 내린 상태입니다.  
~~(id: abc@naver.com, pw: 123 으로 로그인 가능)~~

## ERD

![image](https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/33354e69-0404-4df9-b073-ec642592dd51)

## 주요 기능

1. Spring Security 활용한 회원가입 /로그인 기능
   <p align="center">
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/cb735a68-40c8-4393-948d-5623490c6e9b.jpg" width="100" height="200"/>
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/7de04ba8-94b7-4364-aa75-c1059b868a29.jpg" width="100" height="200"/>
   </p>
   <br>
2. 중고거래글 CRUD 기능
   <p align="center">
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/c2a22c13-b775-4566-955b-b93d60f213a4.jpg" width="100" height="200"/>
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/e67a06ad-9719-4cc7-923d-018b5ebb63a2.jpg" width="100" height="200"/>
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/e797e77c-f043-4755-bd91-5eb259e29b58.jpg" width="100" height="200"/>
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/8067a6a2-9b52-46c3-9783-59c8cce77116.jpg" width="100" height="200"/>
   </p>
   <p align="center">
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/952af6e5-889b-4f52-a2c3-aa4d98c60572.jpg" width="100" height="200"/>
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/37336ace-57dc-4477-b27c-906ac7bc7f5a.jpg" width="100" height="200"/>
   </p>
   <br>
3. 1:1 채팅 기능(웹 소켓 통신 채팅)
   <p align="center">
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/6ddfd2fa-8533-46a4-80f4-9b7d6a74614f.jpg" width="100" height="200"/>
      <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/e4671967-bfce-43b2-911b-bbe00d695ac2.jpg" width="100" height="200"/>
  </p>
  <br>
4. 마이페이지 기능(프로필 수정, 판매내역, 관심목록, 채팅목록)
  <p align="center">
    <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/ab265c6e-dbf4-4abe-849e-0b4c49a8b9b9.jpg" width="100" height="200"/>
    <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/503eae3c-31b1-4895-804d-23a304b9a29e.jpg" width="100" height="200"/>
    <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/b6073f37-b2c3-4bba-91a7-f91a87b4a8d3.jpg" width="100" height="200"/>
  </p>
  <p align="center">
    <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/f1b4df5a-bc7c-4334-8fb3-106757684914.jpg" width="100" height="200"/>
    <img src="https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/d8e08624-def5-4a0b-8f6c-c62dfde92f50.jpg" width="100" height="200"/>
  </p>


## 문제 해결
멤버 프로필 수정 시 처음에는 프로필 사진만 업로드 하고 두번째 수정할 때는 닉네임만 수정해봤는데 분명 닉네임만 수정했는데  
수정된 결과를 보니 프로필 사진이 사라져있었다.
db를 확인해보니 기존에 저장 되있던 프로필 사진 저장 경로가  
빈 문자열로 업데이트 되어있었다.
```
if(editMemberDto.getImageFile() != null){
             String storeFileName = fileStore.storeProfileImage(editMemberDto.getImageFile());
             member.setProfileImage(storeFileName);
            }
        }
```
기존에 editMemberDto에 이미지 파일이 null이 아니면 프로필 사진을 수정하는 방식으로 로직을 작성 했었는데  
이미지 파일을 넣어주지 않아도 null값이 아닌 값이 들어와서 위의 문제가 발생했다는 것을 알게 되었고   
어떻게 조건문을 수정해줘야 이미지파일을 넣어주지 않았을 땐 프로필 사진 수정이 되지 않게 해줄 수 있을까 고민하다가  
다음과 같이 코드를 작성했다.
```
if(editMemberDto.getImageFile() != null){
            System.out.println(editMemberDto.getImageFile().toString());
            System.out.println(editMemberDto.getImageFile().getContentType());
            System.out.println(editMemberDto.getImageFile().getOriginalFilename());
            if(StringUtils.hasText(editMemberDto.getImageFile().getOriginalFilename())){
                System.out.println(111);
            }
            String storeFileName = fileStore.storeProfileImage(editMemberDto.getImageFile());
            member.setProfileImage(storeFileName);
        }
```
print된 결과
![image](https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/6bc78e36-d39f-4b66-9563-a02bbbf142e8)
결과를 보니 이미지 파일을 넣어주지 않았을 때 이미지 파일의 originalFilename 속성 값이 null이 된다는 것을 확인할 수 있었고 
최종적으로
```
    if(StringUtils.hasText(editMemberDto.getImageFile().getOriginalFilename())){
            String storeFileName = fileStore.storeProfileImage(editMemberDto.getImageFile());
            member.setProfileImage(storeFileName);
        }
```
이런 식으로 코드를 작성해줌으로서 문제를 해결할 수 있었다.

## 기술 스택
### Java, Spring, Spring Boot, Spring Security, WebSocket, JPA, MySQL, AWS EC2, RDS, S3

## 회고
https://bit.ly/3Nz4DyH
