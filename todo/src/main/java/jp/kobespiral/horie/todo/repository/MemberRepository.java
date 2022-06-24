package jp.kobespiral.horie.todo.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import jp.kobespiral.horie.todo.entity.Member;//変える

@Repository
public interface MemberRepository extends CrudRepository<Member, String> {
    List<Member> findAll();
}