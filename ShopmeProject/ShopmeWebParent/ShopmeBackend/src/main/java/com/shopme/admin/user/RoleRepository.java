package com.shopme.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Role;

@Repository // cho thấy rằng interface này tương tác với csdl
public interface RoleRepository extends CrudRepository<Role,Integer> { // Role : tên opject tương ứng với table. Interger : kiểu dữ liệu của primarykey của table

}
