# 당근마켓 클론 코딩 프로젝트

## 목차
[소개](#소개)

[ERD](#ERD)

[주요 기능](#주요-기능)

[문제 해결](#문제-해결)

[기술 스택](#기술-스택)


## 소개

IT 프로젝트 플랫폼 numble에서 주최한 'Spring, Spring Boot로 당근마켓 초기버전 클론코딩하기'에 참여하여  
개발 요구 사항 및 와이어프레임을 제공 받아 구현한 결과물입니다.

### 배포 URL
http://54.180.41.136:8080/members/signup  
// 현재 S3 파일 업로드 작업 중, 그 외 기능 동작

## ERD

![image](https://github.com/developer-yechan/Carrot-Market-Cloning/assets/99064214/33354e69-0404-4df9-b073-ec642592dd51)

## 주요 기능
1. 회원가입/로그인 기능
2. 중고거래글 CRUD 기능
3. 관심상품 저장,삭제 기능
4. 1:1 채팅 기능(웹 소켓 통신 채팅)
5. 마이페이지 기능(프로필 수정,판매내역, 관심목록, 채팅목록)

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
### Java, Spring, Spring Boot, JPA, MySQL
