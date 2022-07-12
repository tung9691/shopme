package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE) // Ý nghĩa để test với database thực tế. Nếu chỉ có @AutoConfigureTestDatabase thì sẽ test với database ảo
@Rollback(false) //annotation để cho hibernate không thực hiện tạo lại bảng sau khi tạo xong mà chỉ insert dữ liệu vào bảng đã tạo
public class RoleRepositoryTests {

	@Autowired //Để nói cho spring framework biết rằng sẽ tạo 1 instance của RoleRepository interface ở đây
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin","manage everything"); //tạo 1 object của table với tham số truyền vào là name và description
		Role savedRole = repo.save(roleAdmin);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles() {
		Role roleSalesperson = new Role("Salesperson", "manage produc price, customer, shipping, order and sales report");
		Role roleEditor = new Role("Editor", "manage categories, brands, products, articles and menus");
		Role roleShipper = new Role("Shipper", "view product, view order");
		Role roleAssistant = new Role("Assistant", "manage question and review");
		
		repo.saveAll(List.of(roleSalesperson,roleEditor,roleShipper,roleAssistant)); //save tất cả các object vào table
	}
	
}
