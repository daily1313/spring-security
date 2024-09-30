## basic-auth 

**Spring Security**

- 스프링 기반 어플리케이션의 보안 (인증, 권한, 인가)를 담당하는 스프링 하위 프레임워크
- 보안과 관련해서 체계적으로 많은 옵션들을 제공해주기 때문에 개발자의 입장에서는 보안 관련 로직을 직접 작성하지 않아도 되는 장점이 있습니다

**관련 용어**

- Authentication
    - 해당 사용자가 본인이 맞는지 확인하는 절차
- Authorization
    - 인증된 사용자가 요청한 자원에 접근 가능한지를 결정하는 절차
- Principal
    - 보호받는 Resource에 접근하는 대상
- Credential
    - Resource에 접근하는 대상의 비밀번호
- 권한
    - 인증된 주체가 어플리케이션의 동작을 수행할 수 있도록 허락되어있는지를 결정
    - 인증 과정을 통해 주체가 증명된 이후 권한 부여
- SecurityContextHolder
    - SecurityContext 객체를 저장하고 있는 wrapper 클래스
    - 보안 주체의 세부 정보를 포함하여 응용프로그램의 현재 보안 컨텍스트에 대한 세부 정보 저장
- SecurityContext
    - Authentication을 보관하는 역할
    - ThreadLocal에 저장되어 동일 스레드인 경우, 아무 곳에서나 참조가 가능
    - 즉, SecurityContext는 ThreadLocal에 저장되어 있으면서, 동시에 HttpSession에도 저장되어 있습니다
- SecurityFilterChain
    - Spring Security는 표준 서블릿 필터를 기반으로 동작
    - SpringBoot의 기본 설정을 사용하는 경우, 인증에 사용되는 Filter들이 모여있는 SecurityFilterChain을 자동으로 등록해주는데 SecurityFilterChain을 통해 스프링 시큐리티의 인증 과정이 동작하게 됩니다
    - 이때 FilterChainProxy라는 클래스가 등장하는데, 해당 클래스는 DelegatingFilterProxy로부터 Filter 작동에 대한 요청을 위임받아 실제로 인증 처리를 하는 클래스입니다
    - Spring Security를 사용하면 모든 요청은 FilterChainProxy 클래스를 거치게 되며, 내부에 getFilters() 메서드를 통해 SecurityFilterChain의 Filter 목록을 가져오게 됩니다
- SecurityContextPersistenceFilter
    - SecurityContext 객체의 생성, 조회, 저장 등의 LifeCycle을 담당하는 필터입니다
    - 실제로는 SecurityContextRepository interface의 구현체인 HttpSessionSecurityContextRepository가 구현체로 사용되고 있습니다

**Spring Security의 대략적인 과정**

- Authentication → Success → Authorization
- 인증 절차를 마친 후 인가 절차를 시행
- 인가 과정에서 해당 리소스에 대한 접근권한이 있는지를 확인
    - Principal : id, Credential: password로 사용하는 인증방식

인증 정보는 최종적으로 인메모리 세션 저장소인 SecurityContextHolder에 세션-쿠키 방식으로 저장

Spring Security Architecture
<img width="608" alt="architecture" src="https://github.com/user-attachments/assets/e9dd6c6c-dfb8-4826-8606-2b6812fb2342">

**동작 절차**

1. **ID, PASSWORD 기반의 인증 요청을 UsernamePasswordAuthenticationFilter에서 가로챕니다**
2. **UsernamePasswordAuthentication 객체를 AuthenticationManager 인터페이스를 구현한 ProviderManager에게 넘깁니다**
3. **ProviderManager는 AuthenticationProvider를 순회하면서 UsernamePasswordAuthenticationToken을 처리해줄 AuthenticationProvider를 찾습니다**
4. **AuthenticationProvider 인터페이스를 구현한 CustomAuthenticationProvider에서 실질적인 인증 로직을 수행합니다.**
    - **이때 authenticate() 메서드가 호출되어 전달된 UsernamePasswordAuthenticationToken을 처리하고, 사용자 인증 정보를 확인합니다. 지원하는 토큰 유형을 확인하기 위해 supports() 메서드가 호출됩니다.**
5. **CustomAuthenticationProvider**
    - **UserDetailsService 인터페이스를 구현한 CustomUserDetailsService의 loadUserByUsername 메서드를 호출하여 사용자의 세부 정보를 로드합니다.**
    - **근본적으로 DB에 있는 사용자 정보와 인증용 객체에 담긴 정보를 비교합니다.**
    - **이 메서드는 사용자 이름을 기반으로 UserDetails 객체를 반환하며, 이 객체는 인증 절차에서 사용됩니다.**
    - **인증용 객체를 가져오는 방법: AuthenticationProvider 인터페이스에서는 authenticate() 메서드를 오버라이딩하여 인증용 객체를 파라미터로 받아 사용자가 입력한 정보를 가지고 올 수 있습니다**
6. **인증절차를 통해 인증이 완료되면 Authentication을 SecurityContextHolder 객체 안의 SecurityContext에 저장**
    - **Authentication authentication = SecutiryContextHolder.getContext().getAuthentication();**