package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE) // Ý nghĩa để test với database thực tế. Nếu chỉ có @AutoConfigureTestDatabase thì sẽ test với database ảo
@Rollback(false) //annotation để cho hibernate không thực hiện tạo lại bảng sau khi tạo xong mà chỉ insert dữ liệu vào bảng đã tạo
public class UserRepositoryTest {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testNewUserWithOneRole() { //Khi phương thức này để trống => tạo table
		Role roleAdmin = entityManager.find(Role.class, 5);
		User tungDm4 = new User("quang@fsoft.com.vn", "password", "tung", "dominh");
		tungDm4.addRole(roleAdmin);
		
		User savedUser = repo.save(tungDm4);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		User userTrang = new User("trinhtrangvt91@gmail.com", "trangvt91", "trang", "trinh thi thu");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userTrang.addRole(roleEditor);
		userTrang.addRole(roleAssistant);
		
		User savedUser = repo.save(userTrang);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllusers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserbyId() {
		User userTrang = repo.findById(2).get();
		System.out.println(userTrang);
		assertThat(userTrang).isNotNull();
	}
	
	@Test
	public void testUpdateUser() {
		User userTrang = repo.findById(2).get();
		userTrang.setEnable(true);
		userTrang.setEmail("pigbong@gmail.com");
		
		repo.save(userTrang);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userTrang = repo.findById(2).get();
		Role reoleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);
		
		userTrang.getRoles().remove(reoleEditor);
		userTrang.addRole(roleSalesPerson);
		
		repo.save(userTrang);	
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 4;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "fdsfsdfsd";
		User userByEmail = repo.getUserByEmail(email);
	}
	
	@Test
	public void testCountById() {
		Integer id = 2;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 60;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 5;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
}
