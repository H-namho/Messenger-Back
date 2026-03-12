package com.example.messengerproj.auth.service;

import com.example.messengerproj.auth.domain.RoleType;
import com.example.messengerproj.auth.domain.SignupRequestDto;
import com.example.messengerproj.auth.domain.UserEntity;
import com.example.messengerproj.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    
    @Transactional
    public Long signup(SignupRequestDto dto) { // 회원가입
        boolean chk = userRepository.existsByEmail(dto.email());
        if(chk){
            throw new IllegalArgumentException("이미 가입된 이메일입니다");
        }
        if(!isValidDomain(dto.email())){
            throw new IllegalArgumentException("존재하지 않는 이메일 주소입니다.");
        }
        String password = encoder.encode(dto.password());
        UserEntity user = UserEntity.builder()
                .email(dto.email())
                .password(password)
                .nickname(dto.nickname())
                .role(RoleType.ROLE_USER)
                .isLock(false)
                .build();
        return userRepository.save(user).getId();
    }
    public boolean isValidDomain(String email) { // 실제 존재하는 이메일도메인 체크
        try {
            // 이메일에서 @ 이후부터 추출
            String domain = email.substring(email.indexOf('@') + 1);

            // DNS 조회를 위한 환경 설정
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ictx = new InitialDirContext(env);

            // 해당 도메인의 MX(Mail Exchange) 레코드를 조회
            Attributes attrs = ictx.getAttributes(domain, new String[] {"MX"});

            // MX 레코드가 존재하면 실제 메일 서버가 있는 도메인임
            return attrs.get("MX") != null;
        } catch (Exception e) {
            // 조회 실패 시 (존재하지 않는 도메인 등) false 반환
            return false;
        }
    }
    // 추후 mailsender 의존성 추가해 이메일 인증 고려
}
