package com.myapp.service;

import com.myapp.entity.Member;
import com.myapp.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional //로직을 처리하다가 에러가 발생하였다면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백 시켜줍니다.
@RequiredArgsConstructor //빈을 주입하는 방법으로 @Autowired나 필드 주입(Setter 주입), 생성자 주입을 이용하는 방법이 있는데, @RequiredArgConstructor은 final이나 @NonNull이 붙은 필드에 생성자를 생성해준다.

public class MemberService implements UserDetailsService {

    // 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면 @Autowired 어노테이션 없이 의존성 주입이 가능하다.
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            //이미 가입된 경우 IllegalStateException 예외를 발생시킨다.
            //IllegalArgumentException은 메서드에 전달된 인지가 잘못되었을때 사용하고 IllegalStateException은 객체의 현재 상태가 부적절할 때 사용된다.
            //IllegalArgumentException은 "당신이 준 정보가 잘못되었어요", IllegalStateException은 "지금은 그걸 할 수 있는 상황이 아니에요"
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getName())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
