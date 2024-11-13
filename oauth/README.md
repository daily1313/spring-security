## OAuth 2.0

**OAuth 2.0**

* 인증을 위한 개방형 표준 프로토콜
* 사용자가 비밀번호를 공유하지 않고도 타사 애플리케이션에 다른 웹사이트나 서비스의 데이터에 대한 액세스 권한을 부여할 수 있는 단순한 산업 표준 프로토콜
* Third-Party 애플리케이션에 Resource Owner를 대신하여 Resource Server에서 제공하는 자원에 대한 접근 권한을 위임하는 방식을 제공합니다.
* 구글, 카카오, 네이버 등에서도 OAuth2 프로토콜 기반의 사용자 인증 기능을 제공하고 있습니다.

**구성요소**

<img width="600" alt="스크린샷 2024-11-13 오후 11 25 36" src="https://github.com/user-attachments/assets/dd46fdb5-3cd7-458f-9e7a-e1dba45c2212">

* Resource Owner: 리소스 소유자, 본인의 정보에 접근할 수 있는 자격 승인을 요청하는 주체
* Client: Resource Owner를 대신하여 Authorization Server, Resource Server에 접근하는 주체, Application을 말합니다. 
* Resource Server: Resource Owner의 정보가 저장된 서버
* Authorization Server: 권한 서버, 인증/인가를 수행하는 서버로 Client의 접근 자격과 Access Token을 발행하여 권한을 부여하는 서버

**용어**

* Authentication: 인증, 접근 자격이 있는지 검증하는 단계
* Authorization: 자원에 접근할 권한을 부여하고 리소스 접근 권한이 담긴 Access Token을 발행합니다.
* Access Token: 리소스 정보를 요청할 때 사용하는 토큰 
* Refresh Token: Access Token 만료 시 재발급을 받기 위한 토큰

**OAuth 2.0 대표적인 권한 부여 방식: Authorization Code Grant**

<img width="600" alt="스크린샷 2024-11-13 오후 11 24 14" src="https://github.com/user-attachments/assets/51a3f17d-62de-491d-8ad9-ed9f33d85554">

1. Resource Owner가 Login 요청 
2. Client가 Authorization Server에 Authorization URL에 response_type, client_id, redirect_uri, scope 등의 매개변수를 쿼리 스트링으로 포함하여 Login을 요청합니다.
3. Authroization Server는 Resource Owner에게 로그인 페이지를 제공하고, Resource Owner는 제공된 로그인 페이지에서 id/pw를 입력합니다.
4. 인증을 성공하면, Authorization Code 발급 및 Redirect URI로 사용자를 Redirection 시킵니다.
5. Client는 Authorization Code를 기반으로 Authorization Server에게 Token을 요청합니다.
6. Token 반환 후, 해당 Token을 기반으로 Resource에 접근할 수 있게 됩니다.