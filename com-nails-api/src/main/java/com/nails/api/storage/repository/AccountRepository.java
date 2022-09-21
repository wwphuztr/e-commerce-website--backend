package com.nails.api.storage.repository;

import com.nails.api.storage.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    public Account findAccountByUsername(String username);
    public Account findAccountByEmail(String email);
    public Account findByPhoneOrPhone(String phone, String adminPhone);
    public Long countAccountByPhone(String phone);
    public Account findAccountByResetPwdCode(String resetPwdCode);
    public Account findAccountByEmailOrUsername(String email,String username);
    public Long countAccountByUsernameOrEmailOrPhone(String username, String email, String phone);
    public Page<Account> findAllByKind(int kind, Pageable pageable);
    public Long countAccountByEmail(String email);

    void deleteByUsername(String username);
}
